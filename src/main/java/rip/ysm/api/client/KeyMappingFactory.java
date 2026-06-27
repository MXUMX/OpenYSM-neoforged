/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.client.KeyMapping
 */
package rip.ysm.api.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import rip.ysm.api.client.forge.KeyMappingFactoryImpl;

public final class KeyMappingFactory {
    private KeyMappingFactory() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static KeyMapping createInGameAlt(String name, InputConstants.Type type, int keyCode, String category) {
        return KeyMappingFactoryImpl.createInGameAlt(name, type, keyCode, category);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static KeyMapping createInGameNone(String name, InputConstants.Type type, int keyCode, String category) {
        return KeyMappingFactoryImpl.createInGameNone(name, type, keyCode, category);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return KeyMappingFactoryImpl.isActiveAndMatches(keyMapping, keyCode, scanCode);
    }
}
