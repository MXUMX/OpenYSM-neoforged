/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.elytraslot.forge;

import org.openysm.client.compat.elytraslot.ElytraSlotCompat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class ElytraSlotCompatImpl {
    private ElytraSlotCompatImpl() {
    }

    public static boolean isLoaded() {
        return ElytraSlotCompat.isLoaded();
    }

    public static ItemStack getElytraItem(LivingEntity livingEntity) {
        return ElytraSlotCompat.getElytraItem(livingEntity);
    }
}

