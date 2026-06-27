package org.openysm.client.compat.touhoulittlemaid;

import org.openysm.model.ServerModelManager;
import org.openysm.OpenYSM;
import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.forge.capability.ProjectileModelCapabilityProvider;
import org.openysm.forge.capability.VehicleModelCapabilityProvider;
import org.openysm.resource.models.ModelProperties;
import org.openysm.geckolib3.resource.GeckoLibCache;
import org.openysm.molang.parser.ParseException;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.S2CSyncProjectileModelPacket;
import org.openysm.network.message.S2CSyncVehicleModelPacket;
import org.openysm.network.message.FeedbackData;
import org.openysm.util.data.OrderedStringMap;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TouhouMaidModelHandler {
    public static boolean isMaidEntity(Entity entity) {
        return entity instanceof EntityMaid;
    }

    @OnlyIn(Dist.CLIENT)
    public static void executeMaidMolang(Entity entity, String str) {
        if (!(entity instanceof EntityMaid entityMaid)) {
            return;
        }
        if (!entityMaid.isYsmModel()) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(entityMaid, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
            try {
                cap.executeExpression(GeckoLibCache.parseSimpleExpression(str), true, false, null);
            } catch (ParseException e) {
                OpenYSM.LOGGER.error("Failed to execute molang " + str, e);
            }
        });
    }

    public static void applyProjectileModelFromMaid(Projectile projectile, Entity entity) {
        if (!(entity instanceof EntityMaid entityMaid)) {
            return;
        }
        if (entityMaid.isYsmModel()) {
            org.openysm.capability.YSMCapabilities.get(projectile, ProjectileModelCapabilityProvider.PROJECTILE_MODEL).ifPresent(cap -> {
                cap.setModel(entityMaid.getYsmModelId(), new Object2FloatOpenHashMap<>());
                NetworkHandler.sendToTrackingEntity(new S2CSyncProjectileModelPacket(projectile.getId(), cap), projectile);
            });
        }
    }

    public static void applyVehicleModelFromMaid(Entity entity, Entity entity2) {
        if (!(entity2 instanceof EntityMaid entityMaid)) {
            return;
        }
        if (entityMaid.isYsmModel() && entity.getFirstPassenger() == entity2) {
            org.openysm.capability.YSMCapabilities.get(entity, VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP).ifPresent(cap -> {
                cap.setModel(entityMaid.getYsmModelId(), new Object2FloatOpenHashMap<>());
                NetworkHandler.sendToTrackingEntity(new S2CSyncVehicleModelPacket(entity.getId(), cap), entity);
            });
        }
    }

    public static void activateRouletteAnimation(Entity entity, String str, int i) {
        if (!(entity instanceof EntityMaid entityMaid)) {
            return;
        }
        if (!entityMaid.isYsmModel()) {
            return;
        }
        if (i == -1) {
            entityMaid.stopRouletteAnim();
        } else {
            ServerModelManager.getModelDefinition(entityMaid.getYsmModelId()).ifPresent(data -> {
                OrderedStringMap<String, String> rouletteAnims;
                ModelProperties modelProperties = data.getLoadedModelData().getModelProperties();
                Map<String, OrderedStringMap<String, String>> extraAnimationClassify = modelProperties.getExtraAnimationClassify();
                if (StringUtils.isNotBlank(str) && extraAnimationClassify.containsKey(str)) {
                    rouletteAnims = extraAnimationClassify.get(str);
                } else {
                    rouletteAnims = modelProperties.getExtraAnimation();
                }
                if (rouletteAnims.size() > i) {
                    entityMaid.playRouletteAnim(rouletteAnims.getKeyAt(i));
                }
            });
        }
    }

    public static void handleMaidFeedback(Entity entity, FeedbackData message) {
        if (!(entity instanceof EntityMaid) || !((EntityMaid) entity).isYsmModel()) {
        }
    }
}