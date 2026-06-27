/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package org.openysm.capability.forge;

import org.openysm.capability.ModelInfoCapability;
import org.openysm.forge.capability.ModelInfoCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;

public final class ModelInfoCapabilityImpl {
    private ModelInfoCapabilityImpl() {
    }

    public static Optional<ModelInfoCapability> get(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, ModelInfoCapabilityProvider.MODEL_INFO_CAP).resolve();
    }
}

