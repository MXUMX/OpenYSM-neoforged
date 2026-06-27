package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.compat.carryon.CarryOnDataHelper;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class PlayerAnimationPredicate implements IAnimationPredicate<CustomPlayerEntity> {
    @Override
    public PlayState predicate(AnimationEvent<CustomPlayerEntity> event, ExpressionEvaluator<?> evaluator) {
        Player player = event.getAnimatable().getEntity();
        if (player == null || (event.getAnimatable() instanceof IPreviewAnimatable)) {
            return PlayState.STOP;
        }
        if (player.getPose() == Pose.SWIMMING) {
            return PlayState.STOP;
        }
        if (player.getPose() == Pose.FALL_FLYING && player.isFallFlying()) {
            return PlayState.STOP;
        }
        switch (CarryOnDataHelper.getCarryType(player)) {
            case ENTITY:
                return IAnimationPredicate.playLoopAnimation(event, "carryon:entity");
            case BLOCK:
                return IAnimationPredicate.playLoopAnimation(event, "carryon:block");
            case PLAYER:
                return IAnimationPredicate.playLoopAnimation(event, "carryon:player");
            default:
                break;
        }
        return PlayState.STOP;
    }
}
