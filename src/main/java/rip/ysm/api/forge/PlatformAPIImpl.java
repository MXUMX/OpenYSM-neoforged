/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.fml.loading.FMLEnvironment
 */
package rip.ysm.api.forge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public final class PlatformAPIImpl {
    private PlatformAPIImpl() {
    }

    public static boolean isServer() {
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
    }

    public static String getPlatformName() {
        return "Forge";
    }
}

