/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.impl.RollAnimator
 *  com.alrex.parcool.common.action.impl.Roll$Direction
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.impl.RollAnimator;
import com.alrex.parcool.common.action.impl.Roll;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={RollAnimator.class})
public interface RollAnimatorAccessor {
    @Accessor(value="direction", remap=false)
    public Roll.Direction getRollDirection();
}

