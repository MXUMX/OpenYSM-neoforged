/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.gun.swarfare.forge;

import org.openysm.client.compat.gun.swarfare.SWarfareCompat;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class SWarfareCompatImpl {
    private SWarfareCompatImpl() {
    }

    public static boolean isLoaded() {
        return SWarfareCompat.isLoaded();
    }

    public static boolean isGunItem(ItemStack itemStack) {
        return SWarfareCompat.isGunItem(itemStack);
    }

    public static boolean isPlayerAiming(Player player) {
        return SWarfareCompat.isPlayerAiming(player);
    }

    public static void applyGunTransform(ItemStack stack, AnimatedGeoModel model, LivingEntity entity, PoseStack poseStack, int packedLightIn, float partialTicks) {
        SWarfareCompat.applyGunTransform(stack, model, entity, poseStack, packedLightIn, partialTicks);
    }

    public static PlayState handleTaczAnim(LivingEntity entity, AnimationEvent<? extends LivingAnimatable<? extends LivingEntity>> event, String str, ILoopType loopType) {
        return SWarfareCompat.handleTaczAnim(entity, event, str, loopType);
    }

    public static PlayState handleGunHoldAnim(ItemStack stack, AnimationEvent<? extends LivingAnimatable<? extends LivingEntity>> event) {
        return SWarfareCompat.handleGunHoldAnim(stack, event);
    }

    public static PlayState handleGunActionAnim(ItemStack stack, AnimationEvent<? extends LivingAnimatable<? extends LivingEntity>> event) {
        return SWarfareCompat.handleGunActionAnim(stack, event);
    }

    public static ResourceLocation getGunTexture(ItemStack stack) {
        return SWarfareCompat.getGunTexture(stack);
    }
}

