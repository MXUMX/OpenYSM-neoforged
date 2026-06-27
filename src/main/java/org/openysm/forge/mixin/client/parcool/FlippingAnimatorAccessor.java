/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.impl.FlippingAnimator
 *  com.alrex.parcool.common.action.impl.Flipping$Direction
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.impl.FlippingAnimator;
import com.alrex.parcool.common.action.impl.Flipping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={FlippingAnimator.class})
public interface FlippingAnimatorAccessor {
    @Accessor(value="direction", remap=false)
    public Flipping.Direction getFlippingDirection();
}

