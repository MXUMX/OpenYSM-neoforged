package rip.ysm.neoforge.compat.registries;

import net.minecraft.resources.ResourceLocation;

public interface IForgeRegistry<T> {
    T getValue(ResourceLocation id);
}
