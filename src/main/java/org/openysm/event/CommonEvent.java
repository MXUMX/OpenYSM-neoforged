package org.openysm.event;

import org.openysm.OpenYSM;
import org.openysm.capability.*;
import org.openysm.model.ServerModelManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

import java.io.IOException;
public final class CommonEvent {
    public static Object nativeInit() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            org.openysm.client.ClientCommonSetup.nativeInit();
        }
        try {
            ServerModelManager.reloadPacks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        if (!OpenYSM.isAvailable()) {
            event.enqueueWork(() -> {
                OpenYSM.LOGGER.error(OpenYSM.getErrorMessage());
            });
        } else {
            event.enqueueWork(() -> {
                nativeInit();
            });
        }
    }
}
