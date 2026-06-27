package org.openysm.client.event;

import org.openysm.OpenYSM;
import org.openysm.event.api.SpecialPlayerRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

public class PlayerSkinTextureManager {

    private static final ResourceLocation STEVE_SKIN = ResourceLocation.parse("textures/entity/player/wide/steve.png");

    private static final ResourceLocation ALEX_SKIN = ResourceLocation.parse("textures/entity/player/slim/alex.png");

    private static final String STEVE_TEXTURE_ID = "misc/2_steve";

    private static final String ALEX_TEXTURE_ID = "misc/1_alex";

    @SubscribeEvent
    public static void onRenderTexture(SpecialPlayerRenderEvent event) {
        ResourceLocation location;
        if (!OpenYSM.isAvailable()) {
            return;
        }
        Player player = event.getPlayer();
        if (isDefaultSkin(event.getModelId()) && (player instanceof AbstractClientPlayer abstractClientPlayer)) {
            location = abstractClientPlayer.getSkin().texture();
            if (location == null) {
                location = getSkinTexture(event.getModelId());
            }
            event.setTextureLocation(location);
        }
    }

    private static boolean isDefaultSkin(String str) {
        return str.equals(STEVE_TEXTURE_ID) || str.equals(ALEX_TEXTURE_ID);
    }

    private static ResourceLocation getSkinTexture(String str) {
        return str.equals(STEVE_TEXTURE_ID) ? STEVE_SKIN : ALEX_SKIN;
    }
}
