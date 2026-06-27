package org.openysm.geckolib3.core.controller;

import org.openysm.geckolib3.core.snapshot.BoneTopLevelSnapshot;
import org.openysm.geckolib3.core.molang.context.AnimationContext;
import org.openysm.geckolib3.core.util.TransitionVector3f;
import org.openysm.molang.runtime.ExpressionEvaluator;

import java.util.Optional;

public interface BoneTransformProvider {
    BoneTopLevelSnapshot getBoneTarget();

    Optional<TransitionVector3f> getRotation(ExpressionEvaluator<AnimationContext<?>> evaluator);

    Optional<TransitionVector3f> getPosition(ExpressionEvaluator<AnimationContext<?>> evaluator);

    Optional<TransitionVector3f> getScale(ExpressionEvaluator<AnimationContext<?>> evaluator);
}