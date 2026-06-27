/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.compat.realcamera;

import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.compat.realcamera.forge.RealCameraCompatImpl;

public final class RealCameraCompat {
    private RealCameraCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return RealCameraCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isActive() {
        return RealCameraCompatImpl.isActive();
    }
}

