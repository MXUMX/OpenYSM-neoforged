package org.openysm.client.compat.gun.common;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.compat.gun.swarfare.SWarfareCompat;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.compat.gun.tacz.TacCompat;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemUseAnimationPredicate implements IAnimationPredicate<LivingAnimatable<?>> {
    @Override
    public PlayState predicate(AnimationEvent<LivingAnimatable<?>> event, ExpressionEvaluator<?> evaluator) {
        LivingEntity livingEntity = (LivingEntity) ((LivingAnimatable) event.getAnimatable()).getEntity();
        if (livingEntity == null || (event.getAnimatable() instanceof IPreviewAnimatable)) {
            return PlayState.STOP;
        }
        if (!livingEntity.swinging && !livingEntity.isUsingItem()) {
            ItemStack itemInHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
            PlayState playState = TacCompat.handleGunActionAnimState(itemInHand, event);
            if (playState == null) {
                playState = SWarfareCompat.handleGunActionAnim(itemInHand, event);
            }
            return Objects.requireNonNullElse(playState, PlayState.STOP);
        }
        return PlayState.STOP;
    }

    public static boolean isLoaded() {
        return TacCompat.isLoaded() || SWarfareCompat.isLoaded();
    }
}