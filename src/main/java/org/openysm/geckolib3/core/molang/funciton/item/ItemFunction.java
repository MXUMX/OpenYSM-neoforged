package org.openysm.geckolib3.core.molang.funciton.item;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.item.Item;

public abstract class ItemFunction extends ContextFunction<Item> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Item;
    }
}