package org.openysm.client.input;

import org.openysm.OpenYSM;
import org.openysm.util.InputUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class InputStateKey {

    public static volatile boolean[] keyStates = new boolean[349];

    public static volatile boolean[] mouseStates = new boolean[8];

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OpenYSM.isAvailable() && InputUtil.isPlayerReady() && 32 <= event.getKey() && event.getKey() <= 348) {
            if (event.getAction() == 1) {
                keyStates[event.getKey()] = true;
            } else if (event.getAction() == 0) {
                keyStates[event.getKey()] = false;
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Post event) {
        if (OpenYSM.isAvailable() && InputUtil.isPlayerReady() && 0 <= event.getButton() && event.getButton() <= 7) {
            if (event.getAction() == 1) {
                mouseStates[event.getButton()] = true;
            } else if (event.getAction() == 0) {
                mouseStates[event.getButton()] = false;
            }
        }
    }
}