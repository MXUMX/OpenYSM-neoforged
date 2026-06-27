/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package rip.ysm.compat.touhoulittlemaid.forge;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;

public final class MaidCapabilityBridgeImpl {
    private MaidCapabilityBridgeImpl() {
    }

    public static Optional<Object> get(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, MaidCapabilityProvider.MAID_CAP).resolve().map(c -> c);
    }
}

