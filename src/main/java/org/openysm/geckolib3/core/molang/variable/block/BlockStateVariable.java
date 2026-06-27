package org.openysm.geckolib3.core.molang.variable.block;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateVariable extends LambdaVariable<BlockState> {
    public BlockStateVariable(IValueEvaluator<?, IContext<BlockState>> evaluator) {
        super(evaluator);
    }

    @Override
    public boolean validateContext(IContext<?> context) {
        return context.entity() instanceof BlockState;
    }
}