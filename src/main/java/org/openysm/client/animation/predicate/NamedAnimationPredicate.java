package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.molang.runtime.ExpressionEvaluator;

public class NamedAnimationPredicate<T extends AnimatableEntity<?>> implements IAnimationPredicate<T> {

    private final String animationName;

    public NamedAnimationPredicate(String animationName) {
        this.animationName = animationName;
    }

    @Override
    public PlayState predicate(AnimationEvent<T> event, ExpressionEvaluator<?> evaluator) {
        return IAnimationPredicate.playLoopAnimation(event, this.animationName);
    }
}