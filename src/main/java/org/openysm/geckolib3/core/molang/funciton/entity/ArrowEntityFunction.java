package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.entity.projectile.Arrow;

public abstract class ArrowEntityFunction extends ContextFunction<Arrow> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Arrow;
    }
}