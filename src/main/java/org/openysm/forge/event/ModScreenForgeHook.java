/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.InterModComms
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 *  net.neoforged.fml.common.Mod$EventBusSubscriber$Bus
 *  net.neoforged.fml.event.lifecycle.InterModProcessEvent
 */
package org.openysm.forge.event;

import org.openysm.OpenYSM;
import org.openysm.client.event.ModScreenEvent;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
public final class ModScreenForgeHook {
    private ModScreenForgeHook() {
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public static void onProcessIMC(InterModProcessEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        InterModComms.getMessages((String)"openysm").findFirst().ifPresent(message -> {
            Object screenObj;
            if ("DownloadScreen".equals(message.method()) && (screenObj = message.messageSupplier().get()) instanceof Screen) {
                Screen screen = (Screen)screenObj;
                ModScreenEvent.setReceivedScreen(screen);
            }
        });
    }
}

