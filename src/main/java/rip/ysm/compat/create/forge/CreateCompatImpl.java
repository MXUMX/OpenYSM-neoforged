/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.compat.create.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.create.CreateCompat;
import net.minecraft.world.entity.player.Player;

public final class CreateCompatImpl {
    private CreateCompatImpl() {
    }

    public static boolean isLoaded() {
        return CreateCompat.isLoaded();
    }

    public static boolean isPlayerOnCreateContraption(Player player) {
        return CreateCompat.isPlayerOnCreateContraption(player);
    }

    public static void registerCreateFunctions(CtrlBinding binding) {
        CreateCompat.registerCreateFunctions(binding);
    }
}

