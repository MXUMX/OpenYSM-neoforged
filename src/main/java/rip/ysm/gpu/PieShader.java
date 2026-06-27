/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 */
package rip.ysm.gpu;

import org.openysm.OpenYSM;
import org.openysm.util.log.ChatLogger;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rip.ysm.gpu.ShaderUtil;

public final class PieShader {
    private static int program = 0;
    private static int dummyVao = 0;
    private static int locProj = -1;
    private static int locRect = -1;
    private static int locCenter = -1;
    private static int locOuterRadius = -1;
    private static int locInnerRadius = -1;
    private static int locStartAngle = -1;
    private static int locEndAngle = -1;
    private static int locColor = -1;
    private static int locFeather = -1;
    private static boolean failed = false;

    private PieShader() {
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
            int vs = ShaderUtil.compileShaderFromResource(35633, "/pie.vsh");
            int fs = ShaderUtil.compileShaderFromResource(35632, "/pie.fsh");
            int prog = ShaderUtil.linkProgram(vs, fs);
            locProj = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_proj");
            locRect = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_rect");
            locCenter = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_center");
            locOuterRadius = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_outerRadius");
            locInnerRadius = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_innerRadius");
            locStartAngle = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_startAngle");
            locEndAngle = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_endAngle");
            locColor = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_color");
            locFeather = GL20.glGetUniformLocation((int)prog, (CharSequence)"u_feather");
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

    public static int locCenter() {
        return locCenter;
    }

    public static int locOuterRadius() {
        return locOuterRadius;
    }

    public static int locInnerRadius() {
        return locInnerRadius;
    }

    public static int locStartAngle() {
        return locStartAngle;
    }

    public static int locEndAngle() {
        return locEndAngle;
    }

    public static int locColor() {
        return locColor;
    }

    public static int locFeather() {
        return locFeather;
    }
}

