/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.compat.immersivemelodies.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import net.minecraft.world.entity.LivingEntity;
import rip.ysm.compat.immersivemelodies.ImmersiveMelodiesCompat;

public final class ImmersiveMelodiesCompatImpl {
    private ImmersiveMelodiesCompatImpl() {
    }

    public static boolean isLoaded() {
        return org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat.isLoaded();
    }

    public static void updateMelodyProgress(LivingEntity livingEntity, ImmersiveMelodiesCompat.ImmersiveMelodiesData imData) {
        org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat.ImmersiveMelodiesData raw = new org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat.ImmersiveMelodiesData();
        raw.pitch = imData.pitch;
        raw.volume = imData.volume;
        raw.current = imData.current;
        raw.delta = imData.delta;
        raw.time = imData.time;
        org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat.updateMelodyProgress(livingEntity, raw);
        imData.pitch = raw.pitch;
        imData.volume = raw.volume;
        imData.current = raw.current;
        imData.delta = raw.delta;
        imData.time = raw.time;
    }

    public static void registerBindings(CtrlBinding binding) {
        org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat.registerBindings(binding);
    }
}
