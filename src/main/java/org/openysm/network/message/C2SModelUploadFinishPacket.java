/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 */
package org.openysm.network.message;

import net.minecraft.network.FriendlyByteBuf;
import rip.ysm.api.network.PacketContext;

public record C2SModelUploadFinishPacket(long uploadId) {
    public static void encode(C2SModelUploadFinishPacket message, FriendlyByteBuf buf) {
        buf.writeLong(message.uploadId);
    }

    public static C2SModelUploadFinishPacket decode(FriendlyByteBuf buf) {
        return new C2SModelUploadFinishPacket(buf.readLong());
    }

    public static void handle(C2SModelUploadFinishPacket message, PacketContext ctx) {
    }
}

