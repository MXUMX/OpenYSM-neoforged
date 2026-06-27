package org.openysm.geckolib3.core.molang.variable.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class AbstractArrowEntityVariable extends LambdaVariable<AbstractArrow> {
    public AbstractArrowEntityVariable(IValueEvaluator<?, IContext<AbstractArrow>> evaluator) {
        super(evaluator);
    }

    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof AbstractArrow;
    }
}