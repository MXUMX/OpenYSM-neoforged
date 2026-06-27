package org.openysm.geckolib3.core.molang.builtin.math;

import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.molang.runtime.Function;

public class Ceil implements Function {
    @Override
    public Object evaluate(ExecutionContext<?> context, ArgumentCollection arguments) {
        return Math.ceil(arguments.getAsFloat(context, 0));
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}