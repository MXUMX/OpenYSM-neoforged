/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GL45
 *  org.lwjgl.system.MemoryUtil
 */
package rip.ysm.gpu;

import org.openysm.geckolib3.geo.render.built.GeoModel;
import com.mojang.blaze3d.platform.GlStateManager;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryUtil;

public final class GpuMesh {
    public final long pointer;
    public final int vao;
    public final int vbo;
    public final int ibo;
    public final int boneSsbo;
    public final int vertexCount;
    public final int indexCount;
    public final int boneCount;
    public final int partMask1Start;
    public final int partMask1Count;
    public final int partMask2Start;
    public final int partMask2Count;
    public final int partMask3Start;
    public final int partMask3Count;
    public final ByteBuffer perFrameBoneBuffer;
    private int xformVbo = 0;
    private int xformVao = 0;
    private boolean disposed = false;

    GpuMesh(long pointer, int vao, int vbo, int ibo, int boneSsbo, int vertexCount, int indexCount, int boneCount, int pm1s, int pm1c, int pm2s, int pm2c, int pm3s, int pm3c) {
        this.pointer = pointer;
        this.vao = vao;
        this.vbo = vbo;
        this.ibo = ibo;
        this.boneSsbo = boneSsbo;
        this.vertexCount = vertexCount;
        this.indexCount = indexCount;
        this.boneCount = boneCount;
        this.partMask1Start = pm1s;
        this.partMask1Count = pm1c;
        this.partMask2Start = pm2s;
        this.partMask2Count = pm2c;
        this.partMask3Start = pm3s;
        this.partMask3Count = pm3c;
        this.perFrameBoneBuffer = MemoryUtil.memAlloc((int)(boneCount * 144));
    }

    public int indexOffsetBytes(int renderPartMask) {
        if (renderPartMask == 0 || renderPartMask == 3) {
            return 0;
        }
        if (renderPartMask == 1) {
            return this.partMask1Start * 4;
        }
        if (renderPartMask == 2) {
            return this.partMask2Start * 4;
        }
        return 0;
    }

    public int indexDrawCount(int renderPartMask) {
        if (renderPartMask == 0) {
            return this.indexCount;
        }
        if (renderPartMask == 3) {
            return this.indexCount;
        }
        int self = renderPartMask == 1 ? this.partMask1Count : (renderPartMask == 2 ? this.partMask2Count : 0);
        return self + this.partMask3Count;
    }

    public int xformVbo() {
        return this.xformVbo;
    }

    public int xformVao() {
        return this.xformVao;
    }

    public void ensureXformBuffers() {
        if (this.xformVao != 0) {
            return;
        }
        this.xformVbo = GlStateManager._glGenBuffers();
        GlStateManager._glBindBuffer((int)34962, (int)this.xformVbo);
        GL45.glBufferData((int)34962, (long)((long)this.vertexCount * 36L), (int)35048);
        this.xformVao = GL45.glGenVertexArrays();
        GL45.glBindVertexArray((int)this.xformVao);
        GlStateManager._glBindBuffer((int)34962, (int)this.xformVbo);
        GlStateManager._glBindBuffer((int)34963, (int)this.ibo);
        GL20.glEnableVertexAttribArray((int)0);
        GL20.glVertexAttribPointer((int)0, (int)3, (int)5126, (boolean)false, (int)36, (long)0L);
        GL20.glEnableVertexAttribArray((int)1);
        GL20.glVertexAttribPointer((int)1, (int)4, (int)5121, (boolean)true, (int)36, (long)12L);
        GL20.glEnableVertexAttribArray((int)2);
        GL20.glVertexAttribPointer((int)2, (int)2, (int)5126, (boolean)false, (int)36, (long)16L);
        GL20.glEnableVertexAttribArray((int)3);
        GL30.glVertexAttribIPointer((int)3, (int)2, (int)5122, (int)36, (long)24L);
        GL20.glEnableVertexAttribArray((int)4);
        GL30.glVertexAttribIPointer((int)4, (int)2, (int)5122, (int)36, (long)28L);
        GL20.glEnableVertexAttribArray((int)5);
        GL20.glVertexAttribPointer((int)5, (int)3, (int)5120, (boolean)true, (int)36, (long)32L);
        GL45.glBindVertexArray((int)0);
        GlStateManager._glBindBuffer((int)34962, (int)0);
        GlStateManager._glBindBuffer((int)34963, (int)0);
    }

    public void dispose() {
        if (this.disposed) {
            return;
        }
        this.disposed = true;
        GlStateManager._glDeleteBuffers((int)this.vbo);
        GlStateManager._glDeleteBuffers((int)this.ibo);
        GlStateManager._glDeleteBuffers((int)this.boneSsbo);
        GL45.glDeleteVertexArrays((int)this.vao);
        if (this.xformVbo != 0) {
            GlStateManager._glDeleteBuffers((int)this.xformVbo);
        }
        if (this.xformVao != 0) {
            GL45.glDeleteVertexArrays((int)this.xformVao);
        }
        if (this.pointer != 0L) {
            GeoModel.nFreeGpuMesh(this.pointer);
        }
        MemoryUtil.memFree((Buffer)this.perFrameBoneBuffer);
    }
}

