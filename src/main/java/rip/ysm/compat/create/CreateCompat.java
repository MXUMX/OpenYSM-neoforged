/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.compat.create;

import org.openysm.client.animation.molang.CtrlBinding;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;
import rip.ysm.compat.create.forge.CreateCompatImpl;

public final class CreateCompat {
    private CreateCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return CreateCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isPlayerOnCreateContraption(Player player) {
        return CreateCompatImpl.isPlayerOnCreateContraption(player);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerCreateFunctions(CtrlBinding binding) {
        CreateCompatImpl.registerCreateFunctions(binding);
    }
}

