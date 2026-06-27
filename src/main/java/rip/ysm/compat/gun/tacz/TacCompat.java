/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.gun.tacz;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.compat.gun.tacz.forge.TacCompatImpl;

public final class TacCompat {
    private TacCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return TacCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerControllerFunctions(CtrlBinding binding) {
        TacCompatImpl.registerControllerFunctions(binding);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void applyItemTransform(ItemStack stack, AnimatedGeoModel model, LivingEntity entity, PoseStack poseStack, int packedLightIn, float partialTicks) {
        TacCompatImpl.applyItemTransform(stack, model, entity, poseStack, packedLightIn, partialTicks);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static PlayState handleTaczAnimState(LivingEntity entity, AnimationEvent<? extends LivingAnimatable<?>> event, String animation, ILoopType loopType) {
        return TacCompatImpl.handleTaczAnimState(entity, event, animation, loopType);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static PlayState handleGunHoldAnimState(ItemStack stack, AnimationEvent<? extends LivingAnimatable<?>> event) {
        return TacCompatImpl.handleGunHoldAnimState(stack, event);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static PlayState handleGunActionAnimState(ItemStack stack, AnimationEvent<? extends LivingAnimatable<?>> event) {
        return TacCompatImpl.handleGunActionAnimState(stack, event);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void handleGunSound(LivingEntity entity, ItemStack stack) {
        TacCompatImpl.handleGunSound(entity, stack);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void handleItemSound(ItemStack stack) {
        TacCompatImpl.handleItemSound(stack);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static ResourceLocation getGunTexture(ItemStack stack) {
        return TacCompatImpl.getGunTexture(stack);
    }
}
