package org.openysm.geckolib3.core.molang.variable.item;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.item.Item;

public class ItemVariable extends LambdaVariable<Item> {
    public ItemVariable(IValueEvaluator<?, IContext<Item>> evaluator) {
        super(evaluator);
    }

    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Item;
    }
}