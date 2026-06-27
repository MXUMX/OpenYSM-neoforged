package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.GeckoVehicleEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.entity.Entity;

public class RideStateAnimationPredicate implements IAnimationPredicate<GeckoVehicleEntity> {

    public static final String[] ANIMATION_NAMES = {"has_ride", "not_ride"};

    @Override
    public PlayState predicate(AnimationEvent<GeckoVehicleEntity> event, ExpressionEvaluator<?> evaluator) {
        Entity entity = event.getAnimatable().getEntity();
        if (entity == null) {
            return PlayState.STOP;
        }
        if (!entity.getPassengers().isEmpty()) {
            return IAnimationPredicate.predicate(event, "has_ride");
        }
        return IAnimationPredicate.predicate(event, "not_ride");
    }
}