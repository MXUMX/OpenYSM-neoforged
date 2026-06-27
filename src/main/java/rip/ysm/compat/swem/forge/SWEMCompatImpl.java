/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.compat.swem.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.swem.SWEMCompat;
import net.minecraft.world.entity.LivingEntity;

public final class SWEMCompatImpl {
    private SWEMCompatImpl() {
    }

    public static boolean isLoaded() {
        return SWEMCompat.isLoaded();
    }

    public static String getHorseGaitName(LivingEntity livingEntity) {
        return SWEMCompat.getHorseGaitName(livingEntity);
    }

    public static void registerControllerFunctions(CtrlBinding ctrlBinding) {
        SWEMCompat.registerControllerFunctions(ctrlBinding);
    }
}

