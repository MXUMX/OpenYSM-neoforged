package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.client.gui.DebugAnimationScreen;
import org.openysm.client.gui.PlayerModelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ModScreenEvent {

    private static final String IMC_METHOD = "DownloadScreen";

    @Nullable
    private static Screen receivedScreen;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onProcessIMC(InterModProcessEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        InterModComms.getMessages(OpenYSM.MOD_ID).findFirst().ifPresent(message -> {
            if (IMC_METHOD.equals(message.method())) {
                Object screenObj = message.messageSupplier().get();
                if (screenObj instanceof Screen screen) {
                    receivedScreen = screen;
                }
            }
        });
    }

    public static void openScreen(PlayerModelScreen modelScreen) {
        modelScreen.getMinecraft().setScreen(Objects.requireNonNullElseGet(receivedScreen, () -> {
            return new DebugAnimationScreen(modelScreen);
        }));
    }

    public static void setReceivedScreen(Screen screen) {
        receivedScreen = screen;
    }
}
