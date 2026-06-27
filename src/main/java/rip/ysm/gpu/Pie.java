/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  net.minecraft.client.gui.GuiGraphics
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package rip.ysm.gpu;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import rip.ysm.gpu.PieShader;

public final class Pie {
    public static final float tau = (float)Math.PI * 2;
    private static final Matrix4f mvpScratch = new Matrix4f();
    private static final float[] mvpFloats = new float[16];

    public static void draw(GuiGraphics graphics, float centerX, float centerY, float innerRadius, float outerRadius, float startAngle, float endAngle, int rgba) {
        Pie.draw(graphics, centerX, centerY, innerRadius, outerRadius, startAngle, endAngle, rgba, 1.0f);
    }

    public static void draw(GuiGraphics graphics, float centerX, float centerY, float innerRadius, float outerRadius, float startAngle, float endAngle, int rgba, float feather) {
        if (!PieShader.ensureCompiled()) {
            return;
        }
        float pad = feather + 1.0f;
        float rectX = centerX - outerRadius - pad;
        float rectY = centerY - outerRadius - pad;
        float rectW = (outerRadius + pad) * 2.0f;
        float rectH = (outerRadius + pad) * 2.0f;
        RenderSystem.getProjectionMatrix().mul((Matrix4fc)RenderSystem.getModelViewMatrix(), mvpScratch);
        mvpScratch.mul((Matrix4fc)graphics.pose().last().pose());
        mvpScratch.get(mvpFloats);
        float cr = (float)(rgba >> 16 & 0xFF) / 255.0f;
        float cg = (float)(rgba >> 8 & 0xFF) / 255.0f;
        float cb = (float)(rgba & 0xFF) / 255.0f;
        float ca = (float)(rgba >> 24 & 0xFF) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        GlStateManager._glUseProgram((int)PieShader.program());
        if (PieShader.locProj() >= 0) {
            GL20.glUniformMatrix4fv((int)PieShader.locProj(), (boolean)false, (float[])mvpFloats);
        }
        if (PieShader.locRect() >= 0) {
            GL20.glUniform4f((int)PieShader.locRect(), (float)rectX, (float)rectY, (float)rectW, (float)rectH);
        }
        if (PieShader.locCenter() >= 0) {
            GL20.glUniform2f((int)PieShader.locCenter(), (float)centerX, (float)centerY);
        }
        if (PieShader.locOuterRadius() >= 0) {
            GL20.glUniform1f((int)PieShader.locOuterRadius(), (float)outerRadius);
        }
        if (PieShader.locInnerRadius() >= 0) {
            GL20.glUniform1f((int)PieShader.locInnerRadius(), (float)Math.max(0.0f, innerRadius));
        }
        if (PieShader.locStartAngle() >= 0) {
            GL20.glUniform1f((int)PieShader.locStartAngle(), (float)startAngle);
        }
        if (PieShader.locEndAngle() >= 0) {
            GL20.glUniform1f((int)PieShader.locEndAngle(), (float)endAngle);
        }
        if (PieShader.locColor() >= 0) {
            GL20.glUniform4f((int)PieShader.locColor(), (float)cr, (float)cg, (float)cb, (float)ca);
        }
        if (PieShader.locFeather() >= 0) {
            GL20.glUniform1f((int)PieShader.locFeather(), (float)feather);
        }
        GlStateManager._glBindVertexArray((int)PieShader.dummyVao());
        GL11.glDrawArrays((int)4, (int)0, (int)6);
        GlStateManager._glUseProgram((int)0);
        BufferUploader.reset();
        GlStateManager._glBindVertexArray((int)0);
        RenderSystem.disableBlend();
    }
}

