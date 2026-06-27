package org.openysm.client.input;

import org.openysm.OpenYSM;
import org.openysm.client.renderer.AnimationDebugOverlay;
import org.openysm.util.InputUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent;
public class DebugAnimationKey {

    public static final KeyMapping KEY_MAPPING = new KeyMapping("key.openysm.debug_animation.desc", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM, 66, "key.category.openysm");

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OpenYSM.isAvailable() && InputUtil.isPlayerReady() && event.getAction() == 1 && InputUtil.isKeyPressed(event, KEY_MAPPING)) {
            if (!AnimationDebugOverlay.isDebugActive()) {
                AnimationDebugOverlay.tryUpdateFromHitResult();
            } else {
                AnimationDebugOverlay.clearActiveModel();
            }
        }
    }
}