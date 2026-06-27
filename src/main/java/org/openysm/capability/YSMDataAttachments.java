package org.openysm.capability;

import org.openysm.OpenYSM;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class YSMDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> REGISTER = DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.ATTACHMENT_TYPES, OpenYSM.MOD_ID);

    public static final Supplier<AttachmentType<ModelInfoCapability>> MODEL_INFO = REGISTER.register("model_id", () -> AttachmentType.builder(holder -> new ModelInfoCapability()).serialize(compoundSerializer(ModelInfoCapability::new, ModelInfoCapability::serializeNBT, ModelInfoCapability::deserializeNBT)).copyOnDeath().build());
    public static final Supplier<AttachmentType<AuthModelsCapability>> AUTH_MODELS = REGISTER.register("own_models", () -> AttachmentType.builder(holder -> new AuthModelsCapability()).serialize(listSerializer(AuthModelsCapability::new, AuthModelsCapability::serializeNBT, AuthModelsCapability::deserializeNBT)).copyOnDeath().build());
    public static final Supplier<AttachmentType<StarModelsCapability>> STAR_MODELS = REGISTER.register("star_models", () -> AttachmentType.builder(holder -> new StarModelsCapability()).serialize(listSerializer(StarModelsCapability::new, StarModelsCapability::serializeNBT, StarModelsCapability::deserializeNBT)).copyOnDeath().build());
    public static final Supplier<AttachmentType<ProjectileModelCapability>> PROJECTILE_MODEL = REGISTER.register("projectile_model_id", () -> AttachmentType.builder(holder -> new ProjectileModelCapability()).serialize(compoundSerializer(ProjectileModelCapability::new, ProjectileModelCapability::serializeNBT, ProjectileModelCapability::deserializeNBT)).build());
    public static final Supplier<AttachmentType<VehicleModelCapability>> VEHICLE_MODEL = REGISTER.register("vehicle_model_id", () -> AttachmentType.builder(holder -> new VehicleModelCapability()).serialize(compoundSerializer(VehicleModelCapability::new, VehicleModelCapability::serializeNBT, VehicleModelCapability::deserializeNBT)).build());
    public static final Supplier<AttachmentType<Object>> PLAYER = REGISTER.register("animatable", () -> AttachmentType.builder(holder -> createPlayerAttachment((Player) holder)).build());
    public static final Supplier<AttachmentType<Object>> PROJECTILE = REGISTER.register("projectile_animatable", () -> AttachmentType.builder(holder -> createProjectileAttachment((Projectile) holder)).build());
    public static final Supplier<AttachmentType<Object>> VEHICLE = REGISTER.register("vehicle_animatable", () -> AttachmentType.builder(holder -> createVehicleAttachment((Entity) holder)).build());
    public static final Supplier<AttachmentType<Object>> CLIENT_LAZY = REGISTER.register("client_lazy", () -> AttachmentType.builder(holder -> createClientLazyAttachment((Entity) holder)).build());

    private YSMDataAttachments() {
    }

    private static Object createPlayerAttachment(Player player) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return org.openysm.client.ClientAttachmentFactory.createPlayer(player);
        }
        return ServerAnimatableAttachment.INSTANCE;
    }

    private static Object createProjectileAttachment(Projectile projectile) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return org.openysm.client.ClientAttachmentFactory.createProjectile(projectile);
        }
        return ServerAnimatableAttachment.INSTANCE;
    }

    private static Object createVehicleAttachment(Entity entity) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return org.openysm.client.ClientAttachmentFactory.createVehicle(entity);
        }
        return ServerAnimatableAttachment.INSTANCE;
    }

    private static Object createClientLazyAttachment(Entity entity) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return org.openysm.client.ClientAttachmentFactory.createClientLazy(entity);
        }
        return ServerAnimatableAttachment.INSTANCE;
    }

    private static <T> IAttachmentSerializer<CompoundTag, T> compoundSerializer(Supplier<T> factory, Function<T, CompoundTag> writer, BiConsumer<T, CompoundTag> reader) {
        return tagSerializer(factory, writer, reader);
    }

    private static <T> IAttachmentSerializer<ListTag, T> listSerializer(Supplier<T> factory, Function<T, ListTag> writer, BiConsumer<T, ListTag> reader) {
        return tagSerializer(factory, writer, reader);
    }

    private static <S extends Tag, T> IAttachmentSerializer<S, T> tagSerializer(Supplier<T> factory, Function<T, S> writer, BiConsumer<T, S> reader) {
        return new IAttachmentSerializer<>() {
            @Override
            public T read(IAttachmentHolder holder, S tag, HolderLookup.Provider provider) {
                T capability = factory.get();
                reader.accept(capability, tag);
                return capability;
            }

            @Override
            public S write(T attachment, HolderLookup.Provider provider) {
                return writer.apply(attachment);
            }
        };
    }
}
