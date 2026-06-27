/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.api.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.api.item.forge.ToolActionBridgeImpl;

public final class ToolActionBridge {
    private ToolActionBridge() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean canFishingRodCast(ItemStack stack) {
        return ToolActionBridgeImpl.canFishingRodCast(stack);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return ToolActionBridgeImpl.onEntitySwing(stack, entity);
    }
}
