/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.cosmeticarmorreworked.forge;

import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class CosmeticArmorHelperImpl {
    private CosmeticArmorHelperImpl() {
    }

    public static ItemStack getArmorItem(LivingEntity entity, EquipmentSlot slot) {
        return CosmeticArmorHelper.getArmorItem(entity, slot);
    }

    public static ItemStack getElytraItem(LivingEntity livingEntity) {
        return CosmeticArmorHelper.getElytraItem(livingEntity);
    }
}

