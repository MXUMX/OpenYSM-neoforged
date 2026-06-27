package org.openysm.client.animation.predicate;

import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorHelper;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.animation.condition.ConditionArmor;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.molang.runtime.ExpressionEvaluator;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.StringUtils;

public class ArmorPredicate implements IAnimationPredicate<LivingAnimatable<?>> {

    private final EquipmentSlot slot;

    public ArmorPredicate(EquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public PlayState predicate(AnimationEvent<LivingAnimatable<?>> event, ExpressionEvaluator<?> evaluator) {
        LivingEntity entity = event.getAnimatable().getEntity();
        if (entity == null || (event.getAnimatable() instanceof IPreviewAnimatable)) {
            return PlayState.STOP;
        }
        if (CosmeticArmorHelper.getArmorItem(entity, this.slot).isEmpty()) {
            return PlayState.STOP;
        }
        ConditionArmor conditionArmor = event.getAnimatable().getModelConfig().getArmor();
        if (conditionArmor != null) {
            String name = conditionArmor.doTest(entity, this.slot);
            if (StringUtils.isNoneBlank(name)) {
                return IAnimationPredicate.playAnimationWithLoop(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        String defaultName = this.slot.getName() + ":default";
        if (event.getAnimatable().getAnimation(defaultName) != null) {
            return IAnimationPredicate.playAnimationWithLoop(event, defaultName, ILoopType.EDefaultLoopTypes.LOOP);
        }
        return PlayState.STOP;
    }
}