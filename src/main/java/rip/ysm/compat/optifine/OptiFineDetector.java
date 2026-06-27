/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.compat.optifine;

import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.compat.optifine.forge.OptiFineDetectorImpl;

public final class OptiFineDetector {
    private OptiFineDetector() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isOptifinePresent() {
        return OptiFineDetectorImpl.isOptifinePresent();
    }
}

