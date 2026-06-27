package org.openysm.client.model.processor;

import org.openysm.client.entity.GeoEntity;
import org.openysm.client.model.ModelResourceBundle;

import java.util.function.Predicate;

public interface ModelProcessor<T extends GeoEntity<?>, TModel> {
    ControllerFactory<T> process(TModel modelData, ModelResourceBundle resourceBundle);

    default ModelProcessor<T, TModel> withFilter(Predicate<T> predicate) {
        return (modelData, resourceBundle) -> {
            ControllerFactory<T> installer = process(modelData, resourceBundle);
            return (entity, consumer) -> {
                if (predicate.test(entity)) {
                    installer.create(entity, consumer);
                }
            };
        };
    }
}