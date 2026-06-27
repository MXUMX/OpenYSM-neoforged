/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package org.openysm.capability.forge;

import org.openysm.capability.StarModelsCapability;
import org.openysm.forge.capability.StarModelsCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;

public final class StarModelsCapabilityImpl {
    private StarModelsCapabilityImpl() {
    }

    public static Optional<StarModelsCapability> get(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, StarModelsCapabilityProvider.STAR_MODELS_CAP).resolve();
    }
}

