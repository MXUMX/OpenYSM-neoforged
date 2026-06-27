package org.openysm.geckolib3.core.molang.builtin.query;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.util.MolangUtils;
import org.openysm.geckolib3.core.molang.funciton.entity.EntityFunction;
import org.openysm.molang.runtime.ExecutionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rip.ysm.neoforge.compat.registries.ForgeRegistries;

public class RelativeBlockHasAllTags extends EntityFunction {
    @Override
    public Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        BlockState block = MolangUtils.getRelativeBlockState(context, arguments);
        if (block == null) {
            return null;
        }
        for (int i = 3; i < arguments.size(); i++) {
            ResourceLocation tagId = arguments.getResourceLocation(context, i);
            if (tagId == null) {
                return null;
            }

            TagKey<Block> tag = ForgeRegistries.BLOCKS.tags().createTagKey(tagId);
            if (!block.is(tag)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size >= 4;
    }
}