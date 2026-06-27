/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package org.openysm.network.message;

import org.openysm.client.upload.ModelUploadSession;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rip.ysm.api.network.PacketContext;

public record S2CModelUploadStartPacket(long uploadId, byte status, int chunkSize, int maxTotalBytes, int chunksPerTick, String message) {
    public static void encode(S2CModelUploadStartPacket packet, FriendlyByteBuf buf) {
        buf.writeLong(packet.uploadId);
        buf.writeByte((int)packet.status);
        buf.writeVarInt(packet.chunkSize);
        buf.writeVarInt(packet.maxTotalBytes);
        buf.writeVarInt(packet.chunksPerTick);
        buf.writeUtf(packet.message);
    }

    public static S2CModelUploadStartPacket decode(FriendlyByteBuf buf) {
        long uploadId = buf.readLong();
        byte status = buf.readByte();
        int chunkSize = buf.readVarInt();
        int maxTotalBytes = buf.readVarInt();
        int chunksPerTick = buf.readVarInt();
        String message = buf.readUtf();
        return new S2CModelUploadStartPacket(uploadId, status, chunkSize, maxTotalBytes, chunksPerTick, message);
    }

    public static void handle(S2CModelUploadStartPacket packet, PacketContext ctx) {
        if (ctx.isClientSide()) {
            ctx.enqueueWork(() -> S2CModelUploadStartPacket.handleOnClient(packet));
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private static void handleOnClient(S2CModelUploadStartPacket packet) {
        ModelUploadSession.onStartAck(packet.uploadId, packet.status, packet.chunkSize, packet.maxTotalBytes, packet.chunksPerTick, packet.message);
    }
}

