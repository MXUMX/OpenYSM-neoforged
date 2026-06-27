package org.openysm.client.texture;

import org.openysm.client.compat.oculus.ShadersTextureType;
import net.minecraft.client.renderer.texture.AbstractTexture;

import java.util.Map;

public interface ITextureMap {
    Map<ShadersTextureType, ? extends AbstractTexture> getSuffixTextures();
}