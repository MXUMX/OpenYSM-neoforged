package org.openysm.client.animation.molang.functions.ysm;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.entity.EntityFunction;
import org.openysm.geckolib3.util.MolangUtils;
import org.openysm.molang.runtime.ExecutionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import rip.ysm.neoforge.compat.registries.ForgeRegistries;

public class RelativeBlockNameAny extends EntityFunction {
    @Override
    public Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        ResourceLocation key;
        BlockState blockState = MolangUtils.getRelativeBlockState(context, arguments);
        if (blockState == null || (key = ForgeRegistries.BLOCKS.getKey(blockState.getBlock())) == null) {
            return null;
        }
        for (int i = 3; i < arguments.size(); i++) {
            if (key.equals(arguments.getResourceLocation(context, i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size > 3;
    }
}