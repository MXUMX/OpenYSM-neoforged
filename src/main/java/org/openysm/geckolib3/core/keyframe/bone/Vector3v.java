package org.openysm.geckolib3.core.keyframe.bone;

import org.openysm.geckolib3.core.molang.value.IValue;
import org.openysm.molang.runtime.ExpressionEvaluator;
import org.joml.Vector3f;

public class Vector3v {

    private final IValue x;

    private final IValue y;

    private final IValue z;

    public Vector3v(IValue x, IValue y, IValue z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f eval(ExpressionEvaluator<?> evaluator) {
        return new Vector3f(x.evalAsFloat(evaluator),
                y.evalAsFloat(evaluator),
                z.evalAsFloat(evaluator));
    }
}