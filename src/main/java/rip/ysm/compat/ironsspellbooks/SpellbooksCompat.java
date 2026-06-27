/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 */
package rip.ysm.compat.ironsspellbooks;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import rip.ysm.compat.ironsspellbooks.forge.SpellbooksCompatImpl;

public final class SpellbooksCompat {
    private SpellbooksCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return SpellbooksCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerBindings(CtrlBinding binding) {
        SpellbooksCompatImpl.registerBindings(binding);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static PlayState resolvePlayState(AnimationEvent<LivingAnimatable<?>> event, LivingEntity entity) {
        return SpellbooksCompatImpl.resolvePlayState(event, entity);
    }
}
