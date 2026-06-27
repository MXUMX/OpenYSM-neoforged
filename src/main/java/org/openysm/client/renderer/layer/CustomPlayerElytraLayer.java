package org.openysm.client.renderer.layer;

import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorHelper;
import org.openysm.geckolib3.geo.GeoLayerRenderer;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import org.openysm.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;

public class CustomPlayerElytraLayer extends GeoLayerRenderer<CustomPlayerEntity> {

    private static final ResourceLocation WINGS_LOCATION = ResourceLocation.parse("textures/entity/elytra.png");

    private final ElytraModel<LivingEntity> elytraModel;

    public CustomPlayerElytraLayer(EntityRendererProvider.Context context) {
        this.elytraModel = new ElytraModel<>(context.getModelSet().bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, CustomPlayerEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation cloakTextureLocation;
        LivingEntity entity = entityLivingBaseIn.getEntity();
        ItemStack stack = CosmeticArmorHelper.getElytraItem(entity);
        AnimatedGeoModel animatedGeoModel = entityLivingBaseIn.getCurrentModel();
        if (!stack.isEmpty() && animatedGeoModel != null && !animatedGeoModel.elytraBones().isEmpty() && (entity instanceof AbstractClientPlayer abstractClientPlayer)) {
            if (abstractClientPlayer.getSkin().elytraTexture() != null) {
                cloakTextureLocation = abstractClientPlayer.getSkin().elytraTexture();
            } else if (abstractClientPlayer.getSkin().capeTexture() != null && abstractClientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
                cloakTextureLocation = abstractClientPlayer.getSkin().capeTexture();
            } else {
                cloakTextureLocation = WINGS_LOCATION;
            }
            poseStack.pushPose();
            renderElytra(poseStack, animatedGeoModel);
            poseStack.translate(0.0d, 1.5d, 0.0d);
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0f));
            poseStack.scale(2.0f, 2.0f, 2.0f);
            this.elytraModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.elytraModel.renderToBuffer(poseStack, ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(cloakTextureLocation), stack.hasFoil()), packedLightIn, OverlayTexture.NO_OVERLAY, -1);
            poseStack.popPose();
        }
    }

    public void renderElytra(PoseStack poseStack, AnimatedGeoModel model) {
        RenderUtils.prepMatrixForLocator(poseStack, model.elytraBones());
    }
}
