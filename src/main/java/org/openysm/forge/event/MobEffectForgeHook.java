/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Added
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Expired
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Remove
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 */
package org.openysm.forge.event;

import net.neoforged.bus.api.SubscribeEvent;
public final class MobEffectForgeHook {
    private MobEffectForgeHook() {
    }

    @SubscribeEvent
    public static void onEffectAdded(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Added event) {
        org.openysm.client.event.MobEffectEvent.onEffectAdded(event);
    }

    @SubscribeEvent
    public static void onEffectRemoved(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove event) {
        org.openysm.client.event.MobEffectEvent.onEffectRemoved(event);
    }

    @SubscribeEvent
    public static void onEffectExpired(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Expired event) {
        org.openysm.client.event.MobEffectEvent.onEffectExpired(event);
    }
}
