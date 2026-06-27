/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.neoforge.client.event.RenderArmEvent
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 */
package org.openysm.forge.event;

import org.openysm.client.event.ReplacePlayerHandRenderEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.bus.api.SubscribeEvent;
public final class ReplacePlayerHandRenderForgeHook {
    private ReplacePlayerHandRenderForgeHook() {
    }

    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent event) {
        ReplacePlayerHandRenderEvent.onRenderArm(event);
    }
}
