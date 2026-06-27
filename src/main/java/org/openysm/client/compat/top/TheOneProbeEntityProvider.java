package org.openysm.client.compat.top;

import org.openysm.model.ServerModelManager;
import org.openysm.OpenYSM;
import org.openysm.forge.capability.ModelInfoCapabilityProvider;
import org.openysm.forge.capability.VehicleModelCapabilityProvider;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.network.NetworkHandler;
import org.openysm.util.FileTypeUtil;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class TheOneProbeEntityProvider implements Function<ITheOneProbe, Void> {
    @Override
    @Nullable
    public Void apply(@Nullable ITheOneProbe iTheOneProbe) {
        if (iTheOneProbe != null) {
            iTheOneProbe.registerEntityProvider(new ModelInfoEntityProvider());
            return null;
        }
        return null;
    }

    private static class ModelInfoEntityProvider implements IProbeInfoEntityProvider {

        private static final String PROVIDER_ID = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "model_info").toString();

        private ModelInfoEntityProvider() {
        }

        public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
            if (entity instanceof ServerPlayer serverPlayer) {
                org.openysm.capability.YSMCapabilities.get(serverPlayer, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(cap -> {
                    if (cap.isMandatory() || NetworkHandler.isPlayerConnected(serverPlayer)) {
                        ServerModelManager.getModelDefinition(cap.getModelId()).ifPresent(data -> {
                            iProbeInfo.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(Component.translatable("top.openysm.model_info.id").append(StringUtils.defaultIfBlank(data.getLoadedModelData().getExtraInfo() == null ? StringPool.EMPTY : data.getLoadedModelData().getExtraInfo().getName(), FileTypeUtil.getNameWithoutArchiveExtension(cap.getModelId()))));
                        });
                    }
                });
            } else {
                org.openysm.capability.YSMCapabilities.get(entity, VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP).ifPresent(cap -> {
                    if (cap.isInitialized()) {
                        ServerModelManager.getModelDefinition(cap.getOwnerModelId()).filter(data -> data.getExcludedEntityTypes().contains(entity.getType().builtInRegistryHolder().key().location())).ifPresent(serverModelData -> iProbeInfo.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(Component.translatable("top.openysm.model_info.id").append(StringUtils.defaultIfBlank(serverModelData.getLoadedModelData().getExtraInfo() == null ? StringPool.EMPTY : serverModelData.getLoadedModelData().getExtraInfo().getName(), FileTypeUtil.getNameWithoutArchiveExtension(cap.getOwnerModelId())))));
                    }
                });
            }
        }

        public String getID() {
            return PROVIDER_ID;
        }
    }
}