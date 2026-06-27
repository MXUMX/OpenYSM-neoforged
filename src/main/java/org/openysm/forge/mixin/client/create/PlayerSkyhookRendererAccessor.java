/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.simibubi.create.foundation.render.PlayerSkyhookRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package org.openysm.mixin.client.create;

import com.simibubi.create.foundation.render.PlayerSkyhookRenderer;
import java.util.Set;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PlayerSkyhookRenderer.class})
public interface PlayerSkyhookRendererAccessor {
    @Accessor(value="hangingPlayers", remap=false)
    public static Set<UUID> hangingPlayers() {
        throw new AssertionError();
    }
}

