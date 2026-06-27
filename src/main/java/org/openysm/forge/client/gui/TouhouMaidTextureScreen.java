/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 */
package org.openysm.client.gui;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.client.entity.PlayerPreviewEntity;
import org.openysm.client.gui.PlayerModelScreen;
import org.openysm.client.gui.PlayerTextureScreen;
import org.openysm.client.gui.button.TextureButton;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.client.renderer.RendererManager;
import org.openysm.client.gui.button.TouhouMaidTextureButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

public class TouhouMaidTextureScreen
extends PlayerTextureScreen {
    private final EntityMaid maid;

    public TouhouMaidTextureScreen(PlayerModelScreen modelScreen, String str, ModelAssembly modelAssembly, EntityMaid entityMaid) {
        super(modelScreen, str, modelAssembly);
        this.maid = entityMaid;
    }

    @Override
    public TextureButton createTextureButton(int x, int y, PlayerPreviewEntity previewEntity, int textureIndex) {
        return new TouhouMaidTextureButton(x, y, previewEntity, this.maid, textureIndex, this.renderContext);
    }

    @Override
    public void renderTexturePreview(GuiGraphics guiGraphics, int scissorX, int scissorY, int scissorWidth, int scissorHeight, float partialTick) {
        RenderSystem.enableScissor((int)scissorX, (int)scissorY, (int)scissorWidth, (int)scissorHeight);
        org.openysm.capability.YSMCapabilities.get(this.maid, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
            this.modelHolder.initModelWithTexture(cap.getModelId(), cap.getCurrentTextureName());
            ModelPreviewRenderer.renderEntityPreview((float)this.guiLeft + 149.5f + 40.0f + this.offsetX, (float)this.guiTop + 117.5f + 80.0f + this.offsetY, this.zoom, this.pitch, this.yaw, partialTick, this.modelHolder, RendererManager.getPlayerRenderer(), this.showGround);
        });
        RenderSystem.disableScissor();
    }
}

