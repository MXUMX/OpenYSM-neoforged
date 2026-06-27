/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.tartaricacid.touhoulittlemaid.api.client.render.MaidRenderState
 *  com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid
 *  it.unimi.dsi.fastutil.objects.ReferenceArrayList
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 */
package org.openysm.client.animation.predicate;

import org.openysm.client.animation.AnimationState;
import org.openysm.client.animation.IAnimationPredicate;
import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapability;
import org.openysm.client.entity.IPreviewAnimatable;
import org.openysm.geckolib3.core.builder.ILoopType;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.molang.runtime.ExpressionEvaluator;
import com.github.tartaricacid.touhoulittlemaid.api.client.render.MaidRenderState;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.Objects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import rip.ysm.compat.gun.tacz.TacCompat;
import rip.ysm.compat.slashblade.SlashBladeCompat;

public class TouhouMaidAnimationPredicate
implements IAnimationPredicate<MaidCapability> {
    private static final ReferenceArrayList<AnimationState<EntityMaid, MaidCapability>>[] priorityHandlers = new ReferenceArrayList[5];

    public static void registerHandler(AnimationState<EntityMaid, MaidCapability> animationState) {
        priorityHandlers[animationState.getPriority()].add(animationState);
    }

    @Override
    public PlayState predicate(AnimationEvent<MaidCapability> event, ExpressionEvaluator<?> evaluator) {
        EntityMaid entity = (EntityMaid)event.getAnimatable().getEntity();
        if (entity == null || event.getAnimatable() instanceof IPreviewAnimatable) {
            return PlayState.STOP;
        }
        if (entity.renderState != MaidRenderState.ENTITY) {
            return PlayState.STOP;
        }
        Entity vehicle = entity.getVehicle();
        if (vehicle != null && vehicle.isAlive()) {
            return PlayState.STOP;
        }
        for (int i = 0; i <= 4; ++i) {
            for (AnimationState animationState : priorityHandlers[i]) {
                ILoopType loopType;
                if (!animationState.getPredicate().test(entity, event)) continue;
                String str = animationState.getAnimationName();
                PlayState playState = SlashBladeCompat.handleSlashBladeAnim((LivingEntity)entity, event, str, loopType = animationState.getLoopType());
                if (playState != null) {
                    return playState;
                }
                return Objects.requireNonNullElseGet(TacCompat.handleTaczAnimState((LivingEntity)entity, event, str, loopType), () -> IAnimationPredicate.playAnimationWithLoop(event, str, loopType));
            }
        }
        return PlayState.STOP;
    }

    static {
        for (int i = 0; i < priorityHandlers.length; ++i) {
            TouhouMaidAnimationPredicate.priorityHandlers[i] = new ReferenceArrayList(6);
        }
    }
}
