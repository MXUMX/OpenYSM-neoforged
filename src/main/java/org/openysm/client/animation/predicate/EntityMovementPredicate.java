package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.GeckoVehicleEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.entity.Entity;

public class EntityMovementPredicate implements IAnimationPredicate<GeckoVehicleEntity> {

    public static final String[] MOVEMENT_STATES = {"water", "ground", "fly"};

    @Override
    public PlayState predicate(AnimationEvent<GeckoVehicleEntity> event, ExpressionEvaluator<?> evaluator) {
        Entity entity = event.getAnimatable().getEntity();
        if (entity == null) {
            return PlayState.STOP;
        }
        if (entity.isInWater()) {
            return IAnimationPredicate.predicate(event, "water");
        }
        if (entity.onGround()) {
            return IAnimationPredicate.predicate(event, "ground");
        }
        return IAnimationPredicate.predicate(event, "fly");
    }
}