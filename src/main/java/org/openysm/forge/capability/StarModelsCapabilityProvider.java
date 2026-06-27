/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.nbt.ListTag
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.CapabilityManager
 *  net.minecraftforge.common.capabilities.CapabilityToken
 *  net.minecraftforge.common.capabilities.ICapabilitySerializable
 *  net.minecraftforge.common.util.LazyOptional
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package org.openysm.forge.capability;

import org.openysm.capability.StarModelsCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StarModelsCapabilityProvider
implements ICapabilitySerializable<ListTag> {
    public static Capability<StarModelsCapability> STAR_MODELS_CAP = CapabilityManager.get((CapabilityToken)new CapabilityToken<StarModelsCapability>(){});
    private StarModelsCapability capability = null;

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return STAR_MODELS_CAP.orEmpty(capability, LazyOptional.of(this::getOrCreateCapability));
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability) {
        return this.getCapability(capability, null);
    }

    @NotNull
    private StarModelsCapability getOrCreateCapability() {
        if (this.capability == null) {
            this.capability = new StarModelsCapability();
        }
        return this.capability;
    }

    public void deserializeNBT(ListTag listTag) {
        this.getOrCreateCapability().deserializeNBT(listTag);
    }

    public ListTag serializeNBT() {
        return this.getOrCreateCapability().serializeNBT();
    }
}

