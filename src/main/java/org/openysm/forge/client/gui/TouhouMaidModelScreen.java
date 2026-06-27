/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.world.entity.LivingEntity
 *  org.apache.commons.lang3.StringUtils
 */
package org.openysm.client.gui;

import org.openysm.client.ClientModelManager;
import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.client.entity.PlayerPreviewEntity;
import org.openysm.client.gui.ModelInfoScreen;
import org.openysm.client.gui.ModelMetadataPresenter;
import org.openysm.client.gui.PlayerModelScreen;
import org.openysm.client.gui.PlayerTextureScreen;
import org.openysm.client.gui.button.ModelButton;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.client.gui.TouhouMaidTextureScreen;
import org.openysm.client.gui.button.TouhouMaidModelButton;
import org.openysm.resource.models.Metadata;
import org.openysm.util.FileTypeUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.StringUtils;

public class TouhouMaidModelScreen
extends PlayerModelScreen {
    private final EntityMaid maid;

    public TouhouMaidModelScreen(EntityMaid entityMaid) {
        this.maid = entityMaid;
    }

    @Override
    public ModelButton createModelButton(int x, int y, boolean isAuthLocked, PlayerPreviewEntity previewEntity, ModelAssembly modelAssembly) {
        return new TouhouMaidModelButton(x, y, isAuthLocked, previewEntity, modelAssembly, this.maid);
    }

    @Override
    public Screen createTextureScreen(PlayerModelScreen modelScreen, String str, ModelAssembly modelAssembly) {
        return new TouhouMaidTextureScreen(modelScreen, str, Objects.requireNonNullElse(org.openysm.capability.YSMCapabilities.get(this.maid, MaidCapabilityProvider.MAID_CAP).map(v0 -> v0.getModelAssembly()).orElse(null), modelAssembly), this.maid);
    }

    @Override
    public Screen createModelInfoScreen(PlayerModelScreen modelScreen, ModelAssembly modelAssembly) {
        return new ModelInfoScreen(modelScreen, Objects.requireNonNullElse(org.openysm.capability.YSMCapabilities.get(this.maid, MaidCapabilityProvider.MAID_CAP).map(v0 -> v0.getModelAssembly()).orElse(null), modelAssembly));
    }

    @Override
    public void renderModelPreview(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        RenderSystem.enableScissor((int)((int)((double)(this.guiLeft + 5) * guiScale)), (int)((int)((double)Minecraft.getInstance().getWindow().getHeight() - (double)(this.guiTop + 200) * guiScale)), (int)((int)(125.0 * guiScale)), (int)((int)(171.0 * guiScale)));
        ModelPreviewRenderer.setPreviewMode(true);
        try {
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, this.guiLeft + 5, this.guiTop + 19, this.guiLeft + 130, this.guiTop + 190, 70, 0.0625f, (float)mouseX, (float)mouseY, (LivingEntity)this.maid);
        } finally {
            ModelPreviewRenderer.setPreviewMode(false);
            RenderSystem.disableScissor();
        }
        org.openysm.capability.YSMCapabilities.get(this.maid, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
            List<FormattedCharSequence> listSplit = this.font.split(FormattedText.of((String)ClientModelManager.getModelContext(cap.getModelId()).map(it -> {
                Metadata metadata2 = it.getModelData().getExtraInfo();
                if (metadata2 != null) {
                    return ModelMetadataPresenter.getLocalizedModelString(it, "metadata.name", metadata2.getName());
                }
                return "";
            }).filter(charSequence -> StringUtils.isNoneBlank((CharSequence[])new CharSequence[]{charSequence})).orElse(FileTypeUtil.getNameWithoutArchiveExtension(cap.getModelId()))), 125);
            int lineY = this.guiTop + 205;
            for (FormattedCharSequence formattedCharSequence : listSplit) {
                guiGraphics.drawString(this.font, formattedCharSequence, this.guiLeft + (135 - this.font.width(formattedCharSequence)) / 2, lineY, 15986656);
                lineY += 10;
            }
        });
    }
}
