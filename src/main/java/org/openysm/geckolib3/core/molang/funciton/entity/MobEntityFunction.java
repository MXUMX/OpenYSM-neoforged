package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.entity.Mob;

public abstract class MobEntityFunction extends ContextFunction<Mob> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Mob;
    }
}