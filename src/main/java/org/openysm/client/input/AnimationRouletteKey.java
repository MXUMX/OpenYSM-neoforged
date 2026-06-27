package org.openysm.client.input;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.client.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import org.openysm.client.gui.AnimationRouletteScreen;
import org.openysm.client.model.ModelAssembly;
import org.openysm.config.ServerConfig;
import org.openysm.network.NetworkHandler;
import org.openysm.util.InputUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent;
public class AnimationRouletteKey {

    public static final KeyMapping KEY_ROULETTE = new KeyMapping("key.openysm.animation_roulette.desc", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, 90, "key.category.openysm");

    public static final KeyMapping KEY_LOCK = new KeyMapping("key.openysm.lock_roulette.desc", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM, 76, "key.category.openysm");

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OpenYSM.isAvailable() && InputUtil.isPlayerReady() && event.getAction() == 1 && InputUtil.isKeyPressed(event, KEY_ROULETTE)) {
            if (!NetworkHandler.isClientConnected() || ServerConfig.CAN_SWITCH_MODEL.get()) {
                if (TouhouLittleMaidCompat.isMaidChatAvailable()) {
                    TouhouLittleMaidCompat.openMaidChat();
                } else if (Minecraft.getInstance().player != null) {
                    org.openysm.capability.YSMCapabilities.get(Minecraft.getInstance().player, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
                        String modelId = cap.getModelId();
                        ModelAssembly modelAssembly = cap.getModelAssembly();
                        if (modelAssembly != null && !modelAssembly.getModelData().getModelProperties().getExtraAnimation().isEmpty()) {
                            if (Minecraft.getInstance().screen == null) {
                                Minecraft.getInstance().setScreen(new AnimationRouletteScreen(modelId, modelAssembly, cap));
                            } else if (Minecraft.getInstance().screen instanceof AnimationRouletteScreen) {
                                Minecraft.getInstance().setScreen(null);
                            }
                        }
                    });
                }
            }
        }
    }
}
