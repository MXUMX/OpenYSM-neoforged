/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.client.player.AbstractClientPlayer
 */
package rip.ysm.compat.playeranimator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.player.AbstractClientPlayer;
import rip.ysm.compat.playeranimator.forge.PlayerAnimatorCompatImpl;

public final class PlayerAnimatorCompat {
    private PlayerAnimatorCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return PlayerAnimatorCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isPlayerAnimated(AbstractClientPlayer abstractClientPlayer) {
        return PlayerAnimatorCompatImpl.isPlayerAnimated(abstractClientPlayer);
    }
}

