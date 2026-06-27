package org.openysm.client.compat.bettercombat;

import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.client.animation.molang.CtrlBinding;
import net.neoforged.fml.loading.LoadingModList;

public class BetterCombatCompat {

    private static final String MOD_ID = "bettercombat";

    private static boolean IS_LOADED;

    public static void init() {
        if (LoadingModList.get().getModFileById(MOD_ID) != null) {
            BetterCombatBinding.initialize();
            IS_LOADED = true;
        }
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static void registerBindings(CtrlBinding binding) {
        if (isLoaded()) {
            BetterCombatBinding.registerBindings(binding);
        } else {
            registerDummyBindings(binding);
        }
    }

    private static void registerDummyBindings(CtrlBinding binding) {
        binding.clientPlayerEntityVar("bcombat_attack_animation", interfaceC0807x6b368640 -> StringPool.EMPTY);
    }
}