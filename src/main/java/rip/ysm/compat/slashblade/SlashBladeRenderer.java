/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.slashblade;

import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.compat.slashblade.forge.SlashBladeRendererImpl;

public final class SlashBladeRenderer {
    private SlashBladeRenderer() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void renderOnEntity(LivingEntity entity, AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, float partialTick) {
        SlashBladeRendererImpl.renderOnEntity(entity, model, poseStack, bufferSource, packedLight, stack, partialTick);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void renderRightWaist(AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        SlashBladeRendererImpl.renderRightWaist(model, poseStack, bufferSource, packedLight, stack);
    }
}
