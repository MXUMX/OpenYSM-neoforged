/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.Entity
 */
package rip.ysm.api.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import rip.ysm.api.entity.forge.EntityDataBridgeImpl;

public final class EntityDataBridge {
    private EntityDataBridge() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static CompoundTag getPersistentData(Entity entity) {
        return EntityDataBridgeImpl.getPersistentData(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean shouldRiderSit(Entity vehicle) {
        return EntityDataBridgeImpl.shouldRiderSit(vehicle);
    }
}

