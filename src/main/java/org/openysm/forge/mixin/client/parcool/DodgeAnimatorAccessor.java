/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.impl.DodgeAnimator
 *  com.alrex.parcool.common.action.impl.Dodge$DodgeDirection
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.impl.DodgeAnimator;
import com.alrex.parcool.common.action.impl.Dodge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={DodgeAnimator.class})
public interface DodgeAnimatorAccessor {
    @Accessor(value="direction", remap=false)
    public Dodge.DodgeDirection getDodgeDirection();
}

