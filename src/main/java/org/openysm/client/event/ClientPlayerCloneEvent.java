package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.network.NetworkHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class ClientPlayerCloneEvent {
    @SubscribeEvent
    public static void onPlayerClone(ClientPlayerNetworkEvent.Clone event) {
        if (!OpenYSM.isAvailable() || !NetworkHandler.isClientConnected()) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(event.getOldPlayer(), PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> org.openysm.capability.YSMCapabilities.get(event.getNewPlayer(), PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap2 -> cap2.copyFrom(cap)));
    }
}
