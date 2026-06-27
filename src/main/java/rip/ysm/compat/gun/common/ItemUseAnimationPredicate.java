/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.gun.common;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.molang.runtime.ExpressionEvaluator;
import java.util.Objects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.compat.gun.swarfare.SWarfareCompat;
import rip.ysm.compat.gun.tacz.TacCompat;

public class ItemUseAnimationPredicate
implements IAnimationPredicate<LivingAnimatable<?>> {
    @Override
    public PlayState predicate(AnimationEvent<LivingAnimatable<?>> event, ExpressionEvaluator<?> evaluator) {
        LivingEntity livingEntity = (LivingEntity)event.getAnimatable().getEntity();
        if (livingEntity == null || event.getAnimatable() instanceof IPreviewAnimatable) {
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

