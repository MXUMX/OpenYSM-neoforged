package org.openysm.client.animation.molang.functions.ysm;

import org.openysm.capability.PlayerCapability;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.ContextFunction;
import org.openysm.molang.runtime.ExecutionContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.alchemy.PotionContents;
import rip.ysm.neoforge.compat.registries.ForgeRegistries;

public class EffectLevel extends ContextFunction<Entity> {
    @Override
    public boolean validateArgumentSize(int size) {
        return size >= 1;
    }

    @Override
    public java.lang.Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        int effects = 0;

        for (int i = 0; i < arguments.size(); i++) {
            ResourceLocation effectId = arguments.getResourceLocation(context, i);
            if (effectId != null) {
                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);
                if (mobEffect != null) {
                    if (context.entity().geoInstance() instanceof PlayerCapability cap
                            && !cap.isLocalPlayerModel()) {
                        effects += cap.getPositionTracker().getEffectAmplifier(mobEffect);
                    } else if (((IContext<?>)context.entity()).entity() instanceof LivingEntity) {
                        MobEffectInstance mobEffectInstance = ((LivingEntity)((IContext<?>)context.entity()).entity())
                                .getEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(mobEffect));
                        if (mobEffectInstance != null) {
                            effects += mobEffectInstance.getAmplifier() + 1;
                        }
                    } else {
                        if (!(((IContext<?>)context.entity()).entity() instanceof Arrow)) {
                            return null;
                        }

                        Arrow arrow = (Arrow) ((IContext<?>) context.entity()).entity();
                        for (MobEffectInstance mobEffectInstance : arrow.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getAllEffects()) {
                            if (mobEffectInstance.getEffect().value() == mobEffect) {
                                effects += mobEffectInstance.getAmplifier() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return effects;
    }
}
