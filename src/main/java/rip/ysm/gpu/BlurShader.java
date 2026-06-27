/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderTarget
 *  com.mojang.blaze3d.platform.GlStateManager
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 */
package rip.ysm.gpu;

import org.openysm.OpenYSM;
import org.openysm.util.log.ChatLogger;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rip.ysm.gpu.ShaderUtil;

public final class BlurShader {
    private static int program = 0;
    private static int dummyVao = 0;
    private static int locProj = -1;
    private static int locRect = -1;
    private static int locScreenSize = -1;
    private static int locRectSize = -1;
    private static int locRadius = -1;
    private static int locCorner = -1;
    private static int locBlurRadius = -1;
    private static int locGamma = -1;
    private static int locTint = -1;
    private static int locMode = -1;
    private static int locPieCenter = -1;
    private static int locPieInner = -1;
    private static int locPieOuter = -1;
    private static int locPieStart = -1;
    private static int locPieEnd = -1;
    private static int locPieFeather = -1;
    private static boolean failed = false;
    private static int captureTextureId = 0;
    private static int captureWidth = 0;
    private static int captureHeight = 0;
    private static long lastCaptureFrame = -1L;

    private BlurShader() {
    }

    public static synchronized boolean ensureCompiled() {
        if (program != 0) {
            return true;
        }
        if (failed) {
            return false;
        }
        RenderSystem.assertOnRenderThreadOrInit();
        try {
            int vs = ShaderUtil.compileShaderFromResource(35633, "/blur.vsh");
            int fs = ShaderUtil.compileShaderFromResource(35632, "/blur.fsh");
            int prog = ShaderUtil.linkProgram(vs, fs);
            locProj = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_proj");
            locRect = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_rect");
            locScreenSize = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_screenSize");
            locRectSize = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_rectSize");
            locRadius = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_radius");
            locCorner = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_corner");
            locBlurRadius = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_blurRadius");
            locGamma = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_gamma");
            locTint = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_tint");
            locMode = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_mode");
            locPieCenter = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_pieCenter");
            locPieInner = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_pieInner");
            locPieOuter = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_pieOuter");
            locPieStart = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_pieStart");
            locPieEnd = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_pieEnd");
            locPieFeather = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_pieFeather");
            int locScreen = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_screen");
            GL20.glUseProgram((int)prog);
            if (locScreen >= 0) {
                GL20.glUniform1i((int)locScreen, (int)0);
            }
            GL20.glUseProgram((int)0);
            dummyVao = GL30.glGenVertexArrays();
            program = prog;
            return true;
        }
        catch (Throwable t) {
            ChatLogger.INSTANCE.logFormatted("Failed to compile shader program, please check the log", new Object[0]);
            OpenYSM.LOGGER.error("Failed to compile shader program.", t);
            failed = true;
            return false;
        }
    }

    public static int program() {
        return program;
    }

    public static int dummyVao() {
        return dummyVao;
    }

    public static int locProj() {
        return locProj;
    }

    public static int locRect() {
        return locRect;
    }

    public static int locScreenSize() {
        return locScreenSize;
    }

    public static int locRectSize() {
        return locRectSize;
    }

    public static int locRadius() {
        return locRadius;
    }

    public static int locCorner() {
        return locCorner;
    }

    public static int locBlurRadius() {
        return locBlurRadius;
    }

    public static int locGamma() {
        return locGamma;
    }

    public static int locTint() {
        return locTint;
    }

    public static int locMode() {
        return locMode;
    }

    public static int locPieCenter() {
        return locPieCenter;
    }

    public static int locPieInner() {
        return locPieInner;
    }

    public static int locPieOuter() {
        return locPieOuter;
    }

    public static int locPieStart() {
        return locPieStart;
    }

    public static int locPieEnd() {
        return locPieEnd;
    }

    public static int locPieFeather() {
        return locPieFeather;
    }

    public static int captureTextureId() {
        return captureTextureId;
    }

    public static int captureWidth() {
        return captureWidth;
    }

    public static int captureHeight() {
        return captureHeight;
    }

    public static void captureScreen(long frameKey) {
        if (frameKey == lastCaptureFrame && frameKey >= 0L) {
            return;
        }
        lastCaptureFrame = frameKey;
        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        int w = main.width;
        int h = main.height;
        BlurShader.ensureCaptureTexture(w, h);
        GL30.glBindFramebuffer((int)36008, (int)main.frameBufferId);
        GlStateManager._activeTexture((int)33984);
        GlStateManager._bindTexture((int)captureTextureId);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)w, (int)h);
        GL30.glGenerateMipmap((int)3553);
        GL30.glBindFramebuffer((int)36008, (int)main.frameBufferId);
    }

    private static void ensureCaptureTexture(int w, int h) {
        if (captureTextureId != 0 && captureWidth == w && captureHeight == h) {
            return;
        }
        if (captureTextureId != 0) {
            GL11.glDeleteTextures((int)captureTextureId);
        }
        captureTextureId = GL11.glGenTextures();
        GlStateManager._activeTexture((int)33984);
        GlStateManager._bindTexture((int)captureTextureId);
        int mipLevels = 1;
        int mw = w;
        int mh = h;
        while (mw > 1 || mh > 1) {
            mw = Math.max(1, mw / 2);
            mh = Math.max(1, mh / 2);
            ++mipLevels;
        }
        int lw = w;
        int lh = h;
        for (int i = 0; i < mipLevels; ++i) {
            GL11.glTexImage2D((int)3553, (int)i, (int)32856, (int)lw, (int)lh, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
            lw = Math.max(1, lw / 2);
            lh = Math.max(1, lh / 2);
        }
        GL11.glTexParameteri((int)3553, (int)10241, (int)9987);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL11.glTexParameteri((int)3553, (int)33084, (int)0);
        GL11.glTexParameteri((int)3553, (int)33085, (int)(mipLevels - 1));
        captureWidth = w;
        captureHeight = h;
    }
}

