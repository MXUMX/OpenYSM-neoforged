package org.openysm.geckolib3.core.molang.variable.item;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.item.ItemStack;

public class ItemStackVariable extends LambdaVariable<ItemStack> {
    public ItemStackVariable(IValueEvaluator<?, IContext<ItemStack>> evaluator) {
        super(evaluator);
    }

    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof ItemStack;
    }
}