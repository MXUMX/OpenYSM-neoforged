/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.compat.ironsspellbooks.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.ironsspellbooks.SpellbooksCompat;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import net.minecraft.world.entity.LivingEntity;

public final class SpellbooksCompatImpl {
    private SpellbooksCompatImpl() {
    }

    public static boolean isLoaded() {
        return SpellbooksCompat.isLoaded();
    }

    public static void registerBindings(CtrlBinding binding) {
        SpellbooksCompat.registerBindings(binding);
    }

    public static PlayState resolvePlayState(AnimationEvent<LivingAnimatable<?>> event, LivingEntity entity) {
        return SpellbooksCompat.resolvePlayState(event, entity);
    }
}

