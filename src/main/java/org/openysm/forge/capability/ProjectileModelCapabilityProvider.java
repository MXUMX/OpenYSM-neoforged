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

import org.openysm.capability.ProjectileModelCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProjectileModelCapabilityProvider
implements ICapabilitySerializable<CompoundTag> {
    public static Capability<ProjectileModelCapability> PROJECTILE_MODEL = CapabilityManager.get((CapabilityToken)new CapabilityToken<ProjectileModelCapability>(){});
    private ProjectileModelCapability capability = null;

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return PROJECTILE_MODEL.orEmpty(capability, LazyOptional.of(this::getOrCreateCapability));
    }

    @NotNull
    private ProjectileModelCapability getOrCreateCapability() {
        if (this.capability == null) {
            this.capability = new ProjectileModelCapability();
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

