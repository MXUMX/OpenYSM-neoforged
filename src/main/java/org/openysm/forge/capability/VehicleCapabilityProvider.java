/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.CapabilityManager
 *  net.minecraftforge.common.capabilities.CapabilityToken
 *  net.minecraftforge.common.capabilities.ICapabilityProvider
 *  net.minecraftforge.common.util.LazyOptional
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package org.openysm.forge.capability;

import org.openysm.capability.VehicleCapability;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(value=Dist.CLIENT)
public class VehicleCapabilityProvider
implements ICapabilityProvider {
    public static Capability<VehicleCapability> VEHICLE_CAP = CapabilityManager.get((CapabilityToken)new CapabilityToken<VehicleCapability>(){});
    private VehicleCapability capability;
    private Entity entity;

    public VehicleCapabilityProvider(Entity entity) {
        this.entity = entity;
    }

    public VehicleCapability getOrCreateCapability() {
        if (this.capability == null) {
            this.capability = new VehicleCapability(this.entity);
            this.entity = null;
        }
        return this.capability;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return VEHICLE_CAP.orEmpty(capability, LazyOptional.of(this::getOrCreateCapability));
    }
}

