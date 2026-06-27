package org.openysm.client.compat.slashblade;

import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.core.enums.PlayState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.openysm.client.animation.molang.CtrlBinding;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class SlashBladeCompat {

    private static final String MOD_ID = "slashblade";

    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = ModList.get().isLoaded(MOD_ID) && SlashBladeBridge.isAvailable();
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static boolean isSlashBladeItem(ItemStack itemStack) {
        return isLoaded() && SlashBladeStateHelper.isSlashBlade(itemStack);
    }

    public static String getComboAnimName(AnimationEvent<? extends LivingAnimatable<?>> event) {
        if (isLoaded()) {
            return SlashBladeStateHelper.getSlashBladeAnimation(event);
        }
        return StringPool.EMPTY;
    }

    @Nullable
    public static PlayState handleSlashBladeAnim(LivingEntity livingEntity, AnimationEvent<? extends LivingAnimatable<?>> event, String str, ILoopType loopType) {
        if (isLoaded() && isSlashBladeItem(livingEntity.getMainHandItem())) {
            return SlashBladeStateHelper.handleSlashBladeAnim(event, str, loopType);
        }
        return null;
    }

    public static void registerControllerFunctions(CtrlBinding ctrlBinding) {
        if (isLoaded()) {
            SlashBladeBinding.registerFunctions(ctrlBinding);
        } else {
            registerSlashBladeFunctions(ctrlBinding);
        }
    }

    public static boolean hasNewApi() {
        return isLoaded();
    }

    private static void registerSlashBladeFunctions(CtrlBinding ctrlBinding) {
        ctrlBinding.livingEntityVar("slashblade_animation", it -> {
            return StringPool.EMPTY;
        });
    }
}
