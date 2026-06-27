package org.openysm.client.compat.touhoulittlemaid.event;

import org.openysm.OpenYSM;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public final class MaidCapabilityEvent {

    private static final ResourceLocation CAPABILITY_KEY = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "ysm_maid");

    @SubscribeEvent
    public void onAttachCapabilities(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
        // NeoForge 1.21 removed AttachCapabilitiesEvent; maid state is queried lazily by the compatibility bridge.
    }
}
