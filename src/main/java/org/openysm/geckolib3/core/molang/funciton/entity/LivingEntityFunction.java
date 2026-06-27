package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.entity.LivingEntity;

public abstract class LivingEntityFunction extends ContextFunction<LivingEntity> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof LivingEntity;
    }
}