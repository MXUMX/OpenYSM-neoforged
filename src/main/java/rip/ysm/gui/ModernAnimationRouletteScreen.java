/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.tuple.MutablePair
 *  org.apache.commons.lang3.tuple.Pair
 */
package rip.ysm.gui;

import org.openysm.capability.PlayerCapability;
import org.openysm.client.event.AnimationLockEvent;
import org.openysm.client.gui.ModelMetadataPresenter;
import org.openysm.client.gui.custom.ExtraAnimationButtons;
import org.openysm.client.input.AnimationRouletteKey;
import org.openysm.client.input.ExtraAnimationKey;
import org.openysm.client.model.ModelAssembly;
import org.openysm.config.GeneralConfig;
import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SPlayAnimationPacket;
import org.openysm.util.data.OrderedStringMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import rip.ysm.api.client.KeyMappingFactory;
import rip.ysm.gpu.BlurStack;
import rip.ysm.gpu.Pie;
import rip.ysm.gui.ModelSettingsScreen;

public class ModernAnimationRouletteScreen
extends Screen {
    private static final ResourceLocation settingsIcon = ResourceLocation.fromNamespaceAndPath("openysm", "texture/settings.png");
    private static final ResourceLocation lockIcon = ResourceLocation.fromNamespaceAndPath("openysm", "texture/lock.png");
    private static final ResourceLocation unlockIcon = ResourceLocation.fromNamespaceAndPath("openysm", "texture/unlock.png");
    private static final LinkedList<Pair<String, Integer>> navigationStack = Lists.newLinkedList();
    private static String lastModelId = "";
    private int centerX;
    private int centerY;
    private int hoveredIndex = -1;
    private int hoveredGearIndex = -1;
    private int hoveredPathSegment = -1;
    private boolean hoveredPrev;
    private boolean hoveredNext;
    private Pair<String, Integer> currentNavEntry;
    private final OrderedStringMap<String, String> currentProperties;
    private final Map<String, ExtraAnimationButtons> renderGroups;
    private final Map<String, OrderedStringMap<String, String>> textProperties;
    private final AnimatableEntity<?> animatableModel;
    private final ModelAssembly renderContext;

    public ModernAnimationRouletteScreen(String modelId, ModelAssembly modelAssembly, AnimatableEntity<?> animatable) {
        super((Component)Component.literal((String)"YSM Roulette"));
        this.renderContext = modelAssembly;
        this.animatableModel = animatable;
        this.textProperties = modelAssembly.getModelData().getModelProperties().getExtraAnimationClassify();
        this.renderGroups = modelAssembly.getModelData().getModelProperties().getExtraAnimationButtons();
        if (!lastModelId.equals(modelId)) {
            navigationStack.clear();
            lastModelId = modelId;
        }
        if (navigationStack.isEmpty()) {
            navigationStack.add(MutablePair.of("", 0));
        }
        this.currentNavEntry = navigationStack.peekLast();
        if (this.textProperties.containsKey(this.currentNavEntry.getLeft())) {
            this.currentProperties = this.textProperties.get(this.currentNavEntry.getLeft());
        } else {
            this.currentProperties = modelAssembly.getModelData().getModelProperties().getExtraAnimation();
            navigationStack.clear();
            navigationStack.add(MutablePair.of("", this.currentNavEntry.getRight()));
            this.currentNavEntry = navigationStack.peekLast();
        }
    }

    protected void init() {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        if ((Integer)this.currentNavEntry.getRight() >= this.pageCount()) {
            this.currentNavEntry.setValue(0);
        }
    }

    private int pageCount() {
        return Math.max(1, (this.currentProperties.size() + 7) / 8);
    }

    private int page() {
        return (Integer)this.currentNavEntry.getRight();
    }

    private float sliceStartOffset() {
        return -0.3926991f;
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (GeneralConfig.BLUR_GUI != null && ((Boolean)GeneralConfig.BLUR_GUI.get()).booleanValue()) {
            this.collectAndFlushBlur(g);
        }
        this.updateHover(mouseX, mouseY);
        this.renderSlices(g);
        this.renderLabels(g);
        this.renderCenter(g);
        this.renderPageButtons(g);
        this.renderPathAndPage(g, mouseX, mouseY);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void collectAndFlushBlur(GuiGraphics g) {
        float sliceSpan = 0.7853982f;
        for (int i = 0; i < 8; ++i) {
            int absoluteIdx = i + this.page() * 8;
            if (absoluteIdx >= this.currentProperties.size()) continue;
            float start = this.sliceStartOffset() + (float)i * sliceSpan + 0.02f;
            float end = this.sliceStartOffset() + (float)(i + 1) * sliceSpan - 0.02f;
            BlurStack.pushBlurPie(this.centerX, this.centerY, 22.0f, 100.0f, start, end, 20.0f);
        }
        if (this.pageCount() > 1) {
            BlurStack.pushBlurPie((float)this.centerX - 128.0f, this.centerY, 0.0f, 16.0f, 0.0f, (float)Math.PI * 2, 20.0f);
            BlurStack.pushBlurPie((float)this.centerX + 128.0f, this.centerY, 0.0f, 16.0f, 0.0f, (float)Math.PI * 2, 20.0f);
        }
        BlurStack.flush(g);
    }

    private void updateHover(int mouseX, int mouseY) {
        float dx = mouseX - this.centerX;
        float dy = mouseY - this.centerY;
        float r = (float)Math.sqrt(dx * dx + dy * dy);
        float ang = (float)Math.atan2(dy, dx);
        if (ang < 0.0f) {
            ang += (float)Math.PI * 2;
        }
        ang = (ang - this.sliceStartOffset() + (float)Math.PI * 2) % ((float)Math.PI * 2);
        int idx = Mth.clamp((int)((int)(ang / 0.7853982f)), (int)0, (int)7);
        this.hoveredIndex = -1;
        this.hoveredGearIndex = -1;
        int absoluteIdx = idx + this.page() * 8;
        if (absoluteIdx < this.currentProperties.size() && r >= 22.0f && r <= 100.0f) {
            boolean hasGear = this.currentProperties.getValueAt(absoluteIdx).startsWith("#");
            if (hasGear && r <= 46.0f) {
                this.hoveredGearIndex = absoluteIdx;
            } else {
                this.hoveredIndex = absoluteIdx;
            }
        }
        float prevDx = (float)mouseX - ((float)this.centerX - 128.0f);
        float nextDx = (float)mouseX - ((float)this.centerX + 128.0f);
        float btnDy = mouseY - this.centerY;
        this.hoveredPrev = this.page() > 0 && prevDx * prevDx + btnDy * btnDy <= 256.0f;
        this.hoveredNext = (this.page() + 1) * 8 < this.currentProperties.size() && nextDx * nextDx + btnDy * btnDy <= 256.0f;
    }

    private void renderSlices(GuiGraphics g) {
        float sliceSpan = 0.7853982f;
        for (int i = 0; i < 8; ++i) {
            int mainColor;
            int absoluteIdx = i + this.page() * 8;
            if (absoluteIdx >= this.currentProperties.size()) {
                this.drawSlice(g, i, sliceSpan, 22.0f, 100.0f, 0x30000000);
                continue;
            }
            boolean isHover = absoluteIdx == this.hoveredIndex;
            boolean gearHover = absoluteIdx == this.hoveredGearIndex;
            boolean isSubmenu = this.currentProperties.getKeyAt(absoluteIdx).startsWith("#");
            boolean hasGear = this.currentProperties.getValueAt(absoluteIdx).startsWith("#");
            mainColor = isHover ? (isSubmenu ? -788542464 : -1325400065) : (isSubmenu ? 1884627456 : 0x60000000);
            if (hasGear) {
                int gearColor = gearHover ? -788542464 : -2144128205;
                this.drawSlice(g, i, sliceSpan, 46.0f, 100.0f, mainColor);
                this.drawSlice(g, i, sliceSpan, 22.0f, 46.0f, gearColor);
                this.drawSettingsIcon(g, i, sliceSpan, gearHover);
                continue;
            }
            this.drawSlice(g, i, sliceSpan, 22.0f, 100.0f, mainColor);
        }
    }

    private void drawSlice(GuiGraphics g, int sliceIndex, float sliceSpan, float inner, float outer, int color) {
        float start = this.sliceStartOffset() + (float)sliceIndex * sliceSpan + 0.02f;
        float end = this.sliceStartOffset() + (float)(sliceIndex + 1) * sliceSpan - 0.02f;
        Pie.draw(g, this.centerX, this.centerY, inner, outer, start, end, color, 1.0f);
    }

    private void drawSettingsIcon(GuiGraphics g, int sliceIndex, float sliceSpan, boolean hover) {
        float mid = this.sliceStartOffset() + ((float)sliceIndex + 0.5f) * sliceSpan;
        float r = 34.0f;
        int ix = this.centerX + (int)((double)r * Math.cos(mid)) - 8;
        int iy = this.centerY + (int)((double)r * Math.sin(mid)) - 8;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (hover) {
            g.setColor(1.0f, 1.0f, 0.6f, 1.0f);
        }
        g.blit(settingsIcon, ix, iy, 16, 16, 0.0f, 0.0f, 32, 32, 32, 32);
        if (hover) {
            g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RenderSystem.disableBlend();
    }

    private void renderLabels(GuiGraphics g) {
        float sliceSpan = 0.7853982f;
        for (int i = 0; i < 8; ++i) {
            int absoluteIdx = i + this.page() * 8;
            if (absoluteIdx >= this.currentProperties.size()) continue;
            float midAngle = this.sliceStartOffset() + ((float)i + 0.5f) * sliceSpan;
            boolean hasGear = this.currentProperties.getValueAt(absoluteIdx).startsWith("#");
            boolean isSubmenuLink = this.currentProperties.getKeyAt(absoluteIdx).startsWith("#");
            float labelR = (hasGear ? 46.0f : 22.0f) * 0.5f + 50.0f;
            int lx = this.centerX + (int)((double)labelR * Math.cos(midAngle));
            int ly = this.centerY + (int)((double)labelR * Math.sin(midAngle));
            String text = this.displayLabel(absoluteIdx);
            if (StringUtils.isBlank((CharSequence)text)) continue;
            MutableComponent comp = Component.literal((String)text);
            if (isSubmenuLink) {
                comp = comp.withStyle(ChatFormatting.GOLD);
            }
            boolean showKey = this.page() == 0 && navigationStack.size() == 1 && absoluteIdx < ExtraAnimationKey.KEY_MAPPINGS.size();
            int wrapWidth = (int)((100.0f - (hasGear ? 46.0f : 22.0f)) * 0.9f);
            List<FormattedCharSequence> lines = this.font.split(comp, wrapWidth);
            int totalH = lines.size() * 9 + (showKey ? 10 : 0);
            int lineY = ly - totalH / 2;
            for (FormattedCharSequence line : lines) {
                g.drawCenteredString(this.font, line, lx, lineY, -1);
                lineY += 9;
            }
            if (!showKey) continue;
            this.renderKeyBinding(g, absoluteIdx, lx, lineY + 1);
        }
    }

    private void renderKeyBinding(GuiGraphics g, int slot, int x, int y) {
        if (slot >= ExtraAnimationKey.KEY_MAPPINGS.size()) {
            return;
        }
        KeyMapping km = ExtraAnimationKey.KEY_MAPPINGS.get(slot);
        MutableComponent label = Component.literal((String)"[ ").withStyle(ChatFormatting.YELLOW);
        if (km.isUnbound()) {
            label.append((Component)Component.translatable((String)"key.openysm.extra_animation.none"));
        } else {
            label.append(km.getTranslatedKeyMessage());
        }
        label.append(" ]");
        g.drawCenteredString(this.font, (Component)label, x, y, -3166120);
    }

    private String displayLabel(int absoluteIdx) {
        String sub;
        String value;
        String key = this.currentProperties.getKeyAt(absoluteIdx);
        String display = value = this.currentProperties.getValueAt(absoluteIdx);
        if (value.startsWith("#") && this.renderGroups.containsKey(sub = value.substring(1))) {
            display = this.renderGroups.get(sub).getName();
        }
        if (StringUtils.isBlank((CharSequence)display)) {
            display = key;
        }
        return ModelMetadataPresenter.getLocalizedModelString(this.renderContext, "properties.extra_animation.%s".formatted(key), display);
    }

    private void renderCenter(GuiGraphics g) {
        if (this.animatableModel.getEntity() instanceof Player) {
            ResourceLocation tex = AnimationLockEvent.isLocked() ? lockIcon : unlockIcon;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            g.blit(tex, this.centerX - 16, this.centerY - 16, 32, 32, 0.0f, 0.0f, 64, 64, 64, 64);
            RenderSystem.disableBlend();
        } else {
            g.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.openysm.roulette.stop"), this.centerX, this.centerY - 4, -1);
        }
    }

    private void renderPageButtons(GuiGraphics g) {
        if (this.pageCount() <= 1) {
            return;
        }
        this.drawPageButton(g, (float)this.centerX - 128.0f, this.centerY, this.page() > 0, this.hoveredPrev, "<");
        this.drawPageButton(g, (float)this.centerX + 128.0f, this.centerY, (this.page() + 1) * 8 < this.currentProperties.size(), this.hoveredNext, ">");
    }

    private void drawPageButton(GuiGraphics g, float cx, float cy, boolean enabled, boolean hover, String arrow) {
        int color = !enabled ? 0x40000000 : (hover ? -788529153 : -1879048192);
        Pie.draw(g, cx, cy, 0.0f, 16.0f, 0.0f, (float)Math.PI * 2, color, 1.0f);
        int textColor = enabled ? (hover ? -16777216 : -1) : 0x60FFFFFF;
        g.drawCenteredString(this.font, arrow, (int)cx, (int)cy - 4, textColor);
    }

    private void renderPathAndPage(GuiGraphics g, int mouseX, int mouseY) {
        this.layoutAndDrawPath(g, mouseX, mouseY);
        String pageStr = String.format("%d/%d", this.page() + 1, this.pageCount());
        g.drawCenteredString(this.font, (Component)Component.literal((String)pageStr).withStyle(ChatFormatting.AQUA), this.centerX, this.centerY + 108, -1);
    }

    private void layoutAndDrawPath(GuiGraphics g, int mouseX, int mouseY) {
        int pathY = this.centerY - 118;
        String prefix = Component.translatable((String)"gui.openysm.roulette.path.prefix").getString();
        String rootLabel = Component.translatable((String)"gui.openysm.roulette.path.root").getString();
        int prefixW = this.font.width(prefix);
        int sep = this.font.width(" > ");
        int total = prefixW;
        for (int i = 0; i < navigationStack.size(); ++i) {
            String s = (String)navigationStack.get(i).getLeft();
            total += this.font.width(StringUtils.isBlank((CharSequence)s) ? rootLabel : s);
            if (i >= navigationStack.size() - 1) continue;
            total += sep;
        }
        int x = this.centerX - total / 2;
        g.drawString(this.font, prefix, x, pathY, -1, true);
        x += prefixW;
        this.hoveredPathSegment = -1;
        for (int i = 0; i < navigationStack.size(); ++i) {
            boolean hover;
            String raw = (String)navigationStack.get(i).getLeft();
            String s = StringUtils.isBlank((CharSequence)raw) ? rootLabel : raw;
            int w = this.font.width(s);
            boolean isLast = i == navigationStack.size() - 1;
            boolean bl = hover = mouseX >= x && mouseX < x + w && mouseY >= pathY - 2 && mouseY < pathY + 10;
            int color = isLast ? -13312 : (hover ? -1 : -5592406);
            g.drawString(this.font, s, x, pathY, color, true);
            if (hover && !isLast) {
                g.fill(x, pathY + 9, x + w, pathY + 10, color);
                this.hoveredPathSegment = i;
            }
            x += w;
            if (i >= navigationStack.size() - 1) continue;
            g.drawString(this.font, " > ", x, pathY, -7829368, true);
            x += sep;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hoveredPrev) {
            this.playClick();
            this.previousPage();
            return true;
        }
        if (this.hoveredNext) {
            this.playClick();
            this.nextPage();
            return true;
        }
        if (this.hoveredPathSegment >= 0 && this.hoveredPathSegment < navigationStack.size() - 1) {
            this.playClick();
            this.navigateTo(this.hoveredPathSegment);
            return true;
        }
        if (this.hoveredGearIndex >= 0) {
            String sub;
            this.playClick();
            String value = this.currentProperties.getValueAt(this.hoveredGearIndex);
            if (value.startsWith("#") && this.renderGroups.containsKey(sub = value.substring(1))) {
                Minecraft.getInstance().setScreen((Screen)new ModelSettingsScreen(this.renderContext, this.animatableModel, this, sub));
                return true;
            }
        }
        if (this.hoveredIndex >= 0) {
            this.playClick();
            String key = this.currentProperties.getKeyAt(this.hoveredIndex);
            if ("#return".equals(key)) {
                this.navigateBack();
            } else if (key.startsWith("#")) {
                this.navigateToSubmenu(key);
            } else {
                this.playAnimation(key);
            }
            return true;
        }
        double cdx = mouseX - (double)this.centerX;
        double cdy = mouseY - (double)this.centerY;
        if (cdx * cdx + cdy * cdy <= 484.0) {
            if (this.animatableModel.getEntity() instanceof Player) {
                AnimationLockEvent.toggleLock();
            } else {
                NetworkHandler.sendToServer(C2SPlayAnimationPacket.createWithIndex(this.animatableModel.getEntity().getId()));
                this.onClose();
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void navigateTo(int targetIndex) {
        while (navigationStack.size() > targetIndex + 1) {
            navigationStack.removeLast();
        }
        Minecraft.getInstance().setScreen((Screen)new ModernAnimationRouletteScreen(lastModelId, this.renderContext, this.animatableModel));
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        if (delta < 0.0) {
            this.nextPage();
        } else {
            this.previousPage();
        }
        return true;
    }

    private void previousPage() {
        this.currentNavEntry.setValue(Math.max(0, this.page() - 1));
    }

    private void nextPage() {
        if ((this.page() + 1) * 8 < this.currentProperties.size()) {
            this.currentNavEntry.setValue(this.page() + 1);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyMappingFactory.isActiveAndMatches(AnimationRouletteKey.KEY_ROULETTE, keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void navigateToSubmenu(String value) {
        if (navigationStack.size() > 5) {
            LocalPlayer p = Minecraft.getInstance().player;
            if (p != null) {
                p.displayClientMessage((Component)Component.translatable((String)"gui.openysm.roulette.too_long"), false);
            }
            return;
        }
        String sub = value.substring(1);
        if (this.textProperties.get(sub) != null) {
            navigationStack.addLast(MutablePair.of(sub, 0));
            Minecraft.getInstance().setScreen((Screen)new ModernAnimationRouletteScreen(lastModelId, this.renderContext, this.animatableModel));
        }
    }

    private void navigateBack() {
        if (navigationStack.size() > 1) {
            navigationStack.removeLast();
            Minecraft.getInstance().setScreen((Screen)new ModernAnimationRouletteScreen(lastModelId, this.renderContext, this.animatableModel));
            return;
        }
        Minecraft.getInstance().setScreen(null);
    }

    private void playAnimation(String key) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (NetworkHandler.isClientConnected()) {
            Pair<String, Integer> last = navigationStack.peekLast();
            String submenu = last != null && StringUtils.isNotBlank((CharSequence)((CharSequence)last.getLeft())) ? (String)last.getLeft() : "";
            Object entity = this.animatableModel.getEntity();
            if (entity instanceof Player) {
                NetworkHandler.sendToServer(new C2SPlayAnimationPacket(this.hoveredIndex, submenu));
            } else if (entity instanceof net.minecraft.world.entity.Entity ysmEntity) {
                NetworkHandler.sendToServer(new C2SPlayAnimationPacket(this.hoveredIndex, submenu, ysmEntity.getId()));
            }
        } else if (player != null) {
            PlayerCapability.get((Player)player).ifPresent(cap -> cap.requestModelSwitch(key));
        }
        if (player != null && ((Boolean)GeneralConfig.PRINT_ANIMATION_ROULETTE_MSG.get()).booleanValue()) {
            player.displayClientMessage(Component.translatable("message.openysm.model.animation_roulette.play", key), false);
        }
        Minecraft.getInstance().setScreen(null);
    }

    private void playClick() {
        Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}
