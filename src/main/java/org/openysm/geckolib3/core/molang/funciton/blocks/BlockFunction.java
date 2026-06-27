package org.openysm.geckolib3.core.molang.funciton.blocks;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import net.minecraft.world.level.block.Block;

public abstract class BlockFunction extends ContextFunction<Block> {
    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Block;
    }
}