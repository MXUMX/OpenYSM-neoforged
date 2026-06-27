/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.slashblade;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.compat.slashblade.forge.SlashBladeCompatImpl;

public final class SlashBladeCompat {
    private SlashBladeCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return SlashBladeCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isSlashBladeItem(ItemStack itemStack) {
        return SlashBladeCompatImpl.isSlashBladeItem(itemStack);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static String getComboAnimName(AnimationEvent<? extends LivingAnimatable<?>> event) {
        return SlashBladeCompatImpl.getComboAnimName(event);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static PlayState handleSlashBladeAnim(LivingEntity livingEntity, AnimationEvent<? extends LivingAnimatable<?>> event, String str, ILoopType loopType) {
        return SlashBladeCompatImpl.handleSlashBladeAnim(livingEntity, event, str, loopType);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerControllerFunctions(CtrlBinding ctrlBinding) {
        SlashBladeCompatImpl.registerControllerFunctions(ctrlBinding);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean hasNewApi() {
        return SlashBladeCompatImpl.hasNewApi();
    }
}
