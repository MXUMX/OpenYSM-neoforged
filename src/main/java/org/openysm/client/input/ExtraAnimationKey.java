package org.openysm.client.input;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.resource.models.ModelProperties;
import org.openysm.client.event.AnimationLockEvent;
import org.openysm.client.gui.AnimationRouletteScreen;
import org.openysm.client.model.ModelAssembly;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SPlayAnimationPacket;
import org.openysm.util.InputUtil;
import org.openysm.util.data.OrderedStringMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;
public class ExtraAnimationKey {

    public static final List<KeyMapping> KEY_MAPPINGS = Lists.newArrayList();

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        for (int i = 0; i <= 7; i++) {
            KeyMapping eventMapping = new KeyMapping(String.format("key.openysm.extra_animation.%d.desc", Integer.valueOf(i)), KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, -1, "key.category.openysm");
            event.register(eventMapping);
            KEY_MAPPINGS.add(eventMapping);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!OpenYSM.isAvailable() || !InputUtil.isPlayerReady()) {
            return;
        }
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        for (KeyMapping eventMapping : KEY_MAPPINGS) {
            if (event.getAction() == 1 && InputUtil.isKeyPressed(event, eventMapping) && localPlayer != null && !AnimationLockEvent.isPlayerMoving(localPlayer)) {
                org.openysm.capability.YSMCapabilities.get(localPlayer, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
                    ModelAssembly modelAssembly = cap.getModelAssembly();
                    int index = KEY_MAPPINGS.indexOf(eventMapping);
                    ModelProperties modelProperties = modelAssembly.getModelData().getModelProperties();
                    OrderedStringMap<String, String> map = modelProperties.getExtraAnimation();
                    if (map.size() > index) {
                        String rouletteKey = map.getKeyAt(index);
                        if ("#return".equals(rouletteKey)) {
                            NetworkHandler.sendToServer(C2SPlayAnimationPacket.createDefault());
                            return;
                        }
                        if (rouletteKey.startsWith("#") && modelProperties.getExtraAnimationClassify().containsKey(rouletteKey.substring(1))) {
                            AnimationRouletteScreen.setInitialSubmenu(rouletteKey.substring(1));
                            Minecraft.getInstance().setScreen(new AnimationRouletteScreen(modelProperties.getExtraAnimationButtons(), modelProperties.getExtraAnimationClassify(), modelAssembly, cap));
                            return;
                        }
                        NetworkHandler.sendToServer(new C2SPlayAnimationPacket(index, StringPool.EMPTY));
                    }
                });
                return;
            }
        }
    }
}
