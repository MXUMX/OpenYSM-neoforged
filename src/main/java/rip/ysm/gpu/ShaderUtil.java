/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL20
 */
package rip.ysm.gpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import org.lwjgl.opengl.GL20;

public final class ShaderUtil {
    private ShaderUtil() {
    }

    public static String loadResource(String path) throws IOException {
        try (InputStream in = ShaderUtil.class.getResourceAsStream(path);){
            String string;
            if (in == null) {
                throw new IOException("resource not found: " + path);
            }
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));){
                string = r.lines().collect(Collectors.joining("\n"));
            }
            return string;
        }
    }

    public static int compileShader(int glType, String src, String name) {
        int sh = GL20.glCreateShader((int)glType);
        GL20.glShaderSource((int)sh, (CharSequence)src);
        GL20.glCompileShader((int)sh);
        if (GL20.glGetShaderi((int)sh, (int)35713) == 0) {
            String log = GL20.glGetShaderInfoLog((int)sh);
            GL20.glDeleteShader((int)sh);
            throw new RuntimeException("Compile failed (" + name + "): " + log);
        }
        return sh;
    }

    public static int compileShaderFromResource(int glType, String resourcePath) throws IOException {
        return ShaderUtil.compileShader(glType, ShaderUtil.loadResource(resourcePath), resourcePath);
    }

    public static int linkProgram(int ... shaderIds) {
        return ShaderUtil.linkProgramWith(null, shaderIds);
    }

    public static int linkProgramWith(IntConsumer preLink, int ... shaderIds) {
        int prog = GL20.glCreateProgram();
        for (int sh : shaderIds) {
            GL20.glAttachShader((int)prog, (int)sh);
        }
        if (preLink != null) {
            preLink.accept(prog);
        }
        GL20.glLinkProgram((int)prog);
        if (GL20.glGetProgrami((int)prog, (int)35714) == 0) {
            String log = GL20.glGetProgramInfoLog((int)prog);
            GL20.glDeleteProgram((int)prog);
            for (int sh : shaderIds) {
                GL20.glDeleteShader((int)sh);
            }
            throw new RuntimeException("Link failed: " + log);
        }
        for (int sh : shaderIds) {
            GL20.glDetachShader((int)prog, (int)sh);
            GL20.glDeleteShader((int)sh);
        }
        return prog;
    }
}

