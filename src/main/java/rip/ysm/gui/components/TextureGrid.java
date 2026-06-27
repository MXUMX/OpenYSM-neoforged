/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.gui.components;

import org.openysm.capability.PlayerCapability;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.entity.PlayerPreviewEntity;
import org.openysm.client.gui.ModelMetadataPresenter;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.client.renderer.RendererManager;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SRequestSwitchModelPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import rip.ysm.gui.ModernPlayerTextureScreen;
import rip.ysm.gui.OptionRow;

public final class TextureGrid
extends OptionRow<Object> {
    private static final int TEX_BTN_W = 54;
    private static final int TEX_BTN_H = 102;
    private static final int TEX_GAP = 4;
    private final ModernPlayerTextureScreen owner;
    private final List<String> textureNames;
    private final PlayerPreviewEntity[] holders;

    public TextureGrid(ModernPlayerTextureScreen owner) {
        super(0, 0, 0, 0, null);
        int i;
        this.owner = owner;
        this.textureNames = new ArrayList<String>(owner.textureMap.size());
        for (i = 0; i < owner.textureMap.size(); ++i) {
            this.textureNames.add(owner.textureMap.getKeyAt(i));
        }
        this.holders = new PlayerPreviewEntity[this.textureNames.size()];
        for (i = 0; i < this.holders.length; ++i) {
            this.holders[i] = new PlayerPreviewEntity();
            this.holders[i].resetModel();
            this.holders[i].getAnimationStateMachine().setCurrentAnimation("idle");
            this.holders[i].initModelWithTexture(owner.modelId, this.textureNames.get(i));
        }
    }

    private int cols() {
        return Math.max(1, (this.getWidth() + 4) / 58);
    }

    private int rows() {
        return (this.textureNames.size() + this.cols() - 1) / this.cols();
    }

    @Override
    public void setWidth(int w) {
        super.setWidth(w);
        this.setHeight(this.rows() * 106 - 4);
    }

    public void collectBlurRegions(List<int[]> out, int rowScroll, int areaTop, int areaBottom) {
        int c = this.cols();
        int slotW = 58;
        int slotH = 106;
        for (int i = 0; i < this.textureNames.size(); ++i) {
            int col = i % c;
            int row = i / c;
            int x = this.getX() + col * slotW;
            int y = this.getY() + row * slotH - rowScroll;
            int yBot = y + 102;
            if (yBot <= areaTop || y >= areaBottom) continue;
            int top = Math.max(y, areaTop);
            int bot = Math.min(yBot, areaBottom);
            out.add(new int[]{x, top, 54, bot - top});
        }
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int c = this.cols();
        int slotW = 58;
        int slotH = 106;
        for (int i = 0; i < this.textureNames.size(); ++i) {
            int col = i % c;
            int row = i / c;
            int x = this.getX() + col * slotW;
            int y = this.getY() + row * slotH;
            this.renderSlot(g, x, y, i, mouseX, mouseY, partialTick);
        }
    }

    private void renderSlot(GuiGraphics g, int x, int y, int idx, int mx, int my, float pt) {
        boolean hover;
        String name = this.textureNames.get(idx);
        PlayerPreviewEntity holder = this.holders[idx];
        String currentTex = this.currentTextureName();
        boolean selected = name.equals(currentTex);
        boolean bl = hover = mx >= x && mx < x + 54 && my >= y && my < y + 102;
        int bg = selected ? -1875692749 : (hover ? -1877534953 : -1879048192);
        g.fill(x, y, x + 54, y + 102, bg);
        this.renderHolderPreview(x, y, holder, pt);
        MutableComponent label = Component.literal((String)ModelMetadataPresenter.getLocalizedModelString(this.owner.renderContext, "files.player.texture.%s".formatted(name), name));
        int textY = y + 102 - 12;
        int tw = Minecraft.getInstance().font.width((FormattedText)label);
        g.drawString(Minecraft.getInstance().font, (Component)label, x + (54 - tw) / 2, textY, -1, true);
        if (selected || hover) {
            int border = selected ? -1 : -5592406;
            g.fill(x, y, x + 54, y + 1, border);
            g.fill(x, y + 102 - 1, x + 54, y + 102, border);
            g.fill(x, y, x + 1, y + 102, border);
            g.fill(x + 54 - 1, y, x + 54, y + 102, border);
        }
    }

    private String currentTextureName() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return "";
        }
        return PlayerCapability.get((Player)mc.player).map(LivingAnimatable::getCurrentTextureName).orElse("");
    }

    private void renderHolderPreview(int x, int y, PlayerPreviewEntity holder, float pt) {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        int previewH = 82;
        int sx = (int)((double)x * scale);
        int sy = (int)((double)mc.getWindow().getHeight() - (double)(y + previewH) * scale);
        int sw = (int)(54.0 * scale);
        int sh = (int)((double)previewH * scale);
        RenderSystem.enableScissor((int)sx, (int)sy, (int)sw, (int)sh);
        float frameTime = mc.getTimer().getGameTimeDeltaPartialTick(false);
        ModelPreviewRenderer.renderLivingEntityPreview((float)x + 27.0f, (float)y + 51.0f + 24.0f, 35.0f, frameTime, holder, RendererManager.getPlayerRenderer(), false, true);
        RenderSystem.disableScissor();
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    public void onClick(double mouseX, double mouseY) {
        int c = this.cols();
        int slotW = 58;
        int slotH = 106;
        int col = (int)((mouseX - (double)this.getX()) / (double)slotW);
        int row = (int)((mouseY - (double)this.getY()) / (double)slotH);
        if (col < 0 || col >= c) {
            return;
        }
        int idx = row * c + col;
        if (idx < 0 || idx >= this.textureNames.size()) {
            return;
        }
        double localX = mouseX - (double)this.getX() - (double)(col * slotW);
        double localY = mouseY - (double)this.getY() - (double)(row * slotH);
        if (localX >= 54.0 || localY >= 102.0) {
            return;
        }
        String name = this.textureNames.get(idx);
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        PlayerCapability.get((Player)mc.player).ifPresent(cap -> {
            cap.initModelWithTexture(this.owner.modelId, name);
            NetworkHandler.sendToServer(new C2SRequestSwitchModelPacket(this.owner.modelId, name));
        });
    }
}
