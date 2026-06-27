/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.impl.HorizontalWallRunAnimator
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.impl.HorizontalWallRunAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={HorizontalWallRunAnimator.class})
public interface HorizontalWallRunAnimatorAccessor {
    @Accessor(value="wallIsRightSide", remap=false)
    public boolean isRunningRight();
}

