package org.openysm.client.compat.top;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
public final class TheOneProbeCompatEvent {
    @SubscribeEvent
    public static void onInterModEnqueue(InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> {
            return new TheOneProbeEntityProvider();
        });
    }
}