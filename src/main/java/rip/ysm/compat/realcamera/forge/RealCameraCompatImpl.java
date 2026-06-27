/*
 * Decompiled with CFR 0.152.
 */
package rip.ysm.compat.realcamera.forge;

import org.openysm.client.compat.realcamera.RealCameraCompat;

public final class RealCameraCompatImpl {
    private RealCameraCompatImpl() {
    }

    public static boolean isLoaded() {
        return RealCameraCompat.isLoaded();
    }

    public static boolean isActive() {
        return RealCameraCompat.isActive();
    }
}

