/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package org.openysm.capability.forge;

import org.openysm.capability.VehicleCapability;
import org.openysm.forge.capability.VehicleCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;

public final class VehicleCapabilityImpl {
    private VehicleCapabilityImpl() {
    }

    public static Optional<VehicleCapability> get(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, VehicleCapabilityProvider.VEHICLE_CAP).resolve();
    }
}

