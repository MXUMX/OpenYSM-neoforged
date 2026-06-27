/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.compat.swem;

import org.openysm.client.animation.molang.CtrlBinding;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import rip.ysm.compat.swem.forge.SWEMCompatImpl;

public final class SWEMCompat {
    private SWEMCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return SWEMCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static String getHorseGaitName(LivingEntity livingEntity) {
        return SWEMCompatImpl.getHorseGaitName(livingEntity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerControllerFunctions(CtrlBinding ctrlBinding) {
        SWEMCompatImpl.registerControllerFunctions(ctrlBinding);
    }
}

