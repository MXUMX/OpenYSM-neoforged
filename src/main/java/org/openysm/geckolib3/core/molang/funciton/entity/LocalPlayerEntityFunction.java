package org.openysm.geckolib3.core.molang.funciton.entity;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.client.player.LocalPlayer;

public abstract class LocalPlayerEntityFunction extends ContextFunction<LocalPlayer> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof LocalPlayer;
    }
}