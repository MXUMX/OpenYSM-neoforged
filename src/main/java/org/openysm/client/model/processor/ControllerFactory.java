package org.openysm.client.model.processor;

import org.openysm.geckolib3.core.controller.IAnimationController;
import org.openysm.client.entity.GeoEntity;

import java.util.function.Consumer;

public interface ControllerFactory<T extends GeoEntity<?>> {
    void create(T entity, Consumer<IAnimationController<T>> consumer);
}