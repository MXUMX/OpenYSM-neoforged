/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Connection
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraftforge.network.NetworkEvent$Context
 *  org.jetbrains.annotations.Nullable
 */
package rip.ysm.api.network.forge;

import java.util.function.Supplier;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import rip.ysm.api.network.PacketContext;

final class PacketContextImpl
implements PacketContext {
    private final Supplier<NetworkEvent.Context> contextSupplier;

    PacketContextImpl(Supplier<NetworkEvent.Context> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @Override
    public boolean isClientSide() {
        return this.contextSupplier.get().getDirection().getReceptionSide().isClient();
    }

    @Override
    public boolean isServerSide() {
        return this.contextSupplier.get().getDirection().getReceptionSide().isServer();
    }

    @Override
    @Nullable
    public ServerPlayer getSender() {
        return this.contextSupplier.get().getSender();
    }

    @Override
    public Connection getConnection() {
        return this.contextSupplier.get().getNetworkManager();
    }

    @Override
    public void enqueueWork(Runnable runnable) {
        this.contextSupplier.get().enqueueWork(runnable);
    }
}

