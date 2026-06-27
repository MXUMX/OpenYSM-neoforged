/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.compat.carryon.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import rip.ysm.compat.carryon.CarryOnDataHelper;

public final class CarryOnDataHelperImpl {
    private CarryOnDataHelperImpl() {
    }

    public static boolean isPlayerCarrying(LivingEntity livingEntity) {
        return org.openysm.client.compat.carryon.CarryOnDataHelper.isPlayerCarrying(livingEntity);
    }

    public static CarryOnDataHelper.CarryType getCarryType(Player player) {
        org.openysm.client.compat.carryon.CarryOnDataHelper.CarryType raw = org.openysm.client.compat.carryon.CarryOnDataHelper.getCarryType(player);
        return CarryOnDataHelper.CarryType.valueOf(raw.name());
    }
}
