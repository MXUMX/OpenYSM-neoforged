package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import org.openysm.client.animation.condition.ConditionPassenger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.StringUtils;

public class OffhandAttackAnimationPredicate implements IAnimationPredicate<LivingAnimatable<?>> {
    @Override
    public PlayState predicate(AnimationEvent<LivingAnimatable<?>> event, ExpressionEvaluator<?> evaluator) {
        LivingEntity livingEntity = (LivingEntity) ((LivingAnimatable) event.getAnimatable()).getEntity();
        if (livingEntity == null || (event.getAnimatable() instanceof IPreviewAnimatable)) {
            return PlayState.STOP;
        }
        Entity firstPassenger = livingEntity.getFirstPassenger();
        if (firstPassenger == null || !firstPassenger.isAlive()) {
            return PlayState.STOP;
        }
        ConditionPassenger conditionPassenger = event.getAnimatable().getModelConfig().getPassenger();
        if (conditionPassenger != null) {
            String str = conditionPassenger.doTest(livingEntity);
            if (StringUtils.isNoneBlank(str)) {
                return IAnimationPredicate.playAnimationWithLoop(event, str, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        return PlayState.STOP;
    }
}