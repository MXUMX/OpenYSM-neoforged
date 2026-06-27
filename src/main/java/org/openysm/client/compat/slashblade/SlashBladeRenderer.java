package org.openysm.client.compat.slashblade;

import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlashBladeRenderer {

    public static void renderBladeOnly(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        try {
            SlashBladeBridge.renderBladeOnly(poseStack, bufferSource, packedLight, stack);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public static void renderOnEntity(LivingEntity entity, AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, float partialTick) {
        SlashBladeBridge.renderOnEntity(entity, model, poseStack, bufferSource, packedLight, stack, partialTick);
    }

    public static void renderRightWaist(AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        SlashBladeBridge.renderRightWaist(model, poseStack, bufferSource, packedLight, stack);
    }
}
