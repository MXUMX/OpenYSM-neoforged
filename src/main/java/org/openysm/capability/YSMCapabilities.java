package org.openysm.capability;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapability;
import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.forge.capability.AuthModelsCapabilityProvider;
import org.openysm.forge.capability.ModelInfoCapabilityProvider;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.forge.capability.ProjectileCapabilityProvider;
import org.openysm.forge.capability.ProjectileModelCapabilityProvider;
import org.openysm.forge.capability.StarModelsCapabilityProvider;
import org.openysm.forge.capability.VehicleCapabilityProvider;
import org.openysm.forge.capability.VehicleModelCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public final class YSMCapabilities {
    private YSMCapabilities() {
    }

    @SuppressWarnings("unchecked")
    public static <T> LazyOptional<T> get(Entity entity, Capability<T> capability) {
        if (capability == ModelInfoCapabilityProvider.MODEL_INFO_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.MODEL_INFO));
        }
        if (capability == AuthModelsCapabilityProvider.AUTH_MODELS_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.AUTH_MODELS));
        }
        if (capability == StarModelsCapabilityProvider.STAR_MODELS_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.STAR_MODELS));
        }
        if (capability == ProjectileModelCapabilityProvider.PROJECTILE_MODEL) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.PROJECTILE_MODEL));
        }
        if (capability == VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.VEHICLE_MODEL));
        }
        if (capability == PlayerCapabilityProvider.PLAYER_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.PLAYER));
        }
        if (capability == ProjectileCapabilityProvider.PROJECTILE_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.PROJECTILE));
        }
        if (capability == VehicleCapabilityProvider.VEHICLE_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.VEHICLE));
        }
        if (capability == ClientLazyCapabilityProvider.CLIENT_LAZY_CAP) {
            return LazyOptional.of(() -> (T) entity.getData(YSMDataAttachments.CLIENT_LAZY));
        }
        if (capability == MaidCapabilityProvider.MAID_CAP) {
            return LazyOptional.empty();
        }
        return LazyOptional.empty();
    }
}
