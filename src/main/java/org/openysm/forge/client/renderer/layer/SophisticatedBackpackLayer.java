/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackLayerRenderer
 */
package org.openysm.client.renderer.layer;

import org.openysm.client.compat.sbackpack.SBackpackCompat;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.geo.GeoLayerRenderer;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import org.openysm.geckolib3.core.processor.IBone;
import org.openysm.geckolib3.geo.render.built.GeoBone;
import org.openysm.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackLayerRenderer;

import java.util.ArrayList;
import java.util.List;

public class SophisticatedBackpackLayer
extends GeoLayerRenderer<CustomPlayerEntity> {
    private static final String[] BACKPACK_FALLBACK_BONES = {"UpperBody", "UpBody", "Body", "AllBody"};

    private final EntityModel<Player> backpackModel = SophisticatedBackpackLayer.createBackpackModel();

    private static EntityModel<Player> createBackpackModel() {
        return new EntityModel<Player>(){

            public void setupAnim(Player player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            }

            public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
            }
        };
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, CustomPlayerEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        Player player;
        ItemStack stack;
        AnimatedGeoModel model = entityLivingBaseIn.getCurrentModel();
        List<IBone> backpackBones = this.getBackpackBones(model);
        if (!backpackBones.isEmpty() && (stack = SBackpackCompat.getBackpackItem((LivingEntity)(player = (Player)entityLivingBaseIn.getEntity()))) != null) {
            poseStack.pushPose();
            this.renderBackpack(poseStack, backpackBones);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            poseStack.translate(0.0, -0.1, 0.0);
            BackpackLayerRenderer.renderBackpack(this.backpackModel, (LivingEntity)player, (PoseStack)poseStack, (MultiBufferSource)bufferSource, (int)packedLightIn, (ItemStack)stack, (boolean)false);
            poseStack.popPose();
        }
    }

    private List<IBone> getBackpackBones(AnimatedGeoModel model) {
        if (model == null) {
            return List.of();
        }
        List<IBone> backpackBones = model.backpackBones();
        if (!backpackBones.isEmpty()) {
            return backpackBones;
        }
        if (!model.elytraBones().isEmpty()) {
            return model.elytraBones();
        }
        for (String fallbackBone : BACKPACK_FALLBACK_BONES) {
            List<IBone> fallbackBones = this.buildBoneChain(model, fallbackBone);
            if (!fallbackBones.isEmpty()) {
                return fallbackBones;
            }
        }
        return List.of();
    }

    private List<IBone> buildBoneChain(AnimatedGeoModel model, String targetBoneName) {
        ArrayList<IBone> chain = new ArrayList<>();
        String boneName = targetBoneName;
        while (boneName != null && !boneName.isEmpty()) {
            GeoBone geoBone = this.findGeoBone(model, boneName);
            if (geoBone == null) {
                return List.of();
            }
            IBone bone = model.bones().get(geoBone.getBoneId());
            if (bone == null) {
                return List.of();
            }
            chain.add(0, bone);
            boneName = geoBone.parentName;
        }
        return chain;
    }

    private GeoBone findGeoBone(AnimatedGeoModel model, String boneName) {
        for (GeoBone geoBone : model.getGeoModel().topLevelBones()) {
            if (geoBone.getName().equals(boneName)) {
                return geoBone;
            }
        }
        return null;
    }

    public void renderBackpack(PoseStack poseStack, List<IBone> backpackBones) {
        RenderUtils.prepMatrixForLocator(poseStack, backpackBones);
    }
}
