/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.player.AbstractClientPlayer
 */
package rip.ysm.compat.playeranimator.forge;

import org.openysm.client.compat.playeranimator.PlayerAnimatorCompat;
import net.minecraft.client.player.AbstractClientPlayer;

public final class PlayerAnimatorCompatImpl {
    private PlayerAnimatorCompatImpl() {
    }

    public static boolean isLoaded() {
        return PlayerAnimatorCompat.isLoaded();
    }

    public static boolean isPlayerAnimated(AbstractClientPlayer abstractClientPlayer) {
        return PlayerAnimatorCompat.isPlayerAnimated(abstractClientPlayer);
    }
}

