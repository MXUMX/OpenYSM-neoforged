/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.compat.bettercombat;

import org.openysm.client.animation.molang.CtrlBinding;
import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.compat.bettercombat.forge.BetterCombatCompatImpl;

public final class BetterCombatCompat {
    private BetterCombatCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return BetterCombatCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerBindings(CtrlBinding binding) {
        BetterCombatCompatImpl.registerBindings(binding);
    }
}

