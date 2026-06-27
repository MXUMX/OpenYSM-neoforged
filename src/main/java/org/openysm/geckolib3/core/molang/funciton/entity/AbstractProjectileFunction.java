package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.entity.projectile.Projectile;

public abstract class AbstractProjectileFunction extends ContextFunction<Projectile> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Projectile;
    }
}