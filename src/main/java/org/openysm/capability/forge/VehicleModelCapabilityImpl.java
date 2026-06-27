/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package org.openysm.capability.forge;

import org.openysm.capability.VehicleModelCapability;
import org.openysm.forge.capability.VehicleModelCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;

public final class VehicleModelCapabilityImpl {
    private VehicleModelCapabilityImpl() {
    }

    public static Optional<VehicleModelCapability> get(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP).resolve();
    }
}

