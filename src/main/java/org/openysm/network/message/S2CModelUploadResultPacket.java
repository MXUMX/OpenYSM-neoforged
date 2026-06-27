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

public record S2CModelUploadResultPacket(long uploadId, byte status, String modelId, long h1, long h2, String message) {
    public static void encode(S2CModelUploadResultPacket packet, FriendlyByteBuf buf) {
        buf.writeLong(packet.uploadId);
        buf.writeByte((int)packet.status);
        buf.writeUtf(packet.modelId);
        buf.writeLong(packet.h1);
        buf.writeLong(packet.h2);
        buf.writeUtf(packet.message);
    }

    public static S2CModelUploadResultPacket decode(FriendlyByteBuf buf) {
        return new S2CModelUploadResultPacket(buf.readLong(), buf.readByte(), buf.readUtf(), buf.readLong(), buf.readLong(), buf.readUtf());
    }

    public static void handle(S2CModelUploadResultPacket packet, PacketContext ctx) {
        if (ctx.isClientSide()) {
            ctx.enqueueWork(() -> S2CModelUploadResultPacket.handleOnClient(packet));
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private static void handleOnClient(S2CModelUploadResultPacket packet) {
        ModelUploadSession.onResult(packet.uploadId, packet.status, packet.modelId, packet.h1, packet.h2, packet.message);
    }
}

