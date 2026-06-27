/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.neoforged.neoforge.client.settings.IKeyConflictContext
 *  net.neoforged.neoforge.client.settings.KeyConflictContext
 *  net.neoforged.neoforge.client.settings.KeyModifier
 */
package rip.ysm.api.client.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

public final class KeyMappingFactoryImpl {
    private KeyMappingFactoryImpl() {
    }

    public static KeyMapping createInGameAlt(String name, InputConstants.Type type, int keyCode, String category) {
        return new KeyMapping(name, (IKeyConflictContext)KeyConflictContext.IN_GAME, KeyModifier.ALT, type, keyCode, category);
    }

    public static KeyMapping createInGameNone(String name, InputConstants.Type type, int keyCode, String category) {
        return new KeyMapping(name, (IKeyConflictContext)KeyConflictContext.IN_GAME, KeyModifier.NONE, type, keyCode, category);
    }

    public static boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return keyMapping.matches(keyCode, scanCode) && keyMapping.getKeyModifier().equals((Object)KeyModifier.getActiveModifier());
    }
}
