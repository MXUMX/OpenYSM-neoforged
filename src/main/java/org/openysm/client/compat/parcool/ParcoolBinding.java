package org.openysm.client.compat.parcool;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.client.animation.molang.CtrlBinding;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ParcoolBinding {
    public static void registerBindings(CtrlBinding binding) {
        binding.livingEntityVar("parcool_state", ParcoolBinding::getParcoolState);
    }

    private static String getParcoolState(IContext<LivingEntity> context) {
        Entity entity = context.entity();
        if (entity instanceof Player player) {
            String animationName = ParcoolAnimationHandler.getParcoolAnimationName(player);
            if (animationName != null) {
                return animationName.substring("parcool:".length());
            }
        }
        return StringPool.EMPTY;
    }
}