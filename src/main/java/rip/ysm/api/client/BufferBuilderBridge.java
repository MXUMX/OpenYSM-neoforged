/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.api.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.nio.ByteBuffer;
import rip.ysm.api.client.forge.BufferBuilderBridgeImpl;

public final class BufferBuilderBridge {
    private BufferBuilderBridge() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean putBulkData(BufferBuilder builder, ByteBuffer buffer) {
        return BufferBuilderBridgeImpl.putBulkData(builder, buffer);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean supportsDirectTransfer() {
        return BufferBuilderBridgeImpl.supportsDirectTransfer();
    }
}
