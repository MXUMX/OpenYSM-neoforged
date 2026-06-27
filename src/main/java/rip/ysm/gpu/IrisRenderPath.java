/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.resources.ResourceLocation
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL43
 */
package rip.ysm.gpu;

import org.openysm.geckolib3.geo.render.built.GeoModel;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;
import rip.ysm.gpu.BoneXformCompute;
import rip.ysm.gpu.GpuCapability;
import rip.ysm.gpu.GpuMesh;
import rip.ysm.gpu.GpuRenderPath;

public final class IrisRenderPath {
    private static final float[] modelViewScratch = new float[16];

    public static boolean tryRender(GeoModel model, PoseStack.Pose pose, float[] boneParams, int renderPartMask, int packedLight, int packedOverlay, float r, float g, float b, float a, ResourceLocation textureLocation) {
        if (!GpuCapability.isAvailable()) {
            return false;
        }
        if (!BoneXformCompute.ensureCompiled()) {
            return false;
        }
        if (model.bakedBones == null || model.bakedBones.isEmpty()) {
            return false;
        }
        GpuMesh mesh = GpuRenderPath.getOrBuildMesh(model);
        if (mesh == null) {
            return false;
        }
        mesh.ensureXformBuffers();
        ByteBuffer boneBuf = mesh.perFrameBoneBuffer;
        boneBuf.clear();
        GeoModel.nComputeBoneMatricesLocal(mesh.pointer, boneParams, packedLight, boneBuf);
        boneBuf.position(0);
        boneBuf.limit(mesh.boneCount * 144);
        GL15.glBindBuffer((int)37074, (int)mesh.boneSsbo);
        GL15.glBufferSubData((int)37074, (long)0L, (ByteBuffer)boneBuf);
        GlStateManager._glUseProgram((int)BoneXformCompute.program());
        if (BoneXformCompute.locColor() >= 0) {
            GL20.glUniform4f((int)BoneXformCompute.locColor(), (float)r, (float)g, (float)b, (float)a);
        }
        if (BoneXformCompute.locOverlay() >= 0) {
            GL20.glUniform1i((int)BoneXformCompute.locOverlay(), (int)packedOverlay);
        }
        if (BoneXformCompute.locModelView() >= 0) {
            pose.pose().get(modelViewScratch);
            GL20.glUniformMatrix4fv((int)BoneXformCompute.locModelView(), (boolean)false, (float[])modelViewScratch);
        }
        GL43.glBindBufferBase((int)37074, (int)0, (int)mesh.vbo);
        GL43.glBindBufferBase((int)37074, (int)1, (int)mesh.xformVbo());
        GL43.glBindBufferBase((int)37074, (int)2, (int)mesh.boneSsbo);
        GL43.glDispatchCompute((int)BoneXformCompute.dispatchGroupCount(mesh.vertexCount), (int)1, (int)1);
        GL43.glMemoryBarrier((int)3);
        GlStateManager._glUseProgram((int)0);
        GL43.glBindBufferBase((int)37074, (int)0, (int)0);
        GL43.glBindBufferBase((int)37074, (int)1, (int)0);
        GL43.glBindBufferBase((int)37074, (int)2, (int)0);
        GL15.glBindBuffer((int)37074, (int)0);
        RenderType rt = RenderType.entityTranslucent((ResourceLocation)textureLocation);
        rt.setupRenderState();
        ShaderInstance shader = RenderSystem.getShader();
        if (shader == null) {
            rt.clearRenderState();
            return false;
        }
        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set(pose.pose());
        }
        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(RenderSystem.getProjectionMatrix());
        }
        if (shader.COLOR_MODULATOR != null) {
            shader.COLOR_MODULATOR.set(1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (shader.FOG_START != null) {
            shader.FOG_START.set(1.0f);
        }
        shader.apply();
        GlStateManager._glBindVertexArray((int)mesh.xformVao());
        int offsetBytes = mesh.indexOffsetBytes(renderPartMask);
        int drawCount = mesh.indexDrawCount(renderPartMask);
        if (drawCount > 0) {
            GL11.glDrawElements((int)4, (int)drawCount, (int)5125, (long)offsetBytes);
        }
        shader.clear();
        BufferUploader.reset();
        GlStateManager._glBindVertexArray((int)0);
        rt.clearRenderState();
        return true;
    }
}
