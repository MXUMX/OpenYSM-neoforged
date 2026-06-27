package org.openysm.geckolib3.geo;

import org.openysm.geckolib3.core.AnimatableEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class GeoLayerRenderer<T extends AnimatableEntity<?>> {
    public abstract void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch);
}