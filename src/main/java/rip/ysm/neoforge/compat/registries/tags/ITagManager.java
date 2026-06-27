package rip.ysm.neoforge.compat.registries.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Collections;

public interface ITagManager<T> {
    TagKey<T> createTagKey(ResourceLocation id);

    default Iterable<T> getTag(TagKey<T> key) {
        return Collections.emptyList();
    }
}
