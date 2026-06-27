/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.player.Player
 *  org.apache.commons.lang3.tuple.Pair
 */
package rip.ysm.compat.parcool;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.controller.IAnimationController;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.tuple.Pair;
import rip.ysm.compat.parcool.forge.ParcoolCompatImpl;

public final class ParcoolCompat {
    private ParcoolCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return ParcoolCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Optional<Pair<String, String>> getInCompatibleInfo() {
        return ParcoolCompatImpl.getInCompatibleInfo();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Optional<BiFunction<String, CustomPlayerEntity, IAnimationController<CustomPlayerEntity>>> getControllerFactory() {
        return ParcoolCompatImpl.getControllerFactory();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isPlayerParcooling(Player player) {
        return ParcoolCompatImpl.isPlayerParcooling(player);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static String getActionName(Player player) {
        return ParcoolCompatImpl.getActionName(player);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerBindings(CtrlBinding binding) {
        ParcoolCompatImpl.registerBindings(binding);
    }
}

