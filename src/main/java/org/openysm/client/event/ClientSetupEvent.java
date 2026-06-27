package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.client.ClientModelManager;
import org.openysm.client.compat.gun.tacz.TacCompat;
import org.openysm.client.compat.immersiveaircraft.ImmersiveAirCraftCompat;
import org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat;
import org.openysm.client.compat.ironsspellbooks.SpellbooksCompat;
import org.openysm.client.animation.AnimationRegister;
import org.openysm.client.compat.acceleratedrendering.AcceleratedRenderingCompat;
import org.openysm.client.compat.bettercombat.BetterCombatCompat;
import org.openysm.client.compat.carryon.CarryOnCompat;
import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorCompat;
import org.openysm.client.compat.curios.CuriosCompat;
import org.openysm.client.compat.elytraslot.ElytraSlotCompat;
import org.openysm.client.compat.firstperson.FirstPersonCompat;
import org.openysm.client.compat.gun.swarfare.SWarfareCompat;
import org.openysm.client.compat.oculus.OculusCompat;
import org.openysm.client.compat.optifine.OptiFineDetector;
import org.openysm.client.compat.parcool.ParcoolCompat;
import org.openysm.client.compat.playeranimator.PlayerAnimatorCompat;
import org.openysm.client.compat.realcamera.RealCameraCompat;
import org.openysm.client.compat.simplehats.SimpleHatsHelper;
import org.openysm.client.compat.slashblade.SlashBladeCompat;
import org.openysm.client.compat.swem.SWEMCompat;
import org.openysm.client.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import org.openysm.client.input.ExtraAnimationKey;
import org.openysm.client.input.*;
import org.openysm.client.compat.sbackpack.SBackpackCompat;
import org.openysm.client.renderer.*;
import org.openysm.client.compat.simpleplanes.SimplePlanesCompat;
import org.openysm.client.compat.create.CreateCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.Optional;
public class ClientSetupEvent {
    public static Object nativeClientInit() {
        try {
            int maxTexSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
            if (maxTexSize <= 0) {
                return Component.literal("YSM: OpenGL context not available");
            }
            // 原始C++碼檢查了GL20（著色器）和 GL30（VAO）的可用性
            try {
                int testShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
                if (testShader != 0) {
                    GL20.glDeleteShader(testShader);
                }
            } catch (Exception e) {
                return Component.literal("YSM: GL20 (shaders) not available");
            }

            // 预載入default模型，延遲至第一次渲染tick
            // 不能在FMLClientSetupEvent中同步執行ModelAssembler，會導致StackOverflow
            //ClientModelManager.schedulePreloadDefaultModel();
            return null; // 成功
        } catch (Exception e) {
            return Component.literal("YSM Client Init Failed: " + e.getMessage());
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        AnimationRegister.registerAnimationState();
        event.enqueueWork(() -> {
            CuriosCompat.init();
            FirstPersonCompat.init();
            RealCameraCompat.init();
            PlayerAnimatorCompat.init();
            BetterCombatCompat.init();
            OculusCompat.init();
            AcceleratedRenderingCompat.init();
            OptiFineDetector.init();
            CosmeticArmorCompat.init();
            ElytraSlotCompat.init();
            TacCompat.init();
            SWarfareCompat.init();
            TouhouLittleMaidCompat.init();
            CarryOnCompat.init();
            ParcoolCompat.init();
            SlashBladeCompat.init();
            SWEMCompat.init();
            CreateCompat.init();
            SBackpackCompat.init();
            SimpleHatsHelper.init();
            ImmersiveMelodiesCompat.init();
            SpellbooksCompat.init();
            SimplePlanesCompat.init();
            ImmersiveAirCraftCompat.init();
            showInCompatibleMod(SBackpackCompat.getInCompatibleInfo());
            showInCompatibleMod(ParcoolCompat.getInCompatibleInfo());
            showInCompatibleMod("epicfight", "Epic Fight");
            ClientModelManager.loadDefaultModel();
            checkNativeInitialization();
        });
    }

    private static void showInCompatibleMod(Optional<Pair<String, String>> optional) {
        optional.ifPresent(pair -> OpenYSM.LOGGER.warn("Incompatible optional mod version: {} {}", pair.getKey(), pair.getValue()));
    }

    private static void showInCompatibleMod(String str, String str2) {
        OpenYSM.LOGGER.warn("Incompatible optional mod detected: {}", str2);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(PlayerModelToggleKey.KEY_MAPPING);
        if (!OpenYSM.isAvailable()) {
            return;
        }
        event.register(AnimationRouletteKey.KEY_ROULETTE);
        event.register(AnimationRouletteKey.KEY_LOCK);
        event.register(DebugAnimationKey.KEY_MAPPING);
        event.register(ExtraPlayerRenderKey.KEY_MAPPING);
        ExtraAnimationKey.registerKeyMappings(event);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        event.registerAbove(VanillaGuiLayers.DEBUG_OVERLAY, ysmLayer("debug_info"), AnimationDebugOverlay.createOverlay());
        event.registerAbove(VanillaGuiLayers.DEBUG_OVERLAY, ysmLayer("extra_player"), (guiGraphics, deltaTracker) -> new ExtraPlayerOverlay().render(guiGraphics, net.minecraft.client.Minecraft.getInstance().font, deltaTracker.getGameTimeDeltaPartialTick(false), guiGraphics.guiWidth(), guiGraphics.guiHeight()));
        event.registerAbove(VanillaGuiLayers.DEBUG_OVERLAY, ysmLayer("loading_state"), new ModelSyncStateOverlay());
    }

    private static ResourceLocation ysmLayer(String path) {
        return ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, path);
    }

    private static void checkNativeInitialization() {
        Component component = (Component) nativeClientInit();
        if (component != null) {
            throw new RuntimeException("YSM Client Initialization Failed: " + component.getString(256));
        }
    }

    // 這裡本來有一個native方法，可能是運行時會初始化載入模型
}
