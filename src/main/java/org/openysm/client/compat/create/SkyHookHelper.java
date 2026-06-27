package org.openysm.client.compat.create;

import org.openysm.mixin.client.create.PlayerSkyhookRendererAccessor;
import net.minecraft.world.entity.player.Player;

public class SkyHookHelper {
    public static boolean isPlayerOnSkyHook(Player player) {
        return PlayerSkyhookRendererAccessor.hangingPlayers().contains(player.getUUID());
    }
}