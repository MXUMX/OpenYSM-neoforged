package com.elfmcys.yesstevemodel.geckolib3.geo.render.built;

import java.nio.ByteBuffer;

public final class GeoModel {
    private GeoModel() {
    }

    public static native long nInitModelCache(ByteBuffer buffer);

    public static native void nDestroyModelCache(long handle);

    public static native void nComputeModelVertices(long handle, Object vertexConsumer, float[] matrixBuffer, float[] animBuffer, int renderPartMask, int packedLight, int packedOverlay, float r, float g, float b, float a);

    public static native long nBuildGpuMesh(ByteBuffer modelBuffer, int[] metadata);

    public static native ByteBuffer nGetGpuMeshVertexBuffer(long handle);

    public static native ByteBuffer nGetGpuMeshIndexBuffer(long handle);

    public static native void nReleaseGpuMeshScratch(long handle);

    public static native void nFreeGpuMesh(long handle);

    public static native void nComputeBoneMatrices(long meshHandle, float[] rootPose, float[] rootNormal, float[] boneParams, int packedLight, ByteBuffer outBuffer);

    public static native void nComputeBoneMatricesLocal(long meshHandle, float[] boneParams, int packedLight, ByteBuffer outBuffer);
}
