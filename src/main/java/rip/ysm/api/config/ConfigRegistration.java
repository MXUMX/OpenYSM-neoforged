/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.neoforged.neoforge.common.ModConfigSpec
 *  net.neoforged.fml.config.ModConfig$Type
 */
package rip.ysm.api.config;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig;
import rip.ysm.api.config.forge.ConfigRegistrationImpl;

public final class ConfigRegistration {
    private ConfigRegistration() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void register(String modId, ModConfig.Type type, ModConfigSpec spec) {
        ConfigRegistrationImpl.register(modId, type, spec);
    }
}
