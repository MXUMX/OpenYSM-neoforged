package org.openysm.geckolib3.core.molang.variable.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.client.player.LocalPlayer;

public class LocalPlayerEntityVariable extends LambdaVariable<LocalPlayer> {
    public LocalPlayerEntityVariable(IValueEvaluator<?, IContext<LocalPlayer>> evaluator) {
        super(evaluator);
    }

    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof LocalPlayer;
    }
}