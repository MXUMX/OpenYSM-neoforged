package org.openysm.client.input;

import org.openysm.OpenYSM;
import org.openysm.client.gui.DisclaimerScreen;
import org.openysm.client.gui.ExtraPlayerConfigScreen;
import org.openysm.client.gui.PlayerModelScreen;
import org.openysm.config.GeneralConfig;
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
public class PlayerModelToggleKey {

    public static final KeyMapping KEY_MAPPING = new KeyMapping("key.openysm.player_model.desc", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM, 89, "key.category.openysm");

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (InputUtil.isPlayerReady() && event.getAction() == 1 && InputUtil.isKeyPressed(event, KEY_MAPPING)) {
            if (!OpenYSM.isAvailable()) {
                OpenYSM.sendUnavailableMessage();
                return;
            }
            if (NetworkHandler.isClientConnected() && !ServerConfig.CAN_SWITCH_MODEL.get()) {
                Minecraft.getInstance().setScreen(new ExtraPlayerConfigScreen(null));
            } else if (GeneralConfig.DISCLAIMER_SHOW.get()) {
                Minecraft.getInstance().setScreen(new DisclaimerScreen());
            } else {
                Minecraft.getInstance().setScreen(new PlayerModelScreen());
            }
        }
    }
}