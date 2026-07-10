package org.openysm.client.compat.simplehats;

import org.openysm.client.compat.curios.CuriosBinding;
import org.openysm.client.compat.curios.CuriosCompat;
import fonnymunkey.simplehats.common.item.HatItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HatCuriosGetter {

    private static final String SLOT_HEAD = "head";

    @Nullable
    public static ItemStack getHeadCurio(LivingEntity livingEntity) {
        try {
            return CuriosCompat.getCuriosInventory(livingEntity).flatMap(curiosItemHandler -> curiosItemHandler.getStacksHandler(SLOT_HEAD)).map(stacksHandler -> CuriosBinding.findInSlot(stacksHandler, itemStack -> itemStack.getItem() instanceof HatItem)).orElse(null);
        } catch (LinkageError | RuntimeException ignored) {
            return null;
        }
    }
}
