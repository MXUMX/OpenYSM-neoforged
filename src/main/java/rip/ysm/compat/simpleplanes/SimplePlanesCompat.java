/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  org.joml.Vector3f
 */
package rip.ysm.compat.simpleplanes;

import org.openysm.client.entity.GeckoVehicleEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Optional;
import org.joml.Vector3f;
import rip.ysm.compat.simpleplanes.forge.SimplePlanesCompatImpl;

public final class SimplePlanesCompat {
    private SimplePlanesCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return SimplePlanesCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Optional<Vector3f> getSimplePlanesRotation(AnimationEvent<GeckoVehicleEntity> event) {
        return SimplePlanesCompatImpl.getSimplePlanesRotation(event);
    }
}

