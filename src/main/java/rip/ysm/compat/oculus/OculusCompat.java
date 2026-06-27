/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.compat.oculus;

import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.compat.oculus.forge.OculusCompatImpl;

public final class OculusCompat {
    private OculusCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return OculusCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isPBRActive() {
        return OculusCompatImpl.isPBRActive();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void updatePBRState() {
        OculusCompatImpl.updatePBRState();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isShaderPackInUse() {
        return OculusCompatImpl.isShaderPackInUse();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isRenderingShadowPass() {
        return OculusCompatImpl.isRenderingShadowPass();
    }
}

