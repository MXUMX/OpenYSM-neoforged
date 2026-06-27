/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.player.Player
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

import org.openysm.capability.PlayerCapability;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
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
public class PlayerCapabilityProvider
implements ICapabilityProvider {
    public static Capability<PlayerCapability> PLAYER_CAP = CapabilityManager.get((CapabilityToken)new CapabilityToken<PlayerCapability>(){});
    private PlayerCapability capability;
    private AbstractClientPlayer player;

    public PlayerCapabilityProvider(AbstractClientPlayer abstractClientPlayer) {
        this.player = abstractClientPlayer;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return this.getCapability(capability);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability) {
        return PLAYER_CAP.orEmpty(capability, LazyOptional.of(this::getOrCreateCapability));
    }

    @NotNull
    private PlayerCapability getOrCreateCapability() {
        if (this.capability == null) {
            this.capability = new PlayerCapability((Player)this.player);
            this.player = null;
        }
        return this.capability;
    }
}

