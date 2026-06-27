/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.resources.ResourceLocation
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL43
 */
package rip.ysm.gpu;

import org.openysm.geckolib3.geo.render.built.GeoModel;
import org.openysm.mixin.client.RenderSystemAccessor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;
import rip.ysm.gpu.BoneSkinShader;
import rip.ysm.gpu.GpuCapability;
import rip.ysm.gpu.GpuMesh;
import rip.ysm.gpu.GpuMeshBuilder;

public final class GpuRenderPath {
    private static final float[] rootPoseScratch = new float[16];
    private static final float[] rootNormalScratch = new float[9];
    private static final float[] projScratch = new float[16];
    private static final Matrix4f projMVScratch = new Matrix4f();
    private static final Vector3f[] currentLights = new Vector3f[2];
    private static final ConcurrentHashMap<Long, GpuMesh> meshMap = new ConcurrentHashMap();
    private static final AtomicLong ref = new AtomicLong(1L);
    private static final Matrix4f pivotAbsScratchMat = new Matrix4f();
    private static int[] pivotAbsPathScratch = new int[64];

    public static boolean tryRender(GeoModel model, PoseStack.Pose pose, float[] boneParams, float[] stateBuffer, int renderPartMask, int packedLight, int packedOverlay, float r, float g, float b, float a, ResourceLocation textureLocation) {
        GpuMesh mesh;
        if (!GpuCapability.isAvailable()) {
            return false;
        }
        if (!BoneSkinShader.ensureCompiled()) {
            return false;
        }
        if (model.bakedBones == null || model.bakedBones.isEmpty()) {
            return false;
        }
        if (model.gpuMeshHandle == 0L) {
            mesh = GpuMeshBuilder.build(model);
            if (mesh == null) {
                return false;
            }
            model.gpuMeshHandle = GpuRenderPath.encodeMeshRef(mesh);
        }
        if ((mesh = GpuRenderPath.decodeMeshRef(model.gpuMeshHandle)) == null) {
            return false;
        }
        Matrix4f rootPose = pose.pose();
        Matrix3f rootNormal = pose.normal();
        Matrix4f projMat = RenderSystem.getProjectionMatrix();
        Matrix4f mvMat = RenderSystem.getModelViewMatrix();
        rootPose.get(rootPoseScratch);
        rootNormal.get(rootNormalScratch);
        projMat.mul((Matrix4fc)mvMat, projMVScratch);
        projMVScratch.get(projScratch);
        ByteBuffer boneBuf = mesh.perFrameBoneBuffer;
        boneBuf.clear();
        GpuRenderPath.updatePivotAbsStateBuffer(model, boneParams, stateBuffer);
        GeoModel.nComputeBoneMatrices(mesh.pointer, rootPoseScratch, rootNormalScratch, boneParams, packedLight, boneBuf);
        boneBuf.position(0);
        boneBuf.limit(mesh.boneCount * 144);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        Minecraft mc = Minecraft.getInstance();
        AbstractTexture modelTex = mc.getTextureManager().getTexture(textureLocation);
        int modelTexId = modelTex.getId();
        GlStateManager._activeTexture((int)33986);
        mc.gameRenderer.lightTexture().turnOnLightLayer();
        GlStateManager._activeTexture((int)33985);
        mc.gameRenderer.overlayTexture().setupOverlayColor();
        GlStateManager._bindTexture((int)RenderSystem.getShaderTexture((int)1));
        GlStateManager._activeTexture((int)33984);
        GlStateManager._bindTexture((int)modelTexId);
        GL15.glBindBuffer((int)37074, (int)mesh.boneSsbo);
        GL15.glBufferSubData((int)37074, (long)0L, (ByteBuffer)boneBuf);
        GL43.glBindBufferBase((int)37074, (int)0, (int)mesh.boneSsbo);
        float fogStart = RenderSystem.getShaderFogStart();
        float fogEnd = RenderSystem.getShaderFogEnd();
        float[] fogColor = RenderSystem.getShaderFogColor();
        int fogShape = RenderSystem.getShaderFogShape().getIndex();
        GlStateManager._glUseProgram((int)BoneSkinShader.program());
        if (BoneSkinShader.locProj() >= 0) {
            GL20.glUniformMatrix4fv((int)BoneSkinShader.locProj(), (boolean)false, (float[])projScratch);
        }
        if (BoneSkinShader.locColor() >= 0) {
            GL20.glUniform4f((int)BoneSkinShader.locColor(), (float)r, (float)g, (float)b, (float)a);
        }
        if (BoneSkinShader.locOverlay() >= 0) {
            GL20.glUniform1i((int)BoneSkinShader.locOverlay(), (int)packedOverlay);
        }
        if (BoneSkinShader.locFogStart() >= 0) {
            GL20.glUniform1f((int)BoneSkinShader.locFogStart(), (float)fogStart);
        }
        if (BoneSkinShader.locFogEnd() >= 0) {
            GL20.glUniform1f((int)BoneSkinShader.locFogEnd(), (float)fogEnd);
        }
        if (BoneSkinShader.locFogColor() >= 0) {
            GL20.glUniform4f((int)BoneSkinShader.locFogColor(), (float)fogColor[0], (float)fogColor[1], (float)fogColor[2], (float)fogColor[3]);
        }
        if (BoneSkinShader.locFogShape() >= 0) {
            GL20.glUniform1i((int)BoneSkinShader.locFogShape(), (int)fogShape);
        }
        GpuRenderPath.refreshLights();
        if (BoneSkinShader.locLight0() >= 0) {
            GL20.glUniform3f((int)BoneSkinShader.locLight0(), (float)GpuRenderPath.currentLights[0].x, (float)GpuRenderPath.currentLights[0].y, (float)GpuRenderPath.currentLights[0].z);
        }
        if (BoneSkinShader.locLight1() >= 0) {
            GL20.glUniform3f((int)BoneSkinShader.locLight1(), (float)GpuRenderPath.currentLights[1].x, (float)GpuRenderPath.currentLights[1].y, (float)GpuRenderPath.currentLights[1].z);
        }
        GlStateManager._glBindVertexArray((int)mesh.vao);
        int offsetBytes = mesh.indexOffsetBytes(renderPartMask);
        int drawCount = mesh.indexDrawCount(renderPartMask);
        if (drawCount > 0) {
            if (BoneSkinShader.locAlphaMode() >= 0) {
                GL20.glUniform1i((int)BoneSkinShader.locAlphaMode(), (int)1);
            }
            GL11.glDrawElements((int)4, (int)drawCount, (int)5125, (long)offsetBytes);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (BoneSkinShader.locAlphaMode() >= 0) {
                GL20.glUniform1i((int)BoneSkinShader.locAlphaMode(), (int)2);
            }
            GL11.glDrawElements((int)4, (int)drawCount, (int)5125, (long)offsetBytes);
            RenderSystem.disableBlend();
        }
        GL43.glBindBufferBase((int)37074, (int)0, (int)0);
        GL15.glBindBuffer((int)37074, (int)0);
        GlStateManager._glUseProgram((int)0);
        BufferUploader.reset();
        GlStateManager._glBindVertexArray((int)0);
        mc.gameRenderer.lightTexture().turnOffLightLayer();
        return true;
    }

    private static void refreshLights() {
        Vector3f[] arr = RenderSystemAccessor.ysm$getShaderLightDirections();
        GpuRenderPath.currentLights[0] = arr != null && arr.length > 0 && arr[0] != null ? arr[0] : new Vector3f(0.2f, 1.0f, -0.7f).normalize();
        GpuRenderPath.currentLights[1] = arr != null && arr.length > 1 && arr[1] != null ? arr[1] : new Vector3f(-0.2f, 1.0f, 0.7f).normalize();
    }

    public static void disposeMesh(GeoModel model) {
        if (model.gpuMeshHandle == 0L) {
            return;
        }
        GpuMesh mesh = meshMap.remove(model.gpuMeshHandle);
        if (mesh != null) {
            mesh.dispose();
        }
        model.gpuMeshHandle = 0L;
    }

    public static GpuMesh getOrBuildMesh(GeoModel model) {
        if (model.gpuMeshHandle == 0L) {
            GpuMesh mesh = GpuMeshBuilder.build(model);
            if (mesh == null) {
                return null;
            }
            model.gpuMeshHandle = GpuRenderPath.encodeMeshRef(mesh);
        }
        return GpuRenderPath.decodeMeshRef(model.gpuMeshHandle);
    }

    private static long encodeMeshRef(GpuMesh mesh) {
        long ref = GpuRenderPath.ref.getAndIncrement();
        meshMap.put(ref, mesh);
        return ref;
    }

    private static GpuMesh decodeMeshRef(long ref) {
        return meshMap.get(ref);
    }

    private static void updatePivotAbsStateBuffer(GeoModel model, float[] boneParams, float[] stateBuffer) {
        int pOffset;
        if (stateBuffer == null || boneParams == null) {
            return;
        }
        if (model.bakedBones == null || model.bakedBones.isEmpty()) {
            return;
        }
        int boneCount = model.bakedBones.size();
        for (int i = 0; i < boneCount && (pOffset = i * 12) + 11 < boneParams.length; ++i) {
            int sOffset;
            float unk3 = boneParams[pOffset + 11];
            if (unk3 != 1.0f || (sOffset = i * 4) + 2 >= stateBuffer.length) continue;
            GpuRenderPath.computeOnePivotAbs(i, model.bakedBones, boneParams, stateBuffer, sOffset);
        }
    }

    private static void computeOnePivotAbs(int targetIdx, List<GeoModel.BakedBone> bones, float[] boneParams, float[] stateBuffer, int stateOffset) {
        int depth = 0;
        int idx = targetIdx;
        while (idx != -1) {
            if (depth >= pivotAbsPathScratch.length) {
                int[] newPath = new int[pivotAbsPathScratch.length * 2];
                System.arraycopy(pivotAbsPathScratch, 0, newPath, 0, pivotAbsPathScratch.length);
                pivotAbsPathScratch = newPath;
            }
            GpuRenderPath.pivotAbsPathScratch[depth++] = idx;
            idx = bones.get((int)idx).parentIdx;
        }
        Matrix4f localMat = pivotAbsScratchMat.identity();
        boolean isVisible = true;
        for (int p = depth - 1; p >= 0; --p) {
            int boneIdx = pivotAbsPathScratch[p];
            GeoModel.BakedBone bone = bones.get(boneIdx);
            int pOffset = boneIdx * 12;
            if (pOffset + 11 >= boneParams.length) {
                return;
            }
            float animRx = boneParams[pOffset];
            float animRy = boneParams[pOffset + 1];
            float animRz = boneParams[pOffset + 2];
            float animTx = boneParams[pOffset + 3];
            float animTy = boneParams[pOffset + 4];
            float animTz = boneParams[pOffset + 5];
            float animSx = boneParams[pOffset + 6];
            float animSy = boneParams[pOffset + 7];
            float animSz = boneParams[pOffset + 8];
            if (animSx == 0.0f && animSy == 0.0f && animSz == 0.0f) {
                isVisible = false;
            }
            if (!isVisible) {
                return;
            }
            localMat.translate((bone.pivotX - animTx) * 0.0625f, (bone.pivotY + animTy) * 0.0625f, (bone.pivotZ + animTz) * 0.0625f);
            localMat.rotateZ(animRz);
            localMat.rotateY(animRy);
            localMat.rotateX(animRx);
            if (animSx != 1.0f || animSy != 1.0f || animSz != 1.0f) {
                localMat.scale(animSx, animSy, animSz);
            }
            if (boneIdx == targetIdx) {
                stateBuffer[stateOffset] = -localMat.m30() * 16.0f;
                stateBuffer[stateOffset + 1] = localMat.m31() * 16.0f;
                stateBuffer[stateOffset + 2] = localMat.m32() * 16.0f;
                return;
            }
            localMat.translate(-bone.pivotX / 16.0f, -bone.pivotY / 16.0f, -bone.pivotZ / 16.0f);
        }
    }
}

