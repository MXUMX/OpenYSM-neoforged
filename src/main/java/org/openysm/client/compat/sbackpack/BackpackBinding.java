package org.openysm.client.compat.sbackpack;

import org.openysm.client.animation.molang.CtrlBinding;

public class BackpackBinding {
    public static void registerFunctions(CtrlBinding binding) {
        binding.livingEntityVar("has_sophisticated_backpack", context -> {
            return SBackpackCompat.getBackpackItem(context.entity()) != null;
        });
    }
}