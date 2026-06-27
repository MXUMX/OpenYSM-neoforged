/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL43
 */
package rip.ysm.gpu;

import org.openysm.OpenYSM;
import org.openysm.util.log.ChatLogger;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;
import rip.ysm.gpu.ShaderUtil;

public final class BoneSkinShader {
    public static final int ssbo = 0;
    private static int program = 0;
    private static int locProj = -1;
    private static int locColor = -1;
    private static int locOverlay = -1;
    private static int locFogStart = -1;
    private static int locFogEnd = -1;
    private static int locFogColor = -1;
    private static int locFogShape = -1;
    private static int locLight0 = -1;
    private static int locLight1 = -1;
    private static int locAlphaMode = -1;
    private static boolean failed = false;

    public static synchronized boolean ensureCompiled() {
        if (program != 0) {
            return true;
        }
        if (failed) {
            return false;
        }
        RenderSystem.assertOnRenderThreadOrInit();
        try {
            int vs = ShaderUtil.compileShaderFromResource(35633, "/bone_skin.vsh");
            int fs = ShaderUtil.compileShaderFromResource(35632, "/bone_skin.fsh");
            int prog = ShaderUtil.linkProgramWith(p -> {
                GL20.glBindAttribLocation((int)p, (int)0, (CharSequence)"a_position");
                GL20.glBindAttribLocation((int)p, (int)1, (CharSequence)"a_uv");
                GL20.glBindAttribLocation((int)p, (int)2, (CharSequence)"a_normal");
                GL20.glBindAttribLocation((int)p, (int)3, (CharSequence)"a_boneId");
                GL20.glBindAttribLocation((int)p, (int)4, (CharSequence)"a_cullable");
            }, vs, fs);
            int ssboBlock = GL43.glGetProgramResourceIndex((int)prog, (int)37606, (CharSequence)"BoneBlock");
            if (ssboBlock != -1) {
                GL43.glShaderStorageBlockBinding((int)prog, (int)ssboBlock, (int)0);
            }
            locProj = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_proj");
            locColor = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_color");
            locOverlay = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_packedOverlay");
            locFogStart = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_fogStart");
            locFogEnd = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_fogEnd");
            locFogColor = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_fogColor");
            locFogShape = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_fogShape");
            locLight0 = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_light0");
            locLight1 = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_light1");
            locAlphaMode = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_alphaMode");
            int locSampler0 = GL20.glGetUniformLocation((int)prog, (CharSequence)"Sampler0");
            int locSampler1 = GL20.glGetUniformLocation((int)prog, (CharSequence)"Sampler1");
            int locSampler2 = GL20.glGetUniformLocation((int)prog, (CharSequence)"Sampler2");
            GL20.glUseProgram((int)prog);
            if (locSampler0 >= 0) {
                GL20.glUniform1i((int)locSampler0, (int)0);
            }
            if (locSampler1 >= 0) {
                GL20.glUniform1i((int)locSampler1, (int)1);
            }
            if (locSampler2 >= 0) {
                GL20.glUniform1i((int)locSampler2, (int)2);
            }
            GL20.glUseProgram((int)0);
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

    public static int locProj() {
        return locProj;
    }

    public static int locColor() {
        return locColor;
    }

    public static int locOverlay() {
        return locOverlay;
    }

    public static int locFogStart() {
        return locFogStart;
    }

    public static int locFogEnd() {
        return locFogEnd;
    }

    public static int locFogColor() {
        return locFogColor;
    }

    public static int locFogShape() {
        return locFogShape;
    }

    public static int locLight0() {
        return locLight0;
    }

    public static int locLight1() {
        return locLight1;
    }

    public static int locAlphaMode() {
        return locAlphaMode;
    }
}

