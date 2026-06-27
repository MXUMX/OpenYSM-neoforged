/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.common.ModConfigSpec
 *  net.neoforged.fml.ModLoadingContext
 *  net.neoforged.fml.config.IConfigSpec
 *  net.neoforged.fml.config.ModConfig$Type
 */
package rip.ysm.api.config.forge;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

public final class ConfigRegistrationImpl {
    private ConfigRegistrationImpl() {
    }

    public static void register(String modId, ModConfig.Type type, ModConfigSpec spec) {
        ModLoadingContext.get().getActiveContainer().registerConfig(type, (IConfigSpec)spec);
    }
}
