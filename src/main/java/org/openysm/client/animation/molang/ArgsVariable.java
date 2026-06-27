package org.openysm.client.animation.molang;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.molang.runtime.Variable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArgsVariable implements Variable {

    public static final ArgsVariable INSTANCE = new ArgsVariable();

    @Override
    @Nullable
    public Object evaluate(@NotNull ExecutionContext<?> context) {
        Object entity = context.entity();
        if (entity instanceof IContext) {
            return ((IContext<?>) entity).getAnimationLayers();
        }
        return null;
    }
}