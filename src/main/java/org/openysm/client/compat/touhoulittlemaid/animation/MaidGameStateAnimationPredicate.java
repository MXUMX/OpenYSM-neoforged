package org.openysm.client.compat.touhoulittlemaid.animation;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapability;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidGameRecordManager;

public class MaidGameStateAnimationPredicate implements IAnimationPredicate<MaidCapability> {

    public static final String[] GAME_STATE_ANIMATIONS = {"game_win", "game_lost", "beg"};

    @Override
    public PlayState predicate(AnimationEvent<MaidCapability> event, ExpressionEvaluator<?> evaluator) {
        EntityMaid maid = event.getAnimatable().getEntity();
        if (maid == null || (event.getAnimatable() instanceof IPreviewAnimatable)) {
            return PlayState.STOP;
        }
        if (maid.getVehicle() instanceof EntitySit) {
            MaidGameRecordManager gameRecordManager = maid.getGameRecordManager();
            if (gameRecordManager.isWin()) {
                return IAnimationPredicate.playLoopAnimation(event, "game_win");
            }
            if (gameRecordManager.isLost()) {
                return IAnimationPredicate.playLoopAnimation(event, "game_lost");
            }
        }
        if (maid.isBegging()) {
            return IAnimationPredicate.playLoopAnimation(event, "beg");
        }
        return PlayState.STOP;
    }
}