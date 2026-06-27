package org.openysm.client.compat.touhoulittlemaid.event;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.client.gui.TouhouMaidModelScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.event.OpenYsmMaidScreenEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public final class MaidScreenEvent {
    @SubscribeEvent
    public void onOpenMaidScreen(OpenYsmMaidScreenEvent event) {
        if (org.openysm.capability.YSMCapabilities.get(event.getMaid(), MaidCapabilityProvider.MAID_CAP).isPresent()) {
            Minecraft.getInstance().setScreen(new TouhouMaidModelScreen(event.getMaid()));
        }
    }
}
