/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.compat.firstperson;

import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.compat.firstperson.forge.FirstPersonCompatImpl;

public final class FirstPersonCompat {
    private FirstPersonCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return FirstPersonCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isFirstPersonActive() {
        return FirstPersonCompatImpl.isFirstPersonActive();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean shouldHideHead() {
        return FirstPersonCompatImpl.shouldHideHead();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void setCameraDistance(float distance) {
        FirstPersonCompatImpl.setCameraDistance(distance);
    }
}

