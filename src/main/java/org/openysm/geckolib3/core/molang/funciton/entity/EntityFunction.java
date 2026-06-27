package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.entity.Entity;

public abstract class EntityFunction extends ContextFunction<Entity> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Entity;
    }
}