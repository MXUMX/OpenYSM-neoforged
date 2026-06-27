package org.openysm.client.animation.molang.functions.ysm;

import org.openysm.client.compat.firstperson.FirstPersonCompat;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.variable.IValueEvaluator;
import org.openysm.util.CameraUtil;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.player.Player;

public class FirstPersonModHide implements IValueEvaluator<Boolean, IContext<Player>> {
    @Override
    public Boolean eval(IContext<Player> ctx) {
        if (!ctx.animationEvent().isFirstPerson() && FirstPersonCompat.isLoaded() && CameraUtil.getCameraType(ctx) == CameraType.FIRST_PERSON.ordinal()) {
            return FirstPersonCompat.shouldHideHead();
        }
        return false;
    }
}