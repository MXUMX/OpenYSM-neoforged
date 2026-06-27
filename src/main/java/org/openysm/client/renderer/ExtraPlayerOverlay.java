/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 */
package org.openysm.client.renderer;

import org.openysm.client.gui.ExtraPlayerRenderScreen;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.config.ExtraPlayerRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import rip.ysm.api.client.HudOverlay;

public class ExtraPlayerOverlay
implements HudOverlay {
    @Override
    public void render(GuiGraphics guiGraphics, Font font, float partialTick, int screenWidth, int screenHeight) {
        LocalPlayer localPlayer;
        Minecraft minecraft;
        block3: {
            block2: {
                if (((Boolean)ExtraPlayerRenderConfig.DISABLE_PLAYER_RENDER.get()).booleanValue()) break block2;
                minecraft = Minecraft.getInstance();
                localPlayer = minecraft.player;
                if (localPlayer != null && !(minecraft.screen instanceof ExtraPlayerRenderScreen)) break block3;
            }
            return;
        }
        ModelPreviewRenderer.renderPlayerOverlay(guiGraphics, localPlayer, ((Integer)ExtraPlayerRenderConfig.PLAYER_POS_X.get()).intValue(), ((Integer)ExtraPlayerRenderConfig.PLAYER_POS_Y.get()).intValue(), ((Double)ExtraPlayerRenderConfig.PLAYER_SCALE.get()).floatValue(), ((Double)ExtraPlayerRenderConfig.PLAYER_YAW_OFFSET.get()).floatValue(), -500, minecraft.getTimer().getGameTimeDeltaPartialTick(false));
    }
}
