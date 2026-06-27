/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.event.entity.living.LivingEvent$LivingTickEvent
 *  net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 */
package org.openysm.forge.event;

import org.openysm.client.event.ShieldBlockCooldownEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.bus.api.SubscribeEvent;
public final class ShieldBlockCooldownForgeHook {
    private ShieldBlockCooldownForgeHook() {
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        ShieldBlockCooldownEvent.onShieldBlock(event);
    }

    @SubscribeEvent
    public static void onLivingTick(net.neoforged.neoforge.event.tick.EntityTickEvent.Post event) {
        ShieldBlockCooldownEvent.onLivingTick(event);
    }
}
