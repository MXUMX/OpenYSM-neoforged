package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.client.player.AbstractClientPlayer;

public abstract class AbstractClientPlayerFunction extends ContextFunction<AbstractClientPlayer> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof AbstractClientPlayer;
    }
}