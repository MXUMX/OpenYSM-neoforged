package org.openysm.client.compat.carryon;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

public class CarryOnDataHelper {
    public enum CarryType {
        ENTITY,
        BLOCK,
        PLAYER,
        NONE
    }

    public static boolean isPlayerCarrying(LivingEntity livingEntity) {
        return livingEntity.getVehicle() instanceof Player vehicle && getCarryType(vehicle) == CarryType.PLAYER;
    }

    public static CarryType getCarryType(Player player) {
        CarryOnData carryData = CarryOnDataManager.getCarryData(player);
        if (carryData.isCarrying(CarryOnData.CarryType.BLOCK)) {
            return CarryType.BLOCK;
        }
        if (carryData.isCarrying(CarryOnData.CarryType.ENTITY)) {
            return CarryType.ENTITY;
        }
        if (carryData.isCarrying(CarryOnData.CarryType.PLAYER)) {
            return CarryType.PLAYER;
        }
        return CarryType.NONE;
    }
}
