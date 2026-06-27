/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.neoforge.client.event.RenderHandEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 */
package org.openysm.forge.event;

import org.openysm.client.event.RenderFirstPlayerBackground;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.bus.api.SubscribeEvent;
public final class RenderFirstPlayerForgeHook {
    private RenderFirstPlayerForgeHook() {
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        RenderFirstPlayerBackground.onRenderLevelStage(event);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        RenderFirstPlayerBackground.onRenderHand(event);
    }
}
