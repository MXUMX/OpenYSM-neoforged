package org.openysm.event;

import org.openysm.model.ServerModelManager;
import org.openysm.OpenYSM;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class ServerStartupEvent {
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        ServerModelManager.loadModels(result -> {
            if (!result.isSuccess()) {
                event.getServer().execute(() -> {
                    throw new RuntimeException("YSM Loading Failed: " + result.getErrorMessage().getString(256));
                });
            }
        }, null);
    }
}