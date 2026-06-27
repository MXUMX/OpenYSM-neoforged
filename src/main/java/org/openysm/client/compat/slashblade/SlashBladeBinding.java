package org.openysm.client.compat.slashblade;

import org.openysm.client.animation.molang.CtrlBinding;

public class SlashBladeBinding {
    public static void registerFunctions(CtrlBinding binding) {
        binding.livingEntityVar("slashblade_animation", SlashBladeStateHelper::getSlashBladeAnimationFromContext);
    }
}