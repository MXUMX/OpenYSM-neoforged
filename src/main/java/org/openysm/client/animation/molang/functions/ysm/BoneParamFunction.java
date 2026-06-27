package org.openysm.client.animation.molang.functions.ysm;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.entity.EntityFunction;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.core.processor.IBone;
import org.openysm.client.animation.molang.struct.Vec3fStruct;
import org.openysm.molang.runtime.ExecutionContext;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class BoneParamFunction extends EntityFunction {
    public abstract Vec3fStruct getParam(@NotNull IBone bone);

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }

    @Override
    public Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        IBone bone;
        int name = arguments.getStringId(context, 0);
        if (name == StringPool.EMPTY_ID || (bone = context.entity().geoInstance().getBone(name)) == null) {
            return null;
        }
        return getParam(bone);
    }
}