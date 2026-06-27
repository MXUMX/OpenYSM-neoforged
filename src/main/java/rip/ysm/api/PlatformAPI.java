/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.api.forge.PlatformAPIImpl;

public final class PlatformAPI {
    private PlatformAPI() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isServer() {
        return PlatformAPIImpl.isServer();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static String getPlatformName() {
        return PlatformAPIImpl.getPlatformName();
    }
}

