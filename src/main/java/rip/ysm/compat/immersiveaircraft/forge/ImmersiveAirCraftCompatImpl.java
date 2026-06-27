/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3f
 */
package rip.ysm.compat.immersiveaircraft.forge;

import org.openysm.client.compat.immersiveaircraft.ImmersiveAirCraftCompat;
import org.openysm.client.entity.GeckoVehicleEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import java.util.Optional;
import org.joml.Vector3f;

public final class ImmersiveAirCraftCompatImpl {
    private ImmersiveAirCraftCompatImpl() {
    }

    public static boolean isLoaded() {
        return ImmersiveAirCraftCompat.isLoaded();
    }

    public static Optional<Vector3f> getAircraftRotation(AnimationEvent<GeckoVehicleEntity> event) {
        return ImmersiveAirCraftCompat.getAircraftRotation(event);
    }
}

