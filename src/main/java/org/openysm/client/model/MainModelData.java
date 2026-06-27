package org.openysm.client.model;

import org.openysm.client.texture.OuterFileTexture;
import org.openysm.geckolib3.file.AnimationControllerFile;
import org.openysm.geckolib3.file.AnimationFile;
import org.openysm.geckolib3.geo.render.built.GeoModel;
import org.openysm.util.data.OrderedStringMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Map;

public class MainModelData {

    private final List<GeoModel> models;

    private final Map<String, AnimationFile> animations;

    private final List<AnimationControllerFile> animationControllers;

    private final OrderedStringMap<String, OuterFileTexture> textureMap;

    public MainModelData(GeoModel[] models, Map<String, AnimationFile> animations, AnimationControllerFile[] animationControllerFiles, OrderedStringMap<String, OuterFileTexture> textureMap) {
        this.models = ObjectArrayList.wrap(models);
        this.animations = animations;
        this.animationControllers = ObjectArrayList.wrap(animationControllerFiles);
        this.textureMap = textureMap;
    }

    public Map<String, AnimationFile> getAnimations() {
        return this.animations;
    }

    public List<GeoModel> getModels() {
        return this.models;
    }

    public List<AnimationControllerFile> getAnimationControllers() {
        return this.animationControllers;
    }

    public OrderedStringMap<String, OuterFileTexture> getTextureMap() {
        return this.textureMap;
    }
}