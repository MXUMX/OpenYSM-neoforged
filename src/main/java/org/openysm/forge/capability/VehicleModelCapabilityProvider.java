/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.CapabilityManager
 *  net.minecraftforge.common.capabilities.CapabilityToken
 *  net.minecraftforge.common.capabilities.ICapabilitySerializable
 *  net.minecraftforge.common.util.LazyOptional
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package org.openysm.forge.capability;

import org.openysm.capability.VehicleModelCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleModelCapabilityProvider
implements ICapabilitySerializable<CompoundTag> {
    public static Capability<VehicleModelCapability> VEHICLE_MODEL_CAP = CapabilityManager.get((CapabilityToken)new CapabilityToken<VehicleModelCapability>(){});
    private VehicleModelCapability capability = null;

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return VEHICLE_MODEL_CAP.orEmpty(capability, LazyOptional.of(this::getOrCreateCapability));
    }

    @NotNull
    private VehicleModelCapability getOrCreateCapability() {
        if (this.capability == null) {
            this.capability = new VehicleModelCapability();
        }
        return this.capability;
    }

    public CompoundTag serializeNBT() {
        return this.getOrCreateCapability().serializeNBT();
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        this.getOrCreateCapability().deserializeNBT(compoundTag);
    }
}

