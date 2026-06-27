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
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import rip.ysm.gpu.BlurShader;

public final class BlurStack {
    private static final List<Region> regions = new ArrayList<Region>();
    private static final Matrix4f mvpScratch = new Matrix4f();
    private static final float[] mvpFloats = new float[16];
    private static long frameCounter = 0L;

    private BlurStack() {
    }

    public static void pushBlur(float x, float y, float w, float h, float cornerRadius, float blurRadius) {
        BlurStack.pushBlur(x, y, w, h, cornerRadius, blurRadius, -1);
    }

    public static void pushBlur(float x, float y, float w, float h, float cornerRadius, float blurRadius, int tintRgba) {
        Region r = new Region();
        r.isPie = false;
        r.x = x;
        r.y = y;
        r.w = w;
        r.h = h;
        r.cornerRadius = cornerRadius;
        r.blurRadius = blurRadius;
        r.tintRgba = tintRgba;
        regions.add(r);
    }

    public static void pushBlurPie(float centerX, float centerY, float innerRadius, float outerRadius, float startAngle, float endAngle, float blurRadius) {
        BlurStack.pushBlurPie(centerX, centerY, innerRadius, outerRadius, startAngle, endAngle, blurRadius, -1);
    }

    public static void pushBlurPie(float centerX, float centerY, float innerRadius, float outerRadius, float startAngle, float endAngle, float blurRadius, int tintRgba) {
        float pad = 1.0f;
        Region r = new Region();
        r.isPie = true;
        r.x = centerX - outerRadius - pad;
        r.y = centerY - outerRadius - pad;
        r.w = (outerRadius + pad) * 2.0f;
        r.h = (outerRadius + pad) * 2.0f;
        r.pieCenterX = centerX;
        r.pieCenterY = centerY;
        r.pieInner = innerRadius;
        r.pieOuter = outerRadius;
        r.pieStart = startAngle;
        r.pieEnd = endAngle;
        r.blurRadius = blurRadius;
        r.tintRgba = tintRgba;
        regions.add(r);
    }

    public static void popBlur() {
        if (!regions.isEmpty()) {
            regions.remove(regions.size() - 1);
        }
    }

    public static void clear() {
        regions.clear();
    }

    public static boolean isEmpty() {
        return regions.isEmpty();
    }

    public static void flush(GuiGraphics graphics) {
        if (regions.isEmpty()) {
            return;
        }
        if (!BlurShader.ensureCompiled()) {
            regions.clear();
            return;
        }
        BlurShader.captureScreen(++frameCounter);
        RenderSystem.getProjectionMatrix().mul((Matrix4fc)RenderSystem.getModelViewMatrix(), mvpScratch);
        mvpScratch.mul((Matrix4fc)graphics.pose().last().pose());
        mvpScratch.get(mvpFloats);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        GlStateManager._activeTexture((int)33984);
        GlStateManager._bindTexture((int)BlurShader.captureTextureId());
        GlStateManager._glUseProgram((int)BlurShader.program());
        if (BlurShader.locProj() >= 0) {
            GL20.glUniformMatrix4fv((int)BlurShader.locProj(), (boolean)false, (float[])mvpFloats);
        }
        if (BlurShader.locScreenSize() >= 0) {
            GL20.glUniform2f((int)BlurShader.locScreenSize(), (float)BlurShader.captureWidth(), (float)BlurShader.captureHeight());
        }
        if (BlurShader.locGamma() >= 0) {
            GL20.glUniform1f((int)BlurShader.locGamma(), (float)6.0f);
        }
        GlStateManager._glBindVertexArray((int)BlurShader.dummyVao());
        for (Region r : regions) {
            float tr = (float)(r.tintRgba >> 16 & 0xFF) / 255.0f;
            float tg = (float)(r.tintRgba >> 8 & 0xFF) / 255.0f;
            float tb = (float)(r.tintRgba & 0xFF) / 255.0f;
            float ta = (float)(r.tintRgba >> 24 & 0xFF) / 255.0f;
            if (BlurShader.locRect() >= 0) {
                GL20.glUniform4f((int)BlurShader.locRect(), (float)r.x, (float)r.y, (float)r.w, (float)r.h);
            }
            if (BlurShader.locRectSize() >= 0) {
                GL20.glUniform2f((int)BlurShader.locRectSize(), (float)r.w, (float)r.h);
            }
            if (BlurShader.locBlurRadius() >= 0) {
                GL20.glUniform1f((int)BlurShader.locBlurRadius(), (float)Math.max(1.0f, r.blurRadius));
            }
            if (BlurShader.locTint() >= 0) {
                GL20.glUniform4f((int)BlurShader.locTint(), (float)tr, (float)tg, (float)tb, (float)ta);
            }
            if (r.isPie) {
                if (BlurShader.locMode() >= 0) {
                    GL20.glUniform1i((int)BlurShader.locMode(), (int)1);
                }
                if (BlurShader.locPieCenter() >= 0) {
                    GL20.glUniform2f((int)BlurShader.locPieCenter(), (float)r.pieCenterX, (float)r.pieCenterY);
                }
                if (BlurShader.locPieInner() >= 0) {
                    GL20.glUniform1f((int)BlurShader.locPieInner(), (float)r.pieInner);
                }
                if (BlurShader.locPieOuter() >= 0) {
                    GL20.glUniform1f((int)BlurShader.locPieOuter(), (float)r.pieOuter);
                }
                if (BlurShader.locPieStart() >= 0) {
                    GL20.glUniform1f((int)BlurShader.locPieStart(), (float)r.pieStart);
                }
                if (BlurShader.locPieEnd() >= 0) {
                    GL20.glUniform1f((int)BlurShader.locPieEnd(), (float)r.pieEnd);
                }
                if (BlurShader.locPieFeather() >= 0) {
                    GL20.glUniform1f((int)BlurShader.locPieFeather(), (float)1.0f);
                }
            } else {
                if (BlurShader.locMode() >= 0) {
                    GL20.glUniform1i((int)BlurShader.locMode(), (int)0);
                }
                if (BlurShader.locRadius() >= 0) {
                    GL20.glUniform1f((int)BlurShader.locRadius(), (float)r.cornerRadius);
                }
                if (BlurShader.locCorner() >= 0) {
                    GL20.glUniform4f((int)BlurShader.locCorner(), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
            }
            GL11.glDrawArrays((int)4, (int)0, (int)6);
        }
        GlStateManager._glUseProgram((int)0);
        BufferUploader.reset();
        GlStateManager._glBindVertexArray((int)0);
        RenderSystem.disableBlend();
        regions.clear();
    }

    private static final class Region {
        boolean isPie;
        float x;
        float y;
        float w;
        float h;
        float cornerRadius;
        float pieCenterX;
        float pieCenterY;
        float pieInner;
        float pieOuter;
        float pieStart;
        float pieEnd;
        float blurRadius;
        int tintRgba;

        private Region() {
        }
    }
}

