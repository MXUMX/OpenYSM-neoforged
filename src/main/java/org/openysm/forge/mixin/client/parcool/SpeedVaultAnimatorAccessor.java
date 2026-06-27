/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.impl.SpeedVaultAnimator
 *  com.alrex.parcool.client.animation.impl.SpeedVaultAnimator$Type
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.impl.SpeedVaultAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SpeedVaultAnimator.class})
public interface SpeedVaultAnimatorAccessor {
    @Accessor(value="type", remap=false)
    public SpeedVaultAnimator.Type getVaultType();
}

