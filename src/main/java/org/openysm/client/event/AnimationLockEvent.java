package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.client.input.AnimationRouletteKey;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SPlayAnimationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class AnimationLockEvent {

    private static boolean animationLocked = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OpenYSM.isAvailable() && event.getAction() == 1 && AnimationRouletteKey.KEY_LOCK.matches(event.getKey(), event.getScanCode())) {
            animationLocked = !animationLocked;
        }
    }

    @SubscribeEvent
    public static void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        LocalPlayer localPlayer;
        if (OpenYSM.isAvailable() && !animationLocked && (localPlayer = Minecraft.getInstance().player) != null && isPlayerMoving(localPlayer)) {
            org.openysm.capability.YSMCapabilities.get(localPlayer, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
                if (cap.isModelSwitching()) {
                    cap.clearModelSwitch();
                    if (NetworkHandler.isClientConnected()) {
                        NetworkHandler.sendToServer(C2SPlayAnimationPacket.createDefault());
                    }
                }
            });
        }
    }

    public static boolean isPlayerMoving(LocalPlayer localPlayer) {
        Input input = localPlayer.input;
        return input != null && (isSignificantImpulse(input.leftImpulse) || isSignificantImpulse(input.forwardImpulse) || input.jumping || input.shiftKeyDown);
    }

    private static boolean isSignificantImpulse(float impulse) {
        return Math.abs(impulse) > 1.0E-5f;
    }

    public static void toggleLock() {
        animationLocked = !animationLocked;
    }

    public static boolean isLocked() {
        return animationLocked;
    }
}
