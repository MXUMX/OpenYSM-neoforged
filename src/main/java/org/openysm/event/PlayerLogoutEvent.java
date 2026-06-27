package org.openysm.event;

import org.openysm.model.ServerModelManager;
import org.openysm.OpenYSM;
import org.openysm.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class PlayerLogoutEvent {
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer) {
            if (NetworkHandler.isPlayerConnected(serverPlayer)) {
                ServerModelManager.syncModelToPlayer(serverPlayer.getUUID());
            }
        }
    }
}