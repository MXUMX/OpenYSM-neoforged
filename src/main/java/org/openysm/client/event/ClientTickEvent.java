package org.openysm.client.event;

import org.openysm.client.ClientModelManager;
import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.client.upload.UploadManager;
import org.openysm.audio.ObjectPool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.SubscribeEvent;
public class ClientTickEvent {

    private static int tickCount;

    private static int refreshRate = 60;

    @SubscribeEvent
    public static void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        tickCount++;
        UploadManager.processPendingUploads();
        ClientModelManager.flushPendingModels();
        ObjectPool.cleanup();
        refreshRate = Minecraft.getInstance().getWindow().getRefreshRate();
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            org.openysm.capability.YSMCapabilities.get(localPlayer, PlayerCapabilityProvider.PLAYER_CAP).ifPresent((v0) -> {
                v0.tickAnimations();
            });
        }
    }

    public static int getTickCount() {
        return tickCount;
    }

    public static int getRefreshRate() {
        return refreshRate;
    }
}
