package org.openysm.client.compat.slashblade;

import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.core.enums.PlayState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlashBladeStateHelper {
    public static boolean isSlashBlade(ItemStack itemStack) {
        return SlashBladeBridge.isSlashBlade(itemStack);
    }

    public static String getSlashBladeAnimation(AnimationEvent<?> event) {
        if (event == null || !(event.getAnimatable() instanceof org.openysm.client.entity.LivingAnimatable<?> livingAnimatable)) {
            return StringPool.EMPTY;
        }
        return SlashBladeBridge.getComboAnimationName((LivingEntity) livingAnimatable.getEntity());
    }

    public static String getSlashBladeAnimationFromContext(IContext<? extends LivingEntity> context) {
        return SlashBladeBridge.getComboAnimationName(context);
    }

    public static PlayState handleSlashBladeAnim(AnimationEvent<? extends AnimatableEntity<? extends LivingEntity>> event, String animation, ILoopType loopType) {
        String str2 = "slashblade:" + animation;
        if (SlashBladeBridge.hasAnimation((AnimationEvent<? extends org.openysm.client.entity.LivingAnimatable<?>>) (AnimationEvent<?>) event, str2)) {
            return setAnimAndContinue(event, str2, loopType);
        }
        return setAnimAndContinue(event, animation, loopType);
    }

    @NotNull
    private static PlayState setAnimAndContinue(AnimationEvent<?> event, String animation, ILoopType loopType) {
        event.getController().setAnimation(animation, loopType);
        return PlayState.CONTINUE;
    }
}
