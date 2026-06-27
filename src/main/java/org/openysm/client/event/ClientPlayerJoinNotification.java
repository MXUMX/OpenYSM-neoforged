package org.openysm.client.event;

import org.openysm.client.ClientModelManager;
import org.openysm.OpenYSM;
import org.openysm.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class ClientPlayerJoinNotification {

    private static boolean notified = false;

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (notified) {
            return;
        }
        ClientModelManager.runPendingModelCallback();
        notified = true;
        if (!OpenYSM.isAvailable()) {
            OpenYSM.sendUnavailableMessage();
        } else {
            if (Minecraft.getInstance().isLocalServer()) {
                return;
            }
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(60000L);
                    Minecraft.getInstance().execute(() -> {
                        LocalPlayer localPlayer = Minecraft.getInstance().player;
                        if (localPlayer != null && localPlayer.connection.isAcceptingMessages() && !NetworkHandler.isConnectionValid(localPlayer.connection.getConnection())) {
                            localPlayer.sendSystemMessage(Component.translatable("message.openysm.client.server_not_found"));
                        }
                    });
                } catch (InterruptedException e) {
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        if (notified) {
            notified = false;
            if (!OpenYSM.isAvailable()) {
                return;
            }
            ClientModelManager.resetSync();
        }
    }
}