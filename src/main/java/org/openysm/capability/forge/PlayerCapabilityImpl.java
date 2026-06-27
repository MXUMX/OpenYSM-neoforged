/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package org.openysm.capability.forge;

import org.openysm.capability.PlayerCapability;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class PlayerCapabilityImpl {
    private PlayerCapabilityImpl() {
    }

    public static Optional<PlayerCapability> get(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, PlayerCapabilityProvider.PLAYER_CAP).resolve();
    }

    public static Optional<PlayerCapability> get(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, PlayerCapabilityProvider.PLAYER_CAP).resolve();
    }
}

