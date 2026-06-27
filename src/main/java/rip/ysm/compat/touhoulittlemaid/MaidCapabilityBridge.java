/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.Entity
 */
package rip.ysm.compat.touhoulittlemaid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import rip.ysm.compat.touhoulittlemaid.forge.MaidCapabilityBridgeImpl;

public final class MaidCapabilityBridge {
    private MaidCapabilityBridge() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Optional<Object> get(Entity entity) {
        return MaidCapabilityBridgeImpl.get(entity);
    }
}

