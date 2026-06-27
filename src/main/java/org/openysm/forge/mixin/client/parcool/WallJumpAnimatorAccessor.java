/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.impl.WallJumpAnimator
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.impl.WallJumpAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={WallJumpAnimator.class})
public interface WallJumpAnimatorAccessor {
    @Accessor(value="wallRightSide", remap=false)
    public boolean isJumpingRight();
}

