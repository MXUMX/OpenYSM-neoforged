package org.openysm.client.entity;

import org.openysm.client.model.ModelAssembly;
import org.openysm.geckolib3.geo.render.built.GeoModel;
import org.openysm.client.animation.condition.ArmorConditions;
import org.openysm.capability.PlayerCapability;
import org.openysm.geckolib3.core.builder.AnimationController;
import org.openysm.geckolib3.core.builder.Animation;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PlayerGeoEntity extends GeoEntity<LocalPlayer> {

    private final PlayerCapability playerCapability;

    public PlayerGeoEntity(LocalPlayer player, PlayerCapability capability) {
        super(player, false);
        this.playerCapability = capability;
        setModelId(capability.getModelId());
    }

    @Override
    public void registerAnimationControllers() {
        getModelAssembly().getAnimationBundle().getArmControllerInstaller().accept(this);
    }

    public PlayerCapability getPlayerCapability() {
        return this.playerCapability;
    }

    @Override
    public boolean shouldSkipAnimation(AnimationEvent<?> event) {
        return true;
    }

    @Override
    public void tickModel() {
        if (this.playerCapability.getModelAssembly() != getModelAssembly()) {
            setModelId(this.playerCapability.getModelId());
        }
    }

    @Override
    @Nullable
    public GeoEntity.ModelWrapper buildRenderShape(ModelAssembly modelAssembly, boolean isDefault) {
        return this.playerCapability.getRenderShape();
    }

    @Override
    @Nullable
    public AnimationController getAnimationEntries(String str) {
        return getModelAssembly().getAnimationBundle().getAnimationEntries().get(str);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return this.playerCapability.getTextureLocation();
    }

    @Override
    public float getHeightScale() {
        return getModelAssembly().getModelData().getModelProperties().getHeightScale();
    }

    @Override
    public float getWidthScale() {
        return getModelAssembly().getModelData().getModelProperties().getWidthScale();
    }

    @Override
    @Nullable
    public Animation getAnimation(String str) {
        return getModelAssembly().getAnimationBundle().getArmAnimations().get(str);
    }

    public ArmorConditions getArmModelProcessor() {
        return getModelAssembly().getAnimationBundle().getModelProcessor();
    }

    @Override
    public GeoModel getAnimationProcessor() {
        return getModelAssembly().getAnimationBundle().getArmModel();
    }

    @Override
    public void setupAnim(float seekTime, boolean isFirstPerson) {
        getEvaluationContext().setRoamingProperties(this.playerCapability.getServerVarContainer());
    }
}