/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.compat.carryon;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import rip.ysm.compat.carryon.forge.CarryOnDataHelperImpl;

public final class CarryOnDataHelper {
    private CarryOnDataHelper() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isPlayerCarrying(LivingEntity livingEntity) {
        return CarryOnDataHelperImpl.isPlayerCarrying(livingEntity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static CarryType getCarryType(Player player) {
        return CarryOnDataHelperImpl.getCarryType(player);
    }

    public static enum CarryType {
        ENTITY,
        BLOCK,
        PLAYER,
        NONE;

    }
}

