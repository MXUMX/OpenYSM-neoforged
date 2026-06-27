package org.openysm.client.compat.jade;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.forge.capability.VehicleCapabilityProvider;
import org.openysm.util.FileTypeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class JadeWailaPlugin implements IWailaPlugin {
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(new ModelInfoComponentProvider(), Entity.class);
    }

    private static class ModelInfoComponentProvider implements IEntityComponentProvider {

        private static final ResourceLocation RESOURCE_ID = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "model_info");

        private ModelInfoComponentProvider() {
        }

        public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
            Entity entity = entityAccessor.getEntity();
            if (entity instanceof Player) {
                org.openysm.capability.YSMCapabilities.get(entity, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.isModelActive()) {
                        iTooltip.add(Component.translatable("top.openysm.model_info.id").append(cap.getModelAssembly().getDisplayName(FileTypeUtil.getNameWithoutArchiveExtension(cap.getModelId()))));
                    }
                });
            } else {
                org.openysm.capability.YSMCapabilities.get(entityAccessor.getEntity(), VehicleCapabilityProvider.VEHICLE_CAP).ifPresent(cap -> {
                    if (cap.isModelInitialized() && cap.isModelReady()) {
                        iTooltip.add(Component.translatable("top.openysm.model_info.id").append(cap.getModelAssembly().getDisplayName(FileTypeUtil.getNameWithoutArchiveExtension(cap.getModelId()))));
                    }
                });
            }
        }

        public ResourceLocation getUid() {
            return RESOURCE_ID;
        }
    }
}
