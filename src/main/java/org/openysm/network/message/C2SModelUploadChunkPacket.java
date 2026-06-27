/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 */
package org.openysm.network.message;

import net.minecraft.network.FriendlyByteBuf;
import rip.ysm.api.network.PacketContext;

public record C2SModelUploadChunkPacket(long uploadId, int offset, byte[] data) {
    public static void encode(C2SModelUploadChunkPacket message, FriendlyByteBuf buf) {
        buf.writeLong(message.uploadId);
        buf.writeVarInt(message.offset);
        buf.writeByteArray(message.data);
    }

    public static C2SModelUploadChunkPacket decode(FriendlyByteBuf buf) {
        return new C2SModelUploadChunkPacket(buf.readLong(), buf.readVarInt(), buf.readByteArray());
    }

    public static void handle(C2SModelUploadChunkPacket message, PacketContext ctx) {
    }
}

