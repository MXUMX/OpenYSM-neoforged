/*
 * Decompiled with CFR 0.152.
 */
package org.openysm.event;

import org.openysm.client.event.AnimationLockEvent;
import org.openysm.client.event.ClientPlayerCloneEvent;
import org.openysm.client.event.ClientPlayerJoinNotification;
import org.openysm.client.event.ClientSetupEvent;
import org.openysm.client.event.ClientTickEvent;
import org.openysm.client.event.PlayerSkinTextureManager;
import org.openysm.client.input.AnimationRouletteKey;
import org.openysm.client.input.DebugAnimationKey;
import org.openysm.client.input.ExtraAnimationKey;
import org.openysm.client.input.ExtraPlayerRenderKey;
import org.openysm.client.input.InputStateKey;
import org.openysm.client.input.PlayerModelToggleKey;
import org.openysm.client.renderer.RendererManager;
import org.openysm.event.CapabilityEvent;
import org.openysm.event.CommandRegistry;
import org.openysm.event.CommonEvent;
import org.openysm.event.EnterServerEvent;
import org.openysm.event.EntityJoinCallbackEvent;
import org.openysm.event.PlayerLogoutEvent;
import org.openysm.event.ServerStartupEvent;
import org.openysm.forge.event.MobEffectForgeHook;
import org.openysm.forge.event.ModScreenForgeHook;
import org.openysm.forge.event.RenderFirstPlayerForgeHook;
import org.openysm.forge.event.ReplacePlayerHandRenderForgeHook;
import org.openysm.forge.event.ReplacePlayerRenderForgeHook;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import rip.ysm.api.PlatformAPI;

public final class YsmEventBootstrap {
    private YsmEventBootstrap() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.register(CommonEvent.class);

        NeoForge.EVENT_BUS.register(ServerStartupEvent.class);
        NeoForge.EVENT_BUS.register(EnterServerEvent.class);
        NeoForge.EVENT_BUS.register(PlayerLogoutEvent.class);
        NeoForge.EVENT_BUS.register(CommandRegistry.class);
        NeoForge.EVENT_BUS.register(CapabilityEvent.class);
        NeoForge.EVENT_BUS.register(MobEffectForgeHook.class);
        if (!PlatformAPI.isServer()) {
            modEventBus.register(ClientSetupEvent.class);
            modEventBus.register(ModScreenForgeHook.class);

            NeoForge.EVENT_BUS.register(EntityJoinCallbackEvent.class);
            NeoForge.EVENT_BUS.register(ClientTickEvent.class);
            NeoForge.EVENT_BUS.register(ClientPlayerJoinNotification.class);
            NeoForge.EVENT_BUS.register(ClientPlayerCloneEvent.class);
            NeoForge.EVENT_BUS.register(AnimationLockEvent.class);
            NeoForge.EVENT_BUS.register(PlayerSkinTextureManager.class);
            NeoForge.EVENT_BUS.register(RendererManager.class);
            NeoForge.EVENT_BUS.register(PlayerModelToggleKey.class);
            NeoForge.EVENT_BUS.register(AnimationRouletteKey.class);
            NeoForge.EVENT_BUS.register(DebugAnimationKey.class);
            NeoForge.EVENT_BUS.register(ExtraPlayerRenderKey.class);
            NeoForge.EVENT_BUS.register(ExtraAnimationKey.class);
            NeoForge.EVENT_BUS.register(InputStateKey.class);
            NeoForge.EVENT_BUS.register(ReplacePlayerRenderForgeHook.class);
            NeoForge.EVENT_BUS.register(ReplacePlayerHandRenderForgeHook.class);
            NeoForge.EVENT_BUS.register(RenderFirstPlayerForgeHook.class);
        }
    }
}
