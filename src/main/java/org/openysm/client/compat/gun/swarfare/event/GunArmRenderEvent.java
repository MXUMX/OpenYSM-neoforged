package org.openysm.client.compat.gun.swarfare.event;

import com.atsuishio.superbwarfare.api.event.RenderPlayerArmEvent;
import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.geckolib3.geo.render.built.GeoModel;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.HandItemRenderer;
import org.openysm.client.renderer.RendererManager;
import org.openysm.config.GeneralConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import software.bernie.geckolib.cache.object.GeoBone;

public class GunArmRenderEvent {
    @SubscribeEvent
    public void onRenderPlayerArm(RenderPlayerArmEvent event) {
        LocalPlayer player;
        if (!OpenYSM.isAvailable() || GeneralConfig.DISABLE_SELF_MODEL.get() || GeneralConfig.DISABLE_SELF_HANDS.get() || (player = event.getLocalPlayer()) == null) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(player, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            HumanoidArm arm = event.getArm();
            ModelAssembly modelAssembly = cap.getModelAssembly();
            if (modelAssembly == null || !hasCustomArmModel(arm, modelAssembly.getAnimationBundle().getArmModel())) {
                return;
            }
            PoseStack stack = event.getStack();
            boolean zIsUseOldHandRender = event.isUseOldHandRender();
            GeoBone bone = event.getBone();
            MultiBufferSource currentBuffer = event.getCurrentBuffer();
            float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
            HandItemRenderer renderer = RendererManager.getHandRenderer();
            if (arm == HumanoidArm.LEFT) {
                stack.translate(-0.0625f, 0.125f, 0.0f);
                stack.translate(-0.275d, 0.0625d, 0.0d);
            } else {
                stack.translate(0.0625f, 0.125f, 0.0f);
                stack.translate(0.275d, 0.0625d, 0.0d);
            }
            if (zIsUseOldHandRender) {
                stack.translate((bone.getPivotX() - 1.0f) / 16.0f, (bone.getPivotY() - 2.0f) / 16.0f, bone.getPivotZ() / 16.0f);
            } else {
                stack.translate(bone.getPivotX() / 16.0f, (bone.getPivotY() + 7.0f) / 16.0f, bone.getPivotZ() / 16.0f);
                stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0f));
                stack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0f));
            }
            renderer.renderHandItem(player, modelAssembly, cap, arm, stack, currentBuffer, event.getPackedLightIn(), partialTick);
            if (event instanceof ICancellableEvent cancellableEvent) {
                cancellableEvent.setCanceled(true);
            }
        });
    }

    private boolean hasCustomArmModel(HumanoidArm arm, GeoModel model) {
        if (arm == HumanoidArm.LEFT) {
            return model.hasCustomLeftHand;
        }
        return model.hasCustomRightHand;
    }
}
