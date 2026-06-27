package org.openysm.client.event;

import org.openysm.client.entity.EntityRenderCache;
import org.openysm.geckolib3.geo.NativeModelRenderer;
import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.CustomEntityTranslucentRenderType;
import org.openysm.client.renderer.CustomPlayerRenderer;
import org.openysm.event.api.SpecialPlayerRenderEvent;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.client.renderer.RendererManager;
import org.openysm.config.GeneralConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
public class RenderFirstPlayerBackground {
    // 因为RenderHandEvent可有几率会渲染多次，所以为了避免多次渲染，这样设计
    private static boolean currentFrameRendered = false;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            currentFrameRendered = false;
            ModelPreviewRenderer.setFirstPersonMode(true);
            EntityRenderCache.tick(event.getPartialTick().getGameTimeDeltaPartialTick(false));
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            EntityRenderCache.clear();
            ModelPreviewRenderer.setFirstPersonMode(false);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        if (GeneralConfig.DISABLE_SELF_MODEL.get()) {
            return;
        }
        if (GeneralConfig.DISABLE_SELF_HANDS.get()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || currentFrameRendered) {
            return;
        }
        currentFrameRendered = true;
        org.openysm.capability.YSMCapabilities.get(player, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            if (!cap.isModelActive()) {
                return;
            }
            String modelId = cap.getModelId();
            ModelAssembly modelAssembly = cap.getModelAssembly();
            if (modelAssembly == null || !modelAssembly.getAnimationBundle().getArmModel().hasCustomLimbs) {
                return;
            }
            CustomPlayerRenderer instance = RendererManager.getPlayerRenderer();
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource multiBufferSource = event.getMultiBufferSource();
            if (NeoForge.EVENT_BUS.post(new SpecialPlayerRenderEvent(player, cap,  modelId)).isCanceled()) {
                return;
            }
            ResourceLocation resourceLocationB_ = cap.getTextureLocation();
            int textureIndex = cap.getTextureIndex();
            VertexConsumer buffer = multiBufferSource.getBuffer(CustomEntityTranslucentRenderType.get(resourceLocationB_));
            if (instance != null) {
                poseStack.pushPose();
                if (Minecraft.getInstance().options.bobView().get()) {
                    applyHandTransform(poseStack, event.getPartialTick(), player);
                }
                poseStack.translate(0.0d, -1.5d, 0.0d);
                NativeModelRenderer.renderMesh(buffer, poseStack.last(), modelAssembly.getAnimationBundle().getArmModel(), modelAssembly.getAnimationBundle().getArmModel().getBoneTransformData(), null, textureIndex, 3, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                poseStack.popPose();
            }
        });
    }

    private static void applyHandTransform(PoseStack poseStack, float partialTick, Player player) {
        float walkPhase = -(player.walkDist + ((player.walkDist - player.walkDistO) * partialTick));
        float fLerp = Mth.lerp(partialTick, player.oBob, player.bob);
        poseStack.translate((-Mth.sin(walkPhase * 3.1415927f)) * fLerp * 0.5f, Math.abs(Mth.cos(walkPhase * 3.1415927f) * fLerp), 0.0d);
        poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(Mth.sin(walkPhase * 3.1415927f) * fLerp * 3.0f));
        poseStack.mulPose(com.mojang.math.Axis.XN.rotationDegrees(Math.abs(Mth.cos((walkPhase * 3.1415927f) - 0.2f) * fLerp) * 5.0f));
    }
}
