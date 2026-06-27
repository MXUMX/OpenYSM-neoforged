package org.openysm.client.animation;

import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.entity.LivingEntity;

public class StopAnimationPredicate implements IAnimationPredicate<AnimatableEntity<? extends LivingEntity>> {

    public static final StopAnimationPredicate INSTANCE = new StopAnimationPredicate();

    @Override
    public PlayState predicate(AnimationEvent<AnimatableEntity<? extends LivingEntity>> event, ExpressionEvaluator<?> evaluator) {
        return PlayState.STOP;
    }
}