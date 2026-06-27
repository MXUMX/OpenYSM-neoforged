package org.openysm.client.renderer;

import org.openysm.forge.capability.ProjectileCapabilityProvider;
import org.openysm.client.entity.GeckoProjectileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;

public class ProjectileRenderer extends AbstractProjectileRenderer<Projectile, GeckoProjectileEntity> {
    public ProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Projectile projectile, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (Minecraft.getInstance().player == null || projectile.isInvisibleTo(Minecraft.getInstance().player)) {
            return;
        }
        org.openysm.capability.YSMCapabilities.get(projectile, ProjectileCapabilityProvider.PROJECTILE_CAP).ifPresent(cap -> {
            cap.tickModel();
            render(cap, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        });
    }

    @NotNull
    public ResourceLocation getTextureLocation(Projectile projectile) {
        return org.openysm.capability.YSMCapabilities.get(projectile, ProjectileCapabilityProvider.PROJECTILE_CAP).map((cap) -> cap.getTextureLocation()).orElse(MissingTextureAtlasSprite.getLocation());
    }
}