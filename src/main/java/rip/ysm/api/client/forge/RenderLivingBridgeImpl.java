/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.bus.api.Event
 */
package rip.ysm.api.client.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class RenderLivingBridgeImpl {
    private RenderLivingBridgeImpl() {
    }

    public static boolean firePre(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return NeoForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entity, renderer, partialTick, poseStack, bufferSource, packedLight)).isCanceled();
    }

    public static void firePost(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        NeoForge.EVENT_BUS.post(new RenderLivingEvent.Post(entity, renderer, partialTick, poseStack, bufferSource, packedLight));
    }
}
