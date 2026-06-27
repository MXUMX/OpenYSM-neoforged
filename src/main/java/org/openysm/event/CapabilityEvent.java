package org.openysm.event;

import org.openysm.OpenYSM;
import org.openysm.capability.*;
import org.openysm.config.ServerConfig;
import org.openysm.forge.capability.AuthModelsCapabilityProvider;
import org.openysm.forge.capability.ModelInfoCapabilityProvider;
import org.openysm.forge.capability.ProjectileModelCapabilityProvider;
import org.openysm.forge.capability.StarModelsCapabilityProvider;
import org.openysm.forge.capability.VehicleModelCapabilityProvider;
import org.openysm.model.ServerModelManager;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
public final class CapabilityEvent {

    private static final ResourceLocation MODEL_INFO_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "model_id");

    private static final ResourceLocation PROJECTILE_MODEL_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "projectile_model_id");

    private static final ResourceLocation VEHICLE_MODEL_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "vehicle_model_id");

    private static final ResourceLocation AUTH_MODELS_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "own_models");

    private static final ResourceLocation STAR_MODELS_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "star_models");

    private static final ResourceLocation PLAYER_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "animatable");

    private static final ResourceLocation PROJECTILE_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "projectile_animatable");

    private static final ResourceLocation VEHICLE_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "vehicle_animatable");

    private static final ResourceLocation CLIENT_LAZY_CAP = ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "client_lazy");

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        LazyOptional<ModelInfoCapability> oldModelInfoCap = getModelInfoCap(event.getOriginal());
        LazyOptional<AuthModelsCapability> oldAuthModelsCap = getAuthModelsCap(event.getOriginal());
        LazyOptional<StarModelsCapability> oldStarModelsCap = getStarModelsCap(event.getOriginal());
        LazyOptional<ModelInfoCapability> modelInfoCap = getModelInfoCap(event.getEntity());
        LazyOptional<AuthModelsCapability> authModelsCap = getAuthModelsCap(event.getEntity());
        LazyOptional<StarModelsCapability> starModelsCap = getStarModelsCap(event.getEntity());
        modelInfoCap.ifPresent(newModelInfo -> {
            Objects.requireNonNull(newModelInfo);
            oldModelInfoCap.ifPresent(newModelInfo::copyFrom);
        });
        authModelsCap.ifPresent(newAuthModels -> {
            Objects.requireNonNull(newAuthModels);
            oldAuthModelsCap.ifPresent(newAuthModels::copyFrom);
        });
        starModelsCap.ifPresent(newStarModels -> {
            Objects.requireNonNull(newStarModels);
            oldStarModelsCap.ifPresent(newStarModels::copyFrom);
        });
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking startTracking) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        Entity target = startTracking.getTarget();
        if (target instanceof ServerPlayer trackPlayer) {
            Player entity = startTracking.getEntity();
            getModelInfoCap(trackPlayer).ifPresent(cap -> {
                if (!NetworkHandler.isPlayerConnected(trackPlayer) && !cap.isMandatory()) {
                    return;
                }
                Optional<S2CSetModelAndTexturePacket> optional = cap.createSyncMessage(trackPlayer, false);
                Consumer<? super S2CSetModelAndTexturePacket> consumer = message -> {
                    NetworkHandler.sendToClientPlayer(message, entity);
                };
                Objects.requireNonNull(cap);
                optional.ifPresentOrElse(consumer, cap::markDirty);
            });
            return;
        }
        target = startTracking.getTarget();
        if (target instanceof Projectile projectile) {
            org.openysm.capability.YSMCapabilities.get(projectile, ProjectileModelCapabilityProvider.PROJECTILE_MODEL).ifPresent(cap -> {
                if (cap.isInitialized()) {
                    NetworkHandler.sendToClientPlayer(new S2CSyncProjectileModelPacket(projectile.getId(), cap), startTracking.getEntity());
                }
            });
        } else if (startTracking.getTarget() != null) {
            org.openysm.capability.YSMCapabilities.get(startTracking.getTarget(), VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP).ifPresent(cap -> {
                if (cap.isInitialized()) {
                    NetworkHandler.sendToClientPlayer(new S2CSyncVehicleModelPacket(startTracking.getTarget().getId(), cap), startTracking.getEntity());
                }
            });
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer player) {
            getModelInfoCap(player).ifPresent(modelInfoCap -> {
                if (!NetworkHandler.isPlayerConnected(player) && !modelInfoCap.isMandatory()) {
                    modelInfoCap.markDirty();
                    return;
                }
                modelInfoCap.stopAnimation(player);
                Optional<S2CSetModelAndTexturePacket> optional = modelInfoCap.createSyncMessage(player, false);
                Consumer<? super S2CSetModelAndTexturePacket> consumer = message -> {
                    NetworkHandler.sendToClientPlayer(message, player);
                };
                Objects.requireNonNull(modelInfoCap);
                optional.ifPresentOrElse(consumer, modelInfoCap::markDirty);
            });
            getAuthModelsCap(player).ifPresent(authModelsCap -> {
                for (String modelId : ServerModelManager.getAuthModels()) {
                    authModelsCap.addModel(modelId);
                }
                NetworkHandler.sendToClientPlayer(new S2CSyncAuthModelsPacket(authModelsCap.getAuthModels()), player);
            });
            getStarModelsCap(player).ifPresent(starModelsCap -> {
                NetworkHandler.sendToClientPlayer(new S2CSyncStarModelsPacket(starModelsCap.getStarModels()), player);
            });
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post serverTickEvent) {
        if (OpenYSM.isAvailable()) {
            List<ServerPlayer> players = serverTickEvent.getServer().getPlayerList().getPlayers();
            Boolean bool = ServerConfig.LOW_BANDWIDTH_USAGE.get();
            for (ServerPlayer serverPlayer : players) {
                getModelInfoCap(serverPlayer).ifPresent(cap -> {
                    if (!NetworkHandler.isPlayerConnected(serverPlayer) && !cap.isMandatory()) {
                        if (serverPlayer.tickCount == 200 || serverPlayer.tickCount == 600 || serverPlayer.tickCount == 1800) {
                            NetworkHandler.sendToClientPlayer(new S2CVersionCheckPacket(), serverPlayer);
                            return;
                        }
                        return;
                    }
                    if (cap.isDirty()) {
                        cap.getAnimSync().updateAndSync(serverPlayer, false, bool);
                        cap.createSyncMessage(serverPlayer, true).ifPresent(message -> {
                            cap.clearDirty();
                            NetworkHandler.sendToTrackingEntityAndSelf(message, serverPlayer);
                            if (serverPlayer.getVehicle() != null && serverPlayer.getVehicle().getFirstPassenger() == serverPlayer) {
                                syncVehicleModel(serverPlayer.getVehicle(), serverPlayer);
                            }
                        });
                    } else {
                        cap.getAnimSync().updateAndSync(serverPlayer, true, bool);
                    }
                });
            }
        }
    }

    public static void syncProjectileModel(Projectile projectile, ServerPlayer serverPlayer) {
        org.openysm.capability.YSMCapabilities.get(serverPlayer, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(modelInfoCap -> {
            if (!NetworkHandler.isPlayerConnected(serverPlayer) && !modelInfoCap.isMandatory()) {
                return;
            }
            org.openysm.capability.YSMCapabilities.get(projectile, ProjectileModelCapabilityProvider.PROJECTILE_MODEL).ifPresent(projectileModelCap -> modelInfoCap.withMolangVars(object2FloatOpenHashMap -> {
                projectileModelCap.setModel(modelInfoCap.getModelId(), object2FloatOpenHashMap);
                NetworkHandler.sendToTrackingEntity(new S2CSyncProjectileModelPacket(projectile.getId(), projectileModelCap), projectile);
            }));
        });
    }

    public static void syncVehicleModel(Entity entity, ServerPlayer serverPlayer) {
        org.openysm.capability.YSMCapabilities.get(serverPlayer, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(modelInfoCap -> {
            if (!NetworkHandler.isPlayerConnected(serverPlayer) && !modelInfoCap.isMandatory()) {
                return;
            }
            org.openysm.capability.YSMCapabilities.get(entity, VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP).ifPresent(vehicleModelCap -> modelInfoCap.getMolangVars().ifPresent(object2FloatOpenHashMap -> {
                vehicleModelCap.setModel(modelInfoCap.getModelId(), object2FloatOpenHashMap);
                NetworkHandler.sendToTrackingEntity(new S2CSyncVehicleModelPacket(entity.getId(), vehicleModelCap), entity);
            }));
        });
    }

    private static LazyOptional<ModelInfoCapability> getModelInfoCap(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, ModelInfoCapabilityProvider.MODEL_INFO_CAP);
    }

    private static LazyOptional<AuthModelsCapability> getAuthModelsCap(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, AuthModelsCapabilityProvider.AUTH_MODELS_CAP);
    }

    private static LazyOptional<StarModelsCapability> getStarModelsCap(Player player) {
        return org.openysm.capability.YSMCapabilities.get(player, StarModelsCapabilityProvider.STAR_MODELS_CAP);
    }
}
