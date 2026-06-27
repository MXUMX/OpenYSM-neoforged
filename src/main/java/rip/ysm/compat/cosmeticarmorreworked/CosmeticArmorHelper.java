/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.cosmeticarmorreworked;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.compat.cosmeticarmorreworked.forge.CosmeticArmorHelperImpl;

public final class CosmeticArmorHelper {
    private CosmeticArmorHelper() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static ItemStack getArmorItem(LivingEntity entity, EquipmentSlot slot) {
        return CosmeticArmorHelperImpl.getArmorItem(entity, slot);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static ItemStack getElytraItem(LivingEntity livingEntity) {
        return CosmeticArmorHelperImpl.getElytraItem(livingEntity);
    }
}
