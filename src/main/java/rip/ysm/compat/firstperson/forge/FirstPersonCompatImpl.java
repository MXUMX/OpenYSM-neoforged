/*
 * Decompiled with CFR 0.152.
 */
package rip.ysm.compat.firstperson.forge;

import org.openysm.client.compat.firstperson.FirstPersonCompat;

public final class FirstPersonCompatImpl {
    private FirstPersonCompatImpl() {
    }

    public static boolean isLoaded() {
        return FirstPersonCompat.isLoaded();
    }

    public static boolean isFirstPersonActive() {
        return FirstPersonCompat.isFirstPersonActive();
    }

    public static boolean shouldHideHead() {
        return FirstPersonCompat.shouldHideHead();
    }

    public static void setCameraDistance(float distance) {
        FirstPersonCompat.setCameraDistance(distance);
    }
}

