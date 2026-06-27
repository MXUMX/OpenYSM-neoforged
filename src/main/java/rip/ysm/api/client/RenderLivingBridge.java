/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import rip.ysm.api.client.forge.RenderLivingBridgeImpl;

public final class RenderLivingBridge {
    private RenderLivingBridge() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean firePre(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return RenderLivingBridgeImpl.firePre(entity, renderer, partialTick, poseStack, bufferSource, packedLight);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void firePost(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        RenderLivingBridgeImpl.firePost(entity, renderer, partialTick, poseStack, bufferSource, packedLight);
    }
}
