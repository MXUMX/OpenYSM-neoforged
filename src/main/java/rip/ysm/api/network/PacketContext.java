/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Connection
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.Nullable
 */
package rip.ysm.api.network;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface PacketContext {
    public boolean isClientSide();

    default public boolean isServerSide() {
        return !this.isClientSide();
    }

    @Nullable
    public ServerPlayer getSender();

    public Connection getConnection();

    public void enqueueWork(Runnable var1);
}

