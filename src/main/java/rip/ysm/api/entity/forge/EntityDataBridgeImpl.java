/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.Entity
 */
package rip.ysm.api.entity.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public final class EntityDataBridgeImpl {
    private EntityDataBridgeImpl() {
    }

    public static CompoundTag getPersistentData(Entity entity) {
        return entity.getPersistentData();
    }

    public static boolean shouldRiderSit(Entity vehicle) {
        return vehicle.shouldRiderSit();
    }
}

