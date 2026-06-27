package org.openysm.geckolib3.core.molang.variable.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.client.player.AbstractClientPlayer;

public class ClientPlayerEntityVariable extends LambdaVariable<AbstractClientPlayer> {
    public ClientPlayerEntityVariable(IValueEvaluator<?, IContext<AbstractClientPlayer>> evaluator) {
        super(evaluator);
    }

    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof AbstractClientPlayer;
    }
}