package com.elfmcys.yesstevemodel.geckolib3.geo;

import com.mojang.blaze3d.vertex.VertexConsumer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class NativeModelRenderer {
    private NativeModelRenderer() {
    }

    public static void submitVertices(Object v, int vertexCount, ByteBuffer fBuf, ByteBuffer iBuf) {
        FloatBuffer floats = fBuf.order(ByteOrder.nativeOrder()).asFloatBuffer();
        IntBuffer ints = iBuf.order(ByteOrder.nativeOrder()).asIntBuffer();
        VertexConsumer vertexConsumer = (VertexConsumer) v;
        int fIdx = 0;
        int iIdx = 0;
        for (int n = 0; n < vertexCount; n++) {
            int color = packColor(floats.get(fIdx + 3), floats.get(fIdx + 4), floats.get(fIdx + 5), floats.get(fIdx + 6));
            vertexConsumer.addVertex(
                    floats.get(fIdx), floats.get(fIdx + 1), floats.get(fIdx + 2),
                    color,
                    floats.get(fIdx + 7), floats.get(fIdx + 8),
                    ints.get(iIdx), ints.get(iIdx + 1),
                    floats.get(fIdx + 9), floats.get(fIdx + 10), floats.get(fIdx + 11)
            );
            fIdx += 12;
            iIdx += 2;
        }
    }

    private static int packColor(float r, float g, float b, float a) {
        int ri = (int) (r * 255.0f) & 0xFF;
        int gi = (int) (g * 255.0f) & 0xFF;
        int bi = (int) (b * 255.0f) & 0xFF;
        int ai = (int) (a * 255.0f) & 0xFF;
        return ai << 24 | ri << 16 | gi << 8 | bi;
    }
}
