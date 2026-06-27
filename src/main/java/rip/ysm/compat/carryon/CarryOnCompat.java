/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.compat.carryon;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.controller.IAnimationController;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.world.entity.player.Player;
import rip.ysm.compat.carryon.forge.CarryOnCompatImpl;

public final class CarryOnCompat {
    private CarryOnCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return CarryOnCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Optional<BiFunction<String, CustomPlayerEntity, IAnimationController<CustomPlayerEntity>>> getControllerFactory() {
        return CarryOnCompatImpl.getControllerFactory();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isPlayerCarrying(Player player) {
        return CarryOnCompatImpl.isPlayerCarrying(player);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerBindings(CtrlBinding binding) {
        CarryOnCompatImpl.registerBindings(binding);
    }
}

