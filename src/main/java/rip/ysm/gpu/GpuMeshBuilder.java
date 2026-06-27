/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  com.mojang.blaze3d.systems.RenderSystem
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GL45
 */
package rip.ysm.gpu;

import org.openysm.geckolib3.geo.render.built.GeoModel;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import rip.ysm.gpu.GpuMesh;

public final class GpuMeshBuilder {
    public static GpuMesh build(GeoModel model) {
        if (model.bakedBones == null || model.bakedBones.isEmpty()) {
            return null;
        }
        RenderSystem.assertOnRenderThread();
        ByteBuffer modelBuf = GpuMeshBuilder.serializeModel(model);
        int[] meta = new int[9];
        long handle = GeoModel.nBuildGpuMesh(modelBuf, meta);
        if (handle == 0L) {
            return null;
        }
        int vertexCount = meta[0];
        int indexCount = meta[1];
        int boneCount = meta[2];
        ByteBuffer vbuf = GeoModel.nGetGpuMeshVertexBuffer(handle);
        ByteBuffer ibuf = GeoModel.nGetGpuMeshIndexBuffer(handle);
        if (vbuf == null || ibuf == null) {
            GeoModel.nFreeGpuMesh(handle);
            return null;
        }
        vbuf.order(ByteOrder.nativeOrder());
        ibuf.order(ByteOrder.nativeOrder());
        int vao = GL30.glGenVertexArrays();
        int vbo = GlStateManager._glGenBuffers();
        int ibo = GlStateManager._glGenBuffers();
        int ssbo = GlStateManager._glGenBuffers();
        GL30.glBindVertexArray((int)vao);
        GlStateManager._glBindBuffer((int)34962, (int)vbo);
        GL15.glBufferData((int)34962, (ByteBuffer)vbuf, (int)35044);
        GlStateManager._glBindBuffer((int)34963, (int)ibo);
        GL15.glBufferData((int)34963, (ByteBuffer)ibuf, (int)35044);
        GL20.glEnableVertexAttribArray((int)0);
        GL20.glVertexAttribPointer((int)0, (int)3, (int)5126, (boolean)false, (int)32, (long)0L);
        GL20.glEnableVertexAttribArray((int)1);
        GL20.glVertexAttribPointer((int)1, (int)2, (int)5126, (boolean)false, (int)32, (long)12L);
        GL20.glEnableVertexAttribArray((int)2);
        GL20.glVertexAttribPointer((int)2, (int)4, (int)36255, (boolean)true, (int)32, (long)20L);
        GL20.glEnableVertexAttribArray((int)3);
        GL30.glVertexAttribIPointer((int)3, (int)1, (int)5123, (int)32, (long)24L);
        GL20.glEnableVertexAttribArray((int)4);
        GL20.glVertexAttribPointer((int)4, (int)1, (int)5121, (boolean)false, (int)32, (long)27L);
        GL30.glBindVertexArray((int)0);
        GlStateManager._glBindBuffer((int)34962, (int)0);
        GlStateManager._glBindBuffer((int)34963, (int)0);
        GlStateManager._glBindBuffer((int)37074, (int)ssbo);
        GL45.glBufferData((int)37074, (long)((long)boneCount * 144L), (int)35048);
        GlStateManager._glBindBuffer((int)37074, (int)0);
        GeoModel.nReleaseGpuMeshScratch(handle);
        return new GpuMesh(handle, vao, vbo, ibo, ssbo, vertexCount, indexCount, boneCount, meta[3], meta[4], meta[5], meta[6], meta[7], meta[8]);
    }

    private static ByteBuffer serializeModel(GeoModel model) {
        int totalBones = model.bakedBones.size();
        int totalCubes = 0;
        int totalQuads = 0;
        for (GeoModel.BakedBone bone : model.bakedBones) {
            totalCubes += bone.cubes.size();
            for (GeoModel.BakedCube cube : bone.cubes) {
                totalQuads += cube.quads.size();
            }
        }
        int sz = 4 + totalBones * 25 + totalCubes * 5 + totalQuads * 92;
        ByteBuffer buf = ByteBuffer.allocateDirect(sz).order(ByteOrder.nativeOrder());
        buf.putInt(totalBones);
        for (GeoModel.BakedBone bone : model.bakedBones) {
            buf.putInt(bone.parentIdx);
            buf.putInt(bone.partMask);
            buf.put((byte)(bone.glow ? 1 : 0));
            buf.putFloat(bone.pivotX);
            buf.putFloat(bone.pivotY);
            buf.putFloat(bone.pivotZ);
            buf.putInt(bone.cubes.size());
            for (GeoModel.BakedCube cube : bone.cubes) {
                buf.put((byte)(cube.cullable ? 1 : 0));
                buf.putInt(cube.quads.size());
                for (GeoModel.BakedQuad quad : cube.quads) {
                    int v;
                    for (v = 0; v < 4; ++v) {
                        buf.putFloat(quad.positions[v].x());
                        buf.putFloat(quad.positions[v].y());
                        buf.putFloat(quad.positions[v].z());
                    }
                    for (v = 0; v < 4; ++v) {
                        buf.putFloat(quad.uvs[v].x());
                        buf.putFloat(quad.uvs[v].y());
                    }
                    buf.putFloat(quad.normal.x());
                    buf.putFloat(quad.normal.y());
                    buf.putFloat(quad.normal.z());
                }
            }
        }
        buf.position(0);
        return buf;
    }
}

