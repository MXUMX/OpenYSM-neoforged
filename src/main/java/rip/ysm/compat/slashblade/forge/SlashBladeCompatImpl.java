/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.slashblade.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.slashblade.SlashBladeCompat;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class SlashBladeCompatImpl {
    private SlashBladeCompatImpl() {
    }

    public static boolean isLoaded() {
        return SlashBladeCompat.isLoaded();
    }

    public static boolean isSlashBladeItem(ItemStack itemStack) {
        return SlashBladeCompat.isSlashBladeItem(itemStack);
    }

    public static String getComboAnimName(AnimationEvent<? extends LivingAnimatable<?>> event) {
        return SlashBladeCompat.getComboAnimName(event);
    }

    public static PlayState handleSlashBladeAnim(LivingEntity livingEntity, AnimationEvent<? extends LivingAnimatable<?>> event, String str, ILoopType loopType) {
        return SlashBladeCompat.handleSlashBladeAnim(livingEntity, event, str, loopType);
    }

    public static void registerControllerFunctions(CtrlBinding ctrlBinding) {
        SlashBladeCompat.registerControllerFunctions(ctrlBinding);
    }

    public static boolean hasNewApi() {
        return SlashBladeCompat.hasNewApi();
    }
}

