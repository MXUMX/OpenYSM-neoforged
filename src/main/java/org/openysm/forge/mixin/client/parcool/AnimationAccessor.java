/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alrex.parcool.client.animation.Animator
 *  com.alrex.parcool.common.attachment.client.Animation
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.parcool;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.common.attachment.client.Animation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Animation.class})
public interface AnimationAccessor {
    @Accessor(value="animator", remap=false)
    public Animator getAnimator();
}
