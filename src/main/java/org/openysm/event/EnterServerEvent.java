package org.openysm.event;

import org.openysm.OpenYSM;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.S2CVersionCheckPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
public final class EnterServerEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        Player entity = event.getEntity();
        if (entity instanceof ServerPlayer) {
            NetworkHandler.sendToClientPlayer(new S2CVersionCheckPacket(), entity);
        }
    }
}