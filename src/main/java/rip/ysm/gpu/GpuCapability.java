/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  org.lwjgl.opengl.GL
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLCapabilities
 */
package rip.ysm.gpu;

import org.openysm.NativeLibLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

public final class GpuCapability {
    private static volatile boolean checked = false;
    private static volatile boolean available = false;
    private static volatile String reason = null;

    public static boolean isAvailable() {
        if (!checked) {
            GpuCapability.check();
        }
        return available;
    }

    public static String getReason() {
        if (!checked) {
            GpuCapability.check();
        }
        return reason;
    }

    public static synchronized void check() {
        boolean hasPackedNormal;
        String glslVersion;
        String glVendor;
        String glRenderer;
        String glVersion;
        GLCapabilities caps;
        if (checked) {
            return;
        }
        checked = true;
        if (System.getProperty("OYSM_DISABLE_GPU") != null) {
            reason = "gpu renderer has been disabled";
            return;
        }
        if (!NativeLibLoader.isLoaded()) {
            reason = "native ysm-core not loaded";
            return;
        }
        String osName = System.getProperty("os.name", "").toLowerCase();
        if (osName.contains("mac") || osName.contains("darwin")) {
            reason = "macOS GL is capped at 4.1 and lacks GL_ARB_shader_storage_buffer_object";
            return;
        }
        try {
            RenderSystem.assertOnRenderThreadOrInit();
            caps = GL.getCapabilities();
            glVersion = GL11.glGetString((int)7938);
            glRenderer = GL11.glGetString((int)7937);
            glVendor = GL11.glGetString((int)7936);
            glslVersion = GL11.glGetString((int)35724);
        }
        catch (Throwable t) {
            reason = "GL capabilities not available: " + t.getMessage();
            return;
        }
        if (glVersion == null) {
            reason = "GL version not available";
            return;
        }
        System.out.println("OpenGL version: " + glVersion);
        System.out.println("OpenGL renderer version: " + glRenderer);
        System.out.println("OpenGL vendor: " + glVendor);
        System.out.println("OpenGL glsl version: " + glslVersion);
        if (!caps.OpenGL30) {
            reason = "OpenGL 3.0 not supported (got " + glVersion + ")";
            return;
        }
        boolean hasSsbo = caps.OpenGL43 || caps.GL_ARB_shader_storage_buffer_object;
        boolean hasIfaceQuery = caps.OpenGL43 || caps.GL_ARB_program_interface_query;
        boolean hasLayoutBinding = caps.OpenGL42 || caps.GL_ARB_shading_language_420pack;
        boolean hasExplicitAttrib = caps.OpenGL33 || caps.GL_ARB_explicit_attrib_location;
        boolean bl = hasPackedNormal = caps.OpenGL33 || caps.GL_ARB_vertex_type_2_10_10_10_rev;
        if (!hasSsbo) {
            reason = "SSBO not supported, GL_VERSION=" + glVersion;
            return;
        }
        if (!hasIfaceQuery) {
            reason = "GL_ARB_program_interface_query not supported; GL_VERSION=" + glVersion;
            return;
        }
        if (!hasLayoutBinding) {
            reason = "GL_ARB_shading_language_420pack not supported; GL_VERSION=" + glVersion;
            return;
        }
        if (!hasExplicitAttrib) {
            reason = "GL_ARB_explicit_attrib_location not supported; GL_VERSION=" + glVersion;
            return;
        }
        if (!hasPackedNormal) {
            reason = "GL_ARB_vertex_type_2_10_10_10_rev not supported; GL_VERSION=" + glVersion;
            return;
        }
        available = true;
        reason = "ok (GL " + glVersion + ", " + glRenderer + ")";
    }
}

