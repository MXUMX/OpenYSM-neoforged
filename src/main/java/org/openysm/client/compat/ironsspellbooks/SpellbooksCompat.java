package org.openysm.client.compat.ironsspellbooks;

import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.client.animation.molang.CtrlBinding;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;

public class SpellbooksCompat {

    private static final String MOD_ID = "irons_spellbooks";

    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = ModList.get().isLoaded(MOD_ID);
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static void registerBindings(CtrlBinding binding) {
        if (IS_LOADED) {
            SpellbookBinding.registerBindings(binding);
        } else {
            registerDummyBindings(binding);
        }
    }

    @Nullable
    public static PlayState resolvePlayState(AnimationEvent<LivingAnimatable<?>> event, LivingEntity entity) {
        if (IS_LOADED) {
            return SpellbookBinding.determinePlayState(event, entity);
        }
        return null;
    }

    private static void registerDummyBindings(CtrlBinding binding) {
        binding.clientPlayerEntityVar("iss_animation", ctx -> StringPool.EMPTY);
    }
}