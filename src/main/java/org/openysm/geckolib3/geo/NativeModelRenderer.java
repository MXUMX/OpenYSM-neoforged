package org.openysm.geckolib3.geo;

import org.openysm.NativeLibLoader;
import org.openysm.client.compat.oculus.OculusCompat;
import org.openysm.client.compat.optifine.OptiFineDetector;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.config.GeneralConfig;
import org.openysm.geckolib3.geo.render.built.*;
import org.openysm.util.log.ChatLogger;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;
import rip.ysm.gpu.GpuCapability;
import rip.ysm.gpu.GpuRenderPath;
import rip.ysm.gpu.IrisRenderPath;

public class NativeModelRenderer {
    private static final Matrix4f projectionModelViewMatrix = new Matrix4f();
    private static final float[] matrixTransferArray = new float[48];
    private static final boolean ENABLE_SIMD_RENDERER = !Boolean.getBoolean("openysm.disableSimdRenderer");

    public static void renderMesh(VertexConsumer buffer, PoseStack.Pose pose, GeoModel model, float[] boneParams, float[] stateBuffer, int textureIndex, int renderPartMask, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        renderMesh(buffer, pose, model, boneParams, stateBuffer, textureIndex, renderPartMask, packedLight, packedOverlay, red, green, blue, alpha, null);
    }

    public static void renderMesh(VertexConsumer buffer, PoseStack.Pose pose, GeoModel model, float[] boneParams, float[] stateBuffer, int textureIndex, int renderPartMask, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, ResourceLocation textureLocation) {
        OculusCompat.updatePBRState();
        boolean isCompatMode = OptiFineDetector.isOptifinePresent() || GeneralConfig.USE_COMPATIBILITY_RENDERER.get();
        RenderSystem.getProjectionMatrix().mul(RenderSystem.getModelViewMatrix(), projectionModelViewMatrix);
        boolean isPreview = ModelPreviewRenderer.isPreview() || ModelPreviewRenderer.isExtraPlayer();

        if (textureLocation != null && NativeLibLoader.isLoaded() && !GeneralConfig.USE_COMPATIBILITY_RENDERER.get() && GeneralConfig.USE_GPU_RENDERER.get()) {
            if (!GpuCapability.isAvailable()) {
                ChatLogger.INSTANCE.logFormatted("Disabled GPU renderer for: " + GpuCapability.getReason());
                GeneralConfig.USE_GPU_RENDERER.set(false);
                return;
            }
            if (OculusCompat.isShaderPackInUse() && !isPreview
                    ? IrisRenderPath.tryRender(model, pose, boneParams, renderPartMask, packedLight, packedOverlay, red, green, blue, alpha, textureLocation)
                    : GpuRenderPath.tryRender(model, pose, boneParams, stateBuffer, renderPartMask, packedLight, packedOverlay, red, green, blue, alpha, textureLocation)) {
                return;
            }
        }

        if (ENABLE_SIMD_RENDERER && NativeLibLoader.isLoaded() && !GeneralConfig.USE_COMPATIBILITY_RENDERER.get() && model.nativeModelHandle != 0) {
            nativeRenderModel(
                    buffer,
                    pose,
                    projectionModelViewMatrix,
                    isCompatMode,
                    model,
                    boneParams,
                    stateBuffer,
                    textureIndex,
                    renderPartMask,
                    packedLight,
                    packedOverlay,
                    red, green, blue, alpha,
                    isPreview
            );
        } else {
            renderModel(
                    buffer,
                    pose,
                    projectionModelViewMatrix,
                    isCompatMode,
                    model,
                    boneParams,
                    stateBuffer,
                    textureIndex,
                    renderPartMask,
                    packedLight,
                    packedOverlay,
                    red, green, blue, alpha,
                    isPreview
            );
        }
    }

    public static void renderModel(
            VertexConsumer vertexConsumer,
            PoseStack.Pose pose,
            Matrix4f projectionModelViewMatrix,
            boolean isCompatMode,
            GeoModel mesh,
            float[] boneParams,
            float[] stateBuffer,
            int textureIndex, int renderPartMask,
            int packedLight, int packedOverlay,
            float r, float g, float b, float a,
            boolean isPreview) {

        if (mesh.bakedBones == null || mesh.bakedBones.isEmpty()) return;

        // TODO: 修復GC壓力
        Matrix4f rootPoseMat = pose.pose();
        Matrix3f rootNormalMC = pose.normal();
        Matrix4f projMat = RenderSystem.getProjectionMatrix();

        Matrix4f identityMat = new Matrix4f();
        Matrix4f globalBoneMat = new Matrix4f();
        Matrix4f projBoneMat = new Matrix4f();
        Matrix3f localNormalMat = new Matrix3f();
        Matrix3f globalNormalMat = new Matrix3f();

        Vector4f p1 = new Vector4f();
        Vector4f p2 = new Vector4f();
        Vector4f p3 = new Vector4f();
        Vector4f tempPos = new Vector4f();
        Vector3f tempNorm = new Vector3f();
        Matrix4f[] boneLocalTransforms = new Matrix4f[mesh.bakedBones.size()];
        boolean[] boneVisible = new boolean[mesh.bakedBones.size()];
        boolean[] boneHidesChildren = new boolean[mesh.bakedBones.size()];

        for (int i = 0; i < mesh.bakedBones.size(); i++) {
            calculateBoneMatrix(i, mesh.bakedBones, boneParams, boneLocalTransforms, boneVisible, boneHidesChildren, identityMat, stateBuffer);
        }

        for (int i = 0; i < mesh.bakedBones.size(); i++) {
            // 如果骨骼被標記為不可見直接跳過當前骨骼
            if (!boneVisible[i]) {
                continue;
            }

            GeoModel.BakedBone bone = mesh.bakedBones.get(i);
            if (renderPartMask != 0 && bone.partMask != renderPartMask && bone.partMask != 3) {
                continue;
            }

            Matrix4f localBoneMat = boneLocalTransforms[i];
            globalBoneMat.set(rootPoseMat).mul(localBoneMat);
            projBoneMat.set(projMat).mul(globalBoneMat);

            // 法線全域矩陣
            localBoneMat.normal(localNormalMat);
            globalNormalMat.set(rootNormalMC).mul(localNormalMat);

            int currentPackedLight = bone.glow ? LightTexture.pack(15, 15) : packedLight;

            for (GeoModel.BakedCube cube : bone.cubes) {
                for (GeoModel.BakedQuad quad : cube.quads) {
                    p1.set(quad.positions[0].x(), quad.positions[0].y(), quad.positions[0].z(), 1.0f).mul(projBoneMat);
                    p2.set(quad.positions[1].x(), quad.positions[1].y(), quad.positions[1].z(), 1.0f).mul(projBoneMat);
                    p3.set(quad.positions[2].x(), quad.positions[2].y(), quad.positions[2].z(), 1.0f).mul(projBoneMat);
                    float det = p1.x() * (p2.y() * p3.w() - p3.y() * p2.w()) - p2.x() * (p1.y() * p3.w() - p3.y() * p1.w()) + p3.x() * (p1.y() * p2.w() - p2.y() * p1.w());
                    if (det <= 0.0f && cube.cullable) {
                        continue;
                    }
                    tempNorm.set(quad.normal).mul(globalNormalMat).normalize();
                    for (int v = 0; v < 4; v++) {
                        tempPos.set(quad.positions[v].x(), quad.positions[v].y(), quad.positions[v].z(), 1.0f).mul(globalBoneMat);
                        vertexConsumer.addVertex(tempPos.x(), tempPos.y(), tempPos.z())
                                .setColor(r, g, b, a)
                                .setUv(quad.uvs[v].x(), quad.uvs[v].y())
                                .setOverlay(packedOverlay)
                                .setLight(currentPackedLight)
                                .setNormal(tempNorm.x(), tempNorm.y(), tempNorm.z());
                    }
                }
            }
        }
    }

    private static Matrix4f calculateBoneMatrix(int idx, java.util.List<GeoModel.BakedBone> bones, float[] boneParams, Matrix4f[] cache, boolean[] visibleCache, boolean[] hidesChildrenCache, Matrix4f rootPose, float[] stateBuffer) {
        if (cache[idx] != null) return cache[idx];

        GeoModel.BakedBone bone = bones.get(idx);
        Matrix4f parentMatrix = rootPose;
        boolean isVisible = true;
        boolean inheritedHidden = false;

        if (bone.parentIdx != -1) {
            parentMatrix = calculateBoneMatrix(bone.parentIdx, bones, boneParams, cache, visibleCache, hidesChildrenCache, rootPose, stateBuffer);
            if (hidesChildrenCache[bone.parentIdx]) {
                isVisible = false;
                inheritedHidden = true;
            }
        }

        Matrix4f localMat = new Matrix4f(parentMatrix);

        int pOffset = idx * 12;
        float animRx = boneParams[pOffset];
        float animRy = boneParams[pOffset + 1];
        float animRz = boneParams[pOffset + 2];
        float animTx = boneParams[pOffset + 3];
        float animTy = boneParams[pOffset + 4];
        float animTz = boneParams[pOffset + 5];
        float animSx = boneParams[pOffset + 6];
        float animSy = boneParams[pOffset + 7];
        float animSz = boneParams[pOffset + 8];

        boolean selfHidden = boneParams[pOffset + 9] == 1.0f;
        boolean hideChildren = boneParams[pOffset + 10] == 1.0f;
        float unk3 = boneParams[pOffset + 11];

        boolean scaleHidden = animSx == 0.0f && animSy == 0.0f && animSz == 0.0f;
        if (selfHidden || scaleHidden) {
            isVisible = false;
        }

        localMat.translate(
                (bone.pivotX - animTx) * 0.0625f,
                (bone.pivotY + animTy) * 0.0625f,
                (bone.pivotZ + animTz) * 0.0625f
        );
        localMat.rotateZ(animRz);
        localMat.rotateY(animRy);
        localMat.rotateX(animRx);

        if (bone.name.equals("gun")) {
            //"".hashCode();
        }

        if (animSx != 1.0f || animSy != 1.0f || animSz != 1.0f) {
            localMat.scale(animSx, animSy, animSz);
        }

        if (unk3 == 1.0f && stateBuffer != null && isVisible) {
            int offset = idx * 4;
            if (offset + 2 < stateBuffer.length) {
                stateBuffer[offset] = -localMat.m30() * 16.0f;
                stateBuffer[offset + 1] = localMat.m31() * 16.0f;
                stateBuffer[offset + 2] = localMat.m32() * 16.0f;
            }
        }

        localMat.translate(-bone.pivotX / 16f, -bone.pivotY / 16f, -bone.pivotZ / 16f);

        cache[idx] = localMat;
        visibleCache[idx] = isVisible; // 保存當前骨骼的可見性
        hidesChildrenCache[idx] = inheritedHidden || scaleHidden || (selfHidden && hideChildren);
        return localMat;
    }

    public static void nativeRenderModel(
            VertexConsumer vertexConsumer,
            PoseStack.Pose pose,
            Matrix4f projectionModelViewMatrix,
            boolean isCompatMode,
            GeoModel mesh,
            float[] boneVertex,
            float[] stateBuffer,
            int textureIndex, int renderPartMask,
            int packedLight, int packedOverlay,
            float r, float g, float b, float a,
            boolean isPreview) {

        if (mesh.nativeModelHandle == 0) return;

        Matrix4f projMat = RenderSystem.getProjectionMatrix();
        pose.pose().get(matrixTransferArray, 0);
        pose.normal().get(matrixTransferArray, 16);
        projMat.get(matrixTransferArray, 32);
        GeoModel.nComputeModelVertices(mesh.nativeModelHandle, vertexConsumer, matrixTransferArray, boneVertex, renderPartMask, packedLight, packedOverlay, r, g, b, a);
    }
}
