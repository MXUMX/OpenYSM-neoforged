package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.geckolib3.geo.render.built.GeoModel;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.RendererManager;
import org.openysm.config.GeneralConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class ReplacePlayerHandRenderEvent {
    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent event) {
        if (!OpenYSM.isAvailable() || GeneralConfig.DISABLE_SELF_MODEL.get() || GeneralConfig.DISABLE_SELF_HANDS.get()) {
            return;
        }
        Player player = event.getPlayer();
        if (!(player instanceof LocalPlayer localPlayer)) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(localPlayer, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            if (!cap.isModelActive()) {
                return;
            }
            HumanoidArm arm = event.getArm();
            ModelAssembly context = cap.getModelAssembly();
            if (context == null || !hasArmBone(arm, context.getAnimationBundle().getArmModel())) {
                return;
            }
            RendererManager.getHandRenderer().renderHandItem(localPlayer, context, cap, arm, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
            event.setCanceled(true);
        });
    }

    private static boolean hasArmBone(HumanoidArm humanoidArm, GeoModel meshData) {
        if (humanoidArm == HumanoidArm.LEFT) {
            return meshData.hasCustomLeftHand;
        }
        return meshData.hasCustomRightHand;
    }
}