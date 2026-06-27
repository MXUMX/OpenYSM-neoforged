/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  it.unimi.dsi.fastutil.objects.Object2ReferenceMap
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.gui;

import org.openysm.capability.PlayerCapability;
import org.openysm.client.entity.PlayerPreviewEntity;
import org.openysm.client.gui.PlayerModelScreen;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.client.renderer.RendererManager;
import org.openysm.geckolib3.core.builder.Animation;
import org.openysm.util.data.OrderedStringMap;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import rip.ysm.gui.OptionGroup;
import rip.ysm.gui.OptionRow;
import rip.ysm.gui.OptionScreen;
import rip.ysm.gui.components.AnimationRow;
import rip.ysm.gui.components.TextureGrid;
import rip.ysm.gui.components.buttons.FooterButton;
import rip.ysm.gui.components.buttons.IconButton;
import rip.ysm.gui.components.buttons.TabButton;
import rip.ysm.gui.components.groups.CategoryGroup;
import rip.ysm.gui.components.groups.TextureGroup;

public class ModernPlayerTextureScreen
extends OptionScreen {
    private static final ResourceLocation ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath("openysm", "texture/icon.png");
    private static final List<String> CATEGORY_ORDER = List.of("_textures", "main", "extra", "arm", "fp_arm", "tac", "carryon", "parcool", "swem", "slashblade", "tlm", "immersive_melodies", "irons_spell_books", "arrow");
    private static final List<String> MAIN_ANIMATIONS = List.of("idle", "walk", "run", "jump", "sneak", "sneaking", "swim", "swim_stand", "fly", "elytra_fly", "sit", "ride", "ride_pig", "boat");
    public final ModelAssembly renderContext;
    public final String modelId;
    public final OrderedStringMap<String, ? extends AbstractTexture> textureMap;
    private final PlayerPreviewEntity modelHolder;
    private final List<IconButton> icons = new ArrayList<IconButton>();
    private IconButton hoveredIcon;
    private EditBox searchBox;
    private String currentAnimation = "idle";
    private int previewLeft;
    private int previewTop;
    private int previewRight;
    private int previewBottom;
    private float yaw = 165.0f;
    private float pitch = -5.0f;
    private float zoom = 80.0f;
    private float offsetX = 0.0f;
    private float offsetY = -60.0f;
    private boolean showGround = true;
    private boolean draggingPreview;
    private int draggingButton = -1;

    public ModernPlayerTextureScreen(PlayerModelScreen parent, String modelId, ModelAssembly modelAssembly) {
        super((Component)Component.translatable((String)"gui.openysm.texture_screen.title"), parent);
        this.renderContext = modelAssembly;
        this.modelId = modelId;
        this.textureMap = modelAssembly.getAnimationBundle().getTextures();
        this.modelHolder = new PlayerPreviewEntity();
    }

    @Override
    protected int computePanelWidth() {
        return Math.min(this.width - 40, 780);
    }

    @Override
    protected int computePanelHeight() {
        return Math.min(this.height - 40, 380);
    }

    @Override
    protected boolean shouldUseCompactTabs() {
        return this.width < 640;
    }

    @Override
    protected int computeRowAreaRight() {
        return this.panelRight - this.previewWidth() - 6;
    }

    private int previewWidth() {
        return this.compactTabs ? 200 : 280;
    }

    @Override
    protected void registerGroups() {
        if (!this.textureMap.isEmpty()) {
            TextureGroup tg = new TextureGroup();
            tg.add(new TextureGrid(this));
            this.groups.add(tg);
        }
        Object2ReferenceMap<String, Animation> mainAnims = this.renderContext.getAnimationBundle().getMainAnimations();
        LinkedHashMap<String, List<String>> buckets = new LinkedHashMap<>();
        for (Map.Entry<String, Animation> e : mainAnims.entrySet()) {
            String name = e.getKey();
            if (name.startsWith("\u2014\u2014")) continue;
            String key = ModernPlayerTextureScreen.categoryForAnimation(name);
            buckets.computeIfAbsent(key, k -> new ArrayList<>()).add(name);
        }
        ArrayList<String> sortedCats = new ArrayList<>(buckets.keySet());
        sortedCats.sort((a, b) -> {
            int ia = CATEGORY_ORDER.indexOf(a);
            int ib = CATEGORY_ORDER.indexOf(b);
            if (ia < 0) {
                ia = Integer.MAX_VALUE;
            }
            if (ib < 0) {
                ib = Integer.MAX_VALUE;
            }
            if (ia != ib) {
                return Integer.compare(ia, ib);
            }
            return a.compareTo(b);
        });
        for (String cat : sortedCats) {
            CategoryGroup g = new CategoryGroup(cat);
            for (String name : buckets.get(cat)) {
                g.add(new AnimationRow(0, 0, 0, 18, name, this));
            }
            this.groups.add(g);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(this.applyBtn);
        this.addRenderableWidget(this.undoBtn);
        this.addRenderableWidget(this.cancelBtn);
        this.applyBtn.visible = false;
        this.undoBtn.visible = false;
        this.cancelBtn.visible = false;
        this.applyBtn.active = false;
        this.undoBtn.active = false;
        this.saveBtn.setMessage((Component)Component.translatable((String)"gui.openysm.config.done"));
        this.saveBtn.setX(this.panelRight - this.saveBtn.getWidth());
        this.previewLeft = this.panelRight - this.previewWidth();
        this.previewTop = this.rowAreaTop;
        this.previewRight = this.panelRight;
        this.previewBottom = this.panelBottom - 60;
        this.icons.clear();
        int iconY = this.panelTop;
        int iconX = this.panelRight - 18;
        this.icons.add(new IconButton(iconX, iconY, 18, 64, 16, () -> {
            this.currentAnimation = "idle";
        }, (Component)Component.translatable((String)"gui.openysm.model.stop")));
        this.icons.add(new IconButton(iconX -= 20, iconY, 18, 48, 16, this::resetView, (Component)Component.translatable((String)"gui.openysm.model.reset")));
        this.icons.add(new IconButton(iconX -= 20, iconY, 18, 64, 0, () -> {
            this.showGround = !this.showGround;
        }, (Component)Component.translatable((String)"gui.openysm.model.ground")));
        int searchW = Mth.clamp((int)(this.panelRight - this.panelLeft - 54 - 4 - 200), (int)80, (int)140);
        int searchX = iconX - 2 - searchW;
        String oldQuery = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.font, searchX, iconY, searchW, 18, (Component)Component.translatable((String)"gui.openysm.search.placeholder"));
        this.searchBox.setTextColor(0xFFFFFF);
        this.searchBox.setHint((Component)Component.translatable((String)"gui.openysm.search.placeholder"));
        this.searchBox.setMaxLength(64);
        this.searchBox.setValue(oldQuery);
        this.searchBox.setResponder(s -> this.applySearchFilter());
        this.addRenderableWidget(this.searchBox);
    }

    @Override
    protected void selectGroup(OptionGroup group) {
        super.selectGroup(group);
        this.applySearchFilter();
    }

    private void applySearchFilter() {
        if (this.activeGroup == null) {
            return;
        }
        String s = this.searchBox != null ? this.searchBox.getValue().toLowerCase().trim() : "";
        for (OptionRow r : this.activeRows) {
            r.closeOverlay();
        }
        this.activeRows.clear();
        int rowY = this.rowAreaTop;
        int rowW = this.rowAreaRight - this.rowAreaLeft;
        for (OptionRow<?> template : this.activeGroup.getRows()) {
            AnimationRow ar;
            if (!s.isEmpty() && template instanceof AnimationRow && !(ar = (AnimationRow)template).matches(s)) continue;
            template.setX(this.rowAreaLeft);
            template.setY(rowY);
            template.setWidth(rowW);
            this.activeRows.add(template);
            rowY += template.getHeight() + 2;
        }
        this.rowContentHeight = rowY - this.rowAreaTop;
        this.maxRowScroll = Math.max(0, this.rowContentHeight - (this.rowAreaBottom - this.rowAreaTop));
        this.rowScrollOffset = Math.min(this.rowScrollOffset, this.maxRowScroll);
        this.rowScrollDisplay = Math.min(this.rowScrollDisplay, (float)this.maxRowScroll);
    }

    private void resetView() {
        this.offsetX = 0.0f;
        this.offsetY = -60.0f;
        this.zoom = 80.0f;
        this.yaw = 165.0f;
        this.pitch = -5.0f;
    }

    public void selectAnimation(String name) {
        this.currentAnimation = name;
        if (!this.modelHolder.getAnimationStateMachine().isCurrentAnimation(name)) {
            this.modelHolder.getAnimationStateMachine().setCurrentAnimation(name);
        }
    }

    public String currentAnimation() {
        return this.currentAnimation;
    }

    private static String categoryForAnimation(String name) {
        if (name == null || name.isEmpty()) {
            return "misc";
        }
        String normalized = name.toLowerCase();
        if (MAIN_ANIMATIONS.contains(normalized)) {
            return "main";
        }
        if (normalized.startsWith("extra")) {
            return "extra";
        }
        if (normalized.startsWith("hold_mainhand") || normalized.startsWith("hold_offhand") || normalized.startsWith("mainhand") || normalized.startsWith("offhand") || normalized.startsWith("swing")) {
            return "arm";
        }
        if (normalized.startsWith("fp_") || normalized.startsWith("first_person")) {
            return "fp_arm";
        }
        if (normalized.startsWith("tac") || normalized.startsWith("tacz")) {
            return "tac";
        }
        if (normalized.startsWith("carryon")) {
            return "carryon";
        }
        if (normalized.startsWith("parcool")) {
            return "parcool";
        }
        if (normalized.startsWith("swem")) {
            return "swem";
        }
        if (normalized.startsWith("slashblade") || normalized.startsWith("slash_blade")) {
            return "slashblade";
        }
        if (normalized.startsWith("tlm") || normalized.startsWith("maid") || normalized.startsWith("touhou")) {
            return "tlm";
        }
        if (normalized.startsWith("im_") || normalized.startsWith("immersive_melodies")) {
            return "immersive_melodies";
        }
        if (normalized.startsWith("iss") || normalized.startsWith("irons_spell_books")) {
            return "irons_spell_books";
        }
        if (normalized.startsWith("arrow")) {
            return "arrow";
        }
        int separator = normalized.indexOf('.');
        if (separator < 0) {
            separator = normalized.indexOf(':');
        }
        if (separator > 0) {
            String prefix = normalized.substring(0, separator);
            if (CATEGORY_ORDER.contains(prefix)) {
                return prefix;
            }
        }
        return "misc";
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parentScreen);
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.hoveredIcon = null;
        for (IconButton btn : this.icons) {
            if (!btn.contains(mouseX, mouseY)) continue;
            this.hoveredIcon = btn;
            break;
        }
        super.render(g, mouseX, mouseY, partialTick);
        for (IconButton btn : this.icons) {
            this.drawIcon(g, btn);
        }
    }

    private void drawIcon(GuiGraphics g, IconButton btn) {
        boolean hover = btn == this.hoveredIcon;
        int bg = hover ? -1877534953 : -1879048192;
        g.fill(btn.x, btn.y, btn.x + btn.size, btn.y + btn.size, bg);
        int ix = btn.x + (btn.size - 16) / 2;
        int iy = btn.y + (btn.size - 16) / 2;
        g.blit(ICON_TEXTURE, ix, iy, 16, 16, (float)btn.u, (float)btn.v, 16, 16, 256, 256);
    }

    @Override
    protected void collectBlurRegions(List<int[]> out) {
        out.add(new int[]{this.panelLeft, this.panelTop, this.panelRight - this.panelLeft, 18});
        int tabScroll = Math.round(this.tabScrollDisplay);
        for (TabButton tb2 : this.tabButtons) {
            if (this.compactTabs) {
                int x = tb2.getX() - tabScroll;
                int xRight = x + tb2.getWidth();
                int left = Math.max(x, this.tabAreaLeft);
                int right = Math.min(xRight, this.tabAreaRight);
                if (right <= left) continue;
                out.add(new int[]{left, tb2.getY(), right - left, tb2.getHeight()});
                continue;
            }
            int y = tb2.getY() - tabScroll;
            int yBot = y + tb2.getHeight();
            int top = Math.max(y, this.tabAreaTop);
            int bot = Math.min(yBot, this.tabAreaBottom);
            if (bot <= top) continue;
            out.add(new int[]{tb2.getX(), top, tb2.getWidth(), bot - top});
        }
        if (this.activeGroup instanceof TextureGroup && !this.activeRows.isEmpty() && this.activeRows.get(0) instanceof TextureGrid grid) {
            grid.collectBlurRegions(out, Math.round(this.rowScrollDisplay), this.rowAreaTop, this.rowAreaBottom);
        } else {
            int rowScroll = Math.round(this.rowScrollDisplay);
            for (OptionRow row : this.activeRows) {
                int y = row.getY() - rowScroll;
                int yBot = y + row.getHeight();
                if (yBot <= this.rowAreaTop || y >= this.rowAreaBottom) continue;
                int top = Math.max(y, this.rowAreaTop);
                int bot = Math.min(yBot, this.rowAreaBottom);
                out.add(new int[]{row.getX(), top, row.getWidth(), bot - top});
            }
        }
        out.add(new int[]{this.previewLeft, this.previewTop, this.previewRight - this.previewLeft, this.previewBottom - this.previewTop});
        ModernPlayerTextureScreen.addFooterRect(out, this.applyBtn);
        ModernPlayerTextureScreen.addFooterRect(out, this.undoBtn);
        ModernPlayerTextureScreen.addFooterRect(out, this.saveBtn);
        ModernPlayerTextureScreen.addFooterRect(out, this.cancelBtn);
        for (IconButton btn : this.icons) {
            out.add(new int[]{btn.x, btn.y, btn.size, btn.size});
        }
        if (this.searchBox != null && this.searchBox.visible) {
            out.add(new int[]{this.searchBox.getX(), this.searchBox.getY(), this.searchBox.getWidth(), this.searchBox.getHeight()});
        }
        if (this.hoveredIcon != null || this.hoveredRow instanceof AnimationRow) {
            int descY = this.panelBottom - 32;
            out.add(new int[]{this.panelLeft, descY, this.panelRight - this.panelLeft, 28});
        }
    }

    private static void addFooterRect(List<int[]> out, FooterButton btn) {
        if (btn == null || !btn.visible) {
            return;
        }
        out.add(new int[]{btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight()});
    }

    @Override
    protected void renderExtras(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(this.previewLeft, this.previewTop, this.previewRight, this.previewBottom, 0x66000000);
        this.renderPreview(g, partialTick);
    }

    @Override
    protected void renderDescription(GuiGraphics g, int descY) {
        if (this.hoveredIcon != null) {
            g.fill(this.panelLeft, descY, this.panelRight, descY + 28, Integer.MIN_VALUE);
            g.drawString(this.font, this.hoveredIcon.tooltip, this.panelLeft + 6, descY + 10, -1, false);
            return;
        }
        OptionRow optionRow = this.hoveredRow;
        if (optionRow instanceof AnimationRow) {
            AnimationRow row = (AnimationRow)optionRow;
            g.fill(this.panelLeft, descY, this.panelRight, descY + 28, Integer.MIN_VALUE);
            g.drawString(this.font, row.getMessage(), this.panelLeft + 6, descY + 4, -1, false);
            g.drawString(this.font, (Component)Component.literal((String)row.animKey).withStyle(ChatFormatting.GRAY), this.panelLeft + 6, descY + 16, -5592406, false);
        }
    }

    private void renderPreview(GuiGraphics g, float partialTick) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        if (!this.modelHolder.getAnimationStateMachine().isCurrentAnimation(this.currentAnimation)) {
            this.modelHolder.getAnimationStateMachine().setCurrentAnimation(this.currentAnimation);
        }
        double scale = this.minecraft.getWindow().getGuiScale();
        int sx = (int)((double)this.previewLeft * scale);
        int sy = (int)((double)this.minecraft.getWindow().getHeight() - (double)this.previewBottom * scale);
        int sw = (int)((double)(this.previewRight - this.previewLeft) * scale);
        int sh = (int)((double)(this.previewBottom - this.previewTop) * scale);
        RenderSystem.enableScissor((int)sx, (int)sy, (int)sw, (int)sh);
        PlayerCapability.get((Player)this.minecraft.player).ifPresent(cap -> {
            this.modelHolder.initModelWithTexture(this.modelId, cap.getCurrentTextureName());
            float cx = (float)(this.previewLeft + this.previewRight) / 2.0f + this.offsetX;
            float cy = (float)this.previewTop + (float)(this.previewBottom - this.previewTop) * 0.65f + this.offsetY;
            float frameTime = this.minecraft.getTimer().getGameTimeDeltaPartialTick(false);
            ModelPreviewRenderer.renderEntityPreview(cx, cy, this.zoom, this.pitch, this.yaw, frameTime, this.modelHolder, RendererManager.getPlayerRenderer(), this.showGround);
        });
        RenderSystem.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (IconButton btn : this.icons) {
                if (!btn.contains(mouseX, mouseY)) continue;
                btn.onPress.run();
                return true;
            }
        }
        if (this.isInPreview(mouseX, mouseY)) {
            this.draggingPreview = true;
            this.draggingButton = button;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.draggingPreview && button == this.draggingButton) {
            this.draggingPreview = false;
            this.draggingButton = -1;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.draggingPreview && button == this.draggingButton) {
            if (button == 0) {
                this.yaw = (float)((double)this.yaw + dragX * 1.2);
                this.pitch = Mth.clamp((float)((float)((double)this.pitch - dragY * 0.8)), (float)-90.0f, (float)90.0f);
            } else if (button == 1) {
                this.offsetX = (float)((double)this.offsetX + dragX);
                this.offsetY = (float)((double)this.offsetY + dragY);
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        if (this.isInPreview(mouseX, mouseY)) {
            this.zoom = Mth.clamp((float)((float)((double)this.zoom * (1.0 + delta * 0.1))), (float)18.0f, (float)360.0f);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, 0.0, delta);
    }

    private boolean isInPreview(double mouseX, double mouseY) {
        return mouseX >= (double)this.previewLeft && mouseX < (double)this.previewRight && mouseY >= (double)this.previewTop && mouseY < (double)this.previewBottom;
    }
}
