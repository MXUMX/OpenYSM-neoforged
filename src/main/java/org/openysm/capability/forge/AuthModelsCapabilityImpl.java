/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package org.openysm.capability.forge;

import org.openysm.capability.AuthModelsCapability;
import org.openysm.forge.capability.AuthModelsCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;

public final class AuthModelsCapabilityImpl {
    private AuthModelsCapabilityImpl() {
    }

    public static Optional<AuthModelsCapability> get(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, AuthModelsCapabilityProvider.AUTH_MODELS_CAP).resolve();
    }
}

