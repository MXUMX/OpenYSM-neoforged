package org.openysm.client.renderer;

import org.openysm.forge.capability.VehicleCapabilityProvider;
import org.openysm.client.entity.GeckoVehicleEntity;
import org.openysm.geckolib3.geo.GeoEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class VehicleRenderer extends GeoEntityRenderer<Entity, GeckoVehicleEntity> {
    public VehicleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (Minecraft.getInstance().player == null || entity.isInvisibleTo(Minecraft.getInstance().player)) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(entity, VehicleCapabilityProvider.VEHICLE_CAP).ifPresent(cap -> {
            cap.tickModel();
            renderEntity(cap, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        });
    }

    @NotNull
    public ResourceLocation getTextureLocation(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, VehicleCapabilityProvider.VEHICLE_CAP).map((cap) -> cap.getTextureLocation()).orElse(MissingTextureAtlasSprite.getLocation());
    }
}