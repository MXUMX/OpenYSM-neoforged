package rip.ysm.neoforge.compat.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import rip.ysm.neoforge.compat.registries.tags.ITagManager;

public final class ForgeRegistries {
    public static final ResourceKey<Registry<SoundEvent>> SOUND_EVENTS = Registries.SOUND_EVENT;
    public static final RegistryFacade<Item> ITEMS = new RegistryFacade<>(BuiltInRegistries.ITEM, Registries.ITEM);
    public static final RegistryFacade<Block> BLOCKS = new RegistryFacade<>(BuiltInRegistries.BLOCK, Registries.BLOCK);
    public static final RegistryFacade<EntityType<?>> ENTITY_TYPES = new RegistryFacade<>(BuiltInRegistries.ENTITY_TYPE, Registries.ENTITY_TYPE);
    public static final RegistryFacade<MobEffect> MOB_EFFECTS = new RegistryFacade<>(BuiltInRegistries.MOB_EFFECT, Registries.MOB_EFFECT);
    public static final RegistryFacade<Enchantment> ENCHANTMENTS = new RegistryFacade<>(null, Registries.ENCHANTMENT);
    public static final RegistryFacade<Biome> BIOMES = new RegistryFacade<>(null, Registries.BIOME);

    private ForgeRegistries() {
    }

    public static final class RegistryFacade<T> {
        private final Registry<T> registry;
        private final ResourceKey<? extends Registry<T>> key;
        private final ITagManager<T> tagManager;

        private RegistryFacade(Registry<T> registry, ResourceKey<? extends Registry<T>> key) {
            this.registry = registry;
            this.key = key;
            this.tagManager = id -> TagKey.create(this.key, id);
        }

        public ResourceLocation getKey(T value) {
            return this.registry == null ? null : this.registry.getKey(value);
        }

        public T getValue(ResourceLocation id) {
            return this.registry == null ? null : this.registry.get(id);
        }

        public ITagManager<T> tags() {
            return this.tagManager;
        }
    }
}
