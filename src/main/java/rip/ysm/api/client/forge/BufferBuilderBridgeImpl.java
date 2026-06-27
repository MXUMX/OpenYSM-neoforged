/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.BufferBuilder
 */
package rip.ysm.api.client.forge;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.nio.ByteBuffer;

public final class BufferBuilderBridgeImpl {
    private BufferBuilderBridgeImpl() {
    }

    public static boolean putBulkData(BufferBuilder builder, ByteBuffer buffer) {
        return false;
    }

    public static boolean supportsDirectTransfer() {
        return false;
    }
}
