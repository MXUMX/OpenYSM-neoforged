/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  org.apache.commons.lang3.tuple.Pair
 */
package rip.ysm.compat.parcool.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.parcool.ParcoolCompat;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.core.controller.IAnimationController;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.tuple.Pair;

public final class ParcoolCompatImpl {
    private ParcoolCompatImpl() {
    }

    public static boolean isLoaded() {
        return ParcoolCompat.isLoaded();
    }

    public static Optional<Pair<String, String>> getInCompatibleInfo() {
        return ParcoolCompat.getInCompatibleInfo();
    }

    public static Optional<BiFunction<String, CustomPlayerEntity, IAnimationController<CustomPlayerEntity>>> getControllerFactory() {
        return ParcoolCompat.getControllerFactory();
    }

    public static boolean isPlayerParcooling(Player player) {
        return ParcoolCompat.isPlayerParcooling(player);
    }

    public static String getActionName(Player player) {
        return ParcoolCompat.getActionName(player);
    }

    public static void registerBindings(CtrlBinding binding) {
        ParcoolCompat.registerBindings(binding);
    }
}

