package org.openysm.geckolib3.core.molang.funciton.blocks;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class AbstractBlockFunction extends ContextFunction<BlockBehaviour> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof BlockBehaviour;
    }
}