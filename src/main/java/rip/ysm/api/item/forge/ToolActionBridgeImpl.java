/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.common.ItemAbilities
 */
package rip.ysm.api.item.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;

public final class ToolActionBridgeImpl {
    private ToolActionBridgeImpl() {
    }

    public static boolean canFishingRodCast(ItemStack stack) {
        return stack.canPerformAction(ItemAbilities.FISHING_ROD_CAST);
    }

    public static boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return stack.onEntitySwing(entity);
    }
}

