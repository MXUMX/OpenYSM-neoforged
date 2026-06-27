package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.client.compat.parcool.ParcoolCompat;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.entity.player.Player;

public class PlayerSpecialAnimationPredicate implements IAnimationPredicate<CustomPlayerEntity> {
    @Override
    public PlayState predicate(AnimationEvent<CustomPlayerEntity> event, ExpressionEvaluator<?> evaluator) {
        Player player = event.getAnimatable().getEntity();
        if (player == null || (event.getAnimatable() instanceof IPreviewAnimatable)) {
            return null;
        }
        String str = ParcoolCompat.getActionName(player);
        if (str != null && event.getAnimatable().getAnimation(str) != null) {
            return IAnimationPredicate.predicate(event, str);
        }
        return PlayState.STOP;
    }
}