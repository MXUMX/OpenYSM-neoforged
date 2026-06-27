package org.openysm.client.upload;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public interface IResourceLocatable {
    Optional<ResourceLocation> getResourceLocation();
}