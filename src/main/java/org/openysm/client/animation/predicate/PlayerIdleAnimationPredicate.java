package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import org.apache.commons.lang3.StringUtils;

public class PlayerIdleAnimationPredicate implements IAnimationPredicate<CustomPlayerEntity> {
    @Override
    public PlayState predicate(AnimationEvent<CustomPlayerEntity> event, ExpressionEvaluator<?> evaluator) {
        String str = ((IPreviewAnimatable) event.getAnimatable()).getAnimationStateMachine().getQueuedAnimation();
        if (StringUtils.isNoneBlank(str)) {
            return IAnimationPredicate.playLoopAnimation(event, str);
        }
        return PlayState.STOP;
    }
}