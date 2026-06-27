/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.slashblade.forge;

import org.openysm.client.compat.slashblade.SlashBladeRenderer;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class SlashBladeRendererImpl {
    private SlashBladeRendererImpl() {
    }

    public static void renderOnEntity(LivingEntity entity, AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, float partialTick) {
        SlashBladeRenderer.renderOnEntity(entity, model, poseStack, bufferSource, packedLight, stack, partialTick);
    }

    public static void renderRightWaist(AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        SlashBladeRenderer.renderRightWaist(model, poseStack, bufferSource, packedLight, stack);
    }
}

