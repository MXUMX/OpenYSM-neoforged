/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.Entity
 */
package rip.ysm.api.capability;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import rip.ysm.api.capability.forge.CapabilityLifecycleImpl;

public final class CapabilityLifecycle {
    private CapabilityLifecycle() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void revive(Entity entity) {
        CapabilityLifecycleImpl.revive(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void invalidate(Entity entity) {
        CapabilityLifecycleImpl.invalidate(entity);
    }
}

