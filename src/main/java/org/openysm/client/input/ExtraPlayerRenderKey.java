package org.openysm.client.input;

import org.openysm.OpenYSM;
import org.openysm.client.gui.ExtraPlayerRenderScreen;
import org.openysm.util.InputUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent;
public class ExtraPlayerRenderKey {

    public static final KeyMapping KEY_MAPPING = new KeyMapping("key.openysm.open_extra_player_render.desc", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM, 80, "key.category.openysm");

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OpenYSM.isAvailable() && InputUtil.isPlayerReady() && event.getAction() == 1 && InputUtil.isKeyPressed(event, KEY_MAPPING)) {
            Minecraft.getInstance().setScreen(new ExtraPlayerRenderScreen());
        }
    }
}