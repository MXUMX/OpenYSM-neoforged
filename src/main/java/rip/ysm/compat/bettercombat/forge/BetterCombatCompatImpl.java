/*
 * Decompiled with CFR 0.152.
 */
package rip.ysm.compat.bettercombat.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.bettercombat.BetterCombatCompat;

public final class BetterCombatCompatImpl {
    private BetterCombatCompatImpl() {
    }

    public static boolean isLoaded() {
        return BetterCombatCompat.isLoaded();
    }

    public static void registerBindings(CtrlBinding binding) {
        BetterCombatCompat.registerBindings(binding);
    }
}

