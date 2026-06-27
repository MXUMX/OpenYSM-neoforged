/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.compat.immersivemelodies;

import org.openysm.client.animation.molang.CtrlBinding;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import rip.ysm.compat.immersivemelodies.forge.ImmersiveMelodiesCompatImpl;

public final class ImmersiveMelodiesCompat {
    private ImmersiveMelodiesCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return ImmersiveMelodiesCompatImpl.isLoaded();
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void updateMelodyProgress(LivingEntity livingEntity, ImmersiveMelodiesData imData) {
        ImmersiveMelodiesCompatImpl.updateMelodyProgress(livingEntity, imData);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerBindings(CtrlBinding binding) {
        ImmersiveMelodiesCompatImpl.registerBindings(binding);
    }

    public static final class ImmersiveMelodiesData {
        public float pitch = 0.0f;
        public float volume = 0.0f;
        public float current = 0.0f;
        public long delta = 0L;
        public long time = 0L;
    }
}
