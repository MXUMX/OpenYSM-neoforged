package org.openysm.client.model.processor;

import org.openysm.client.entity.GeoEntity;
import org.openysm.geckolib3.core.builder.AnimationController;
import org.openysm.client.model.ModelResourceBundle;
import org.openysm.geckolib3.core.controller.IAnimationController;
import org.openysm.client.animation.condition.ConditionArmor;
import org.openysm.geckolib3.core.builder.Animation;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.client.model.AnimationDataProvider;
import org.openysm.client.model.PlayerModelBundle;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import net.minecraft.world.entity.EquipmentSlot;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class ArmorSlotProcessor<T extends GeoEntity<?>> implements ModelProcessor<T, PlayerModelBundle> {

    private final String prefix;

    private final String category;

    private final AnimationDataProvider<PlayerModelBundle> animationDataProvider;

    private final TriFunction<String, T, EquipmentSlot, IAnimationController<T>> controllerFactory;

    public ArmorSlotProcessor(String prefix, String category, AnimationDataProvider<PlayerModelBundle> animationDataProvider, TriFunction<String, T, EquipmentSlot, IAnimationController<T>> controllerFactory) {
        this.prefix = prefix;
        this.category = category;
        this.animationDataProvider = animationDataProvider;
        this.controllerFactory = controllerFactory;
    }

    @Override
    public ControllerFactory<T> process(PlayerModelBundle modelBundle, ModelResourceBundle resourceBundle) {
        ArrayList<Pair<String, EquipmentSlot>> matchingSlots = new ArrayList<>();
        ConditionArmor armorCondition = this.animationDataProvider.getConditionArmor(modelBundle, resourceBundle);
        Object2ReferenceMap<String, AnimationController> animationEntries = this.animationDataProvider.getAnimationEntries(modelBundle, resourceBundle);
        Object2ReferenceMap<String, Animation> animations = this.animationDataProvider.getAnimations(modelBundle, resourceBundle);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            String slotKey = String.format("%s.%s_%s", this.prefix, this.category, slot.getName());
            if (animationEntries.containsKey(slotKey)) {
                matchingSlots.add(Pair.of(slotKey, slot));
            } else if (resourceBundle.getEvents().containsKey(String.format("%s_ctrl_%s_%s", this.prefix, this.category, slot.getName()))) {
                matchingSlots.add(Pair.of(slotKey, slot));
            } else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && (armorCondition.hasFilter(slot) || animations.containsKey(slot.getName() + ":default"))) {
                matchingSlots.add(Pair.of(slotKey, slot));
            }
        }
        return (entity, consumer) -> {
            if (!(entity instanceof IPreviewAnimatable)) {
                for (Pair<String, EquipmentSlot> pair : matchingSlots) {
                    consumer.accept(this.controllerFactory.apply(pair.getLeft(), entity, pair.getRight()));
                }
            }
        };
    }
}
