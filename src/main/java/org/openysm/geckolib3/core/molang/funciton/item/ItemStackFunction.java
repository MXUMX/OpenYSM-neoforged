package org.openysm.geckolib3.core.molang.funciton.item;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.item.ItemStack;

public abstract class ItemStackFunction extends ContextFunction<ItemStack> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof ItemStack;
    }
}