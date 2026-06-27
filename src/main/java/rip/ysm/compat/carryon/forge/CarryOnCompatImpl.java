/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.compat.carryon.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.carryon.CarryOnCompat;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.controller.IAnimationController;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.world.entity.player.Player;

public final class CarryOnCompatImpl {
    private CarryOnCompatImpl() {
    }

    public static boolean isLoaded() {
        return CarryOnCompat.isLoaded();
    }

    public static Optional<BiFunction<String, CustomPlayerEntity, IAnimationController<CustomPlayerEntity>>> getControllerFactory() {
        return CarryOnCompat.getControllerFactory();
    }

    public static boolean isPlayerCarrying(Player player) {
        return CarryOnCompat.isPlayerCarrying(player);
    }

    public static void registerBindings(CtrlBinding binding) {
        CarryOnCompat.registerBindings(binding);
    }
}

