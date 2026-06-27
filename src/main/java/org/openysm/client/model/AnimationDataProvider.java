package org.openysm.client.model;

import org.openysm.geckolib3.core.builder.AnimationController;
import org.openysm.client.model.ModelResourceBundle;
import org.openysm.client.animation.condition.ConditionArmor;
import org.openysm.geckolib3.core.builder.Animation;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;

public interface AnimationDataProvider<T> {
    Object2ReferenceMap<String, AnimationController> getAnimationEntries(T t, ModelResourceBundle resourceBundle);

    Object2ReferenceMap<String, Animation> getAnimations(T t, ModelResourceBundle resourceBundle);

    ConditionArmor getConditionArmor(T t, ModelResourceBundle resourceBundle);
}