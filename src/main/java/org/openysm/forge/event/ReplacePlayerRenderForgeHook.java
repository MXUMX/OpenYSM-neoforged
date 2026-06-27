/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.neoforge.client.event.RenderPlayerEvent$Pre
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 */
package org.openysm.forge.event;

import org.openysm.client.event.ReplacePlayerRenderEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
public final class ReplacePlayerRenderForgeHook {
    private ReplacePlayerRenderForgeHook() {
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        ReplacePlayerRenderEvent.onRenderPlayerPre(event);
    }
}
