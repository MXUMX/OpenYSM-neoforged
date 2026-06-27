package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;

public class PlayerBaseAnimationPredicate implements IAnimationPredicate<CustomPlayerEntity> {
    @Override
    public PlayState predicate(AnimationEvent<CustomPlayerEntity> event, ExpressionEvaluator<?> evaluator) {
        CustomPlayerEntity playerEntity = event.getAnimatable();
        if (playerEntity instanceof IPreviewAnimatable previewAnimatable) {
            if (previewAnimatable.getAnimationStateMachine().hasAnimation()) {
                return IAnimationPredicate.playLoopAnimation(event, previewAnimatable.getAnimationStateMachine().getCurrentAnimation());
            }
            return PlayState.STOP;
        }
        if (playerEntity.isModelSwitching()) {
            if (playerEntity.isDisabledState()) {
                playerEntity.enableModel();
                event.getController().stopTransition();
            }
            return IAnimationPredicate.predicate(event, playerEntity.getSelectedModelId());
        }
        return PlayState.STOP;
    }
}