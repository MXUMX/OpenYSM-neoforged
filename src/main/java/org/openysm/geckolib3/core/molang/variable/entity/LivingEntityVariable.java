package org.openysm.geckolib3.core.molang.variable.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.entity.LivingEntity;

public class LivingEntityVariable extends LambdaVariable<LivingEntity> {
    public LivingEntityVariable(IValueEvaluator<?, IContext<LivingEntity>> evaluator) {
        super(evaluator);
    }

    @Override
    protected boolean validateContext(IContext<?> context) {
        return context.entity() instanceof LivingEntity;
    }
}