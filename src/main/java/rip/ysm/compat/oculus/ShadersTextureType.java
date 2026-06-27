/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package rip.ysm.compat.oculus;

import net.minecraft.resources.ResourceLocation;

public enum ShadersTextureType {
    NORMAL("_n"),
    SPECULAR("_s");

    public static final ShadersTextureType[] VALUES;
    private final String suffix;

    private ShadersTextureType(String str) {
        this.suffix = str;
    }

    public ResourceLocation appendSuffix(ResourceLocation resourceLocation) {
        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), resourceLocation.getPath() + this.suffix);
    }

    static {
        VALUES = ShadersTextureType.values();
    }
}
