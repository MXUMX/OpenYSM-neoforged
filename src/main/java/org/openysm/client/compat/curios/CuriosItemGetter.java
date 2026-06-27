package org.openysm.client.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosItemGetter {

    private static final String SLOT_BACK = "back";

    @Nullable
    public static ItemStack getBackCurio(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity).resolve().flatMap(iCuriosItemHandler -> iCuriosItemHandler.getStacksHandler(SLOT_BACK)).map(stacksHandler -> {
            return CuriosBinding.findInSlot(stacksHandler, itemStack -> {
                return itemStack.getItem() instanceof BackpackItem;
            });
        }).orElse(null);
    }
}
