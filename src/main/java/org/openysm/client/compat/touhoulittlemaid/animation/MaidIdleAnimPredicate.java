package org.openysm.client.compat.touhoulittlemaid.animation;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapability;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;

public class MaidIdleAnimPredicate implements IAnimationPredicate<MaidCapability> {
    @Override
    public PlayState predicate(AnimationEvent<MaidCapability> event, ExpressionEvaluator<?> evaluator) {
        MaidCapability capability = event.getAnimatable();
        if (capability instanceof IPreviewAnimatable previewAnimatable) {
            if (previewAnimatable.getAnimationStateMachine().hasAnimation()) {
                return IAnimationPredicate.playLoopAnimation(event, previewAnimatable.getAnimationStateMachine().getCurrentAnimation());
            }
            return PlayState.STOP;
        }
        if (capability.isModelAvailable()) {
            if (capability.hasModel()) {
                capability.refreshModel();
                event.getController().stopTransition();
            }
            return IAnimationPredicate.predicate(event, capability.getModelTextureId());
        }
        return PlayState.STOP;
    }
}