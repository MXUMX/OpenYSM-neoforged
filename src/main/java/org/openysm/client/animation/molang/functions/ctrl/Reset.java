package org.openysm.client.animation.molang.functions.ctrl;

import org.openysm.geckolib3.core.controller.PredicateBasedController;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import org.openysm.molang.runtime.ExecutionContext;

public class Reset extends ContextFunction<Object> {
    @Override
    public Object eval(ExecutionContext<IContext<Object>> context, ArgumentCollection arguments) {
        PredicateBasedController<?> animationController = context.entity().animationEvent().getController();
        if (animationController == null) {
            return null;
        }
        animationController.clearAnimation();
        return null;
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 0;
    }
}