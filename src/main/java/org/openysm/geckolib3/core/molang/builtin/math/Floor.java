package org.openysm.geckolib3.core.molang.builtin.math;

import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.molang.runtime.Function;
import net.minecraft.util.Mth;

public class Floor implements Function {
    @Override
    public Object evaluate(ExecutionContext<?> context, ArgumentCollection arguments) {
        return Mth.floor(arguments.getAsFloat(context, 0));
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}