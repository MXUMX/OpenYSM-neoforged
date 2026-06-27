package org.openysm.client.compat.touhoulittlemaid.event;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.event.YsmMaidClientTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class MaidClientTickEvent {
    @SubscribeEvent
    public void onMaidClientTick(YsmMaidClientTickEvent event) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return;
        }
        EntityMaid maid = event.getMaid();
        if (localPlayer.getUUID().equals(maid.getOwnerUUID())) {
            tickMaidModel(maid);
        }
    }

    private void tickMaidModel(EntityMaid entityMaid) {
        org.openysm.capability.YSMCapabilities.get(entityMaid, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
        });
    }
}