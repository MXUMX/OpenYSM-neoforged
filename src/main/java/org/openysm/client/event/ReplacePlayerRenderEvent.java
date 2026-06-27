package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.client.compat.firstperson.FirstPersonCompat;
import org.openysm.client.compat.playeranimator.PlayerAnimatorCompat;
import org.openysm.client.compat.realcamera.RealCameraCompat;
import org.openysm.client.renderer.RendererManager;
import org.openysm.config.GeneralConfig;
import org.openysm.util.CameraUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class ReplacePlayerRenderEvent {
    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        Player entity = event.getEntity();
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (entity.equals(localPlayer) && GeneralConfig.DISABLE_SELF_MODEL.get().booleanValue()) {
            return;
        }
        if ((!entity.equals(localPlayer) && GeneralConfig.DISABLE_OTHER_MODEL.get().booleanValue()) || event.getEntity().isSpectator()) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(entity, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            if (cap.isModelActive()) {
                if (!CameraUtil.isFirstPerson(cap) || FirstPersonCompat.isFirstPersonActive() || RealCameraCompat.isActive() || GeneralConfig.DISABLE_EXTERNAL_FP_ANIM.get().booleanValue() || !PlayerAnimatorCompat.isPlayerAnimated(localPlayer)) {
                    event.setCanceled(true);
                    RendererManager.getPlayerRenderer().render(event.getEntity(), event.getEntity().getYRot(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
                }
            }
        });
    }
}