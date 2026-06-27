/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package org.openysm.client.gui;

import org.openysm.client.gui.PlayerModelScreen;
import org.openysm.client.gui.button.FlatColorButton;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DownloadScreen
extends Screen {
    private final PlayerModelScreen parentScreen;
    private int guiLeft;
    private int guiTop;

    public DownloadScreen(PlayerModelScreen modelScreen) {
        super((Component)Component.literal((String)"YSM Config GUI"));
        this.parentScreen = modelScreen;
    }

    public void init() {
        this.guiLeft = (this.width - 420) / 2;
        this.guiTop = (this.height - 235) / 2;
        this.addRenderableWidget(new FlatColorButton(this.guiLeft + 5, this.guiTop, 80, 18, (Component)Component.translatable((String)"gui.openysm.model.return"), button -> Minecraft.getInstance().setScreen((Screen)this.parentScreen)));
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.fillGradient(this.guiLeft, this.guiTop, this.guiLeft + 420, this.guiTop + 235, -14540254, -14540254);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 1000.0f);
        guiGraphics.drawCenteredString(this.font, "Coming Soooooooooooooooooooooooooon\u2122", this.width / 2, this.height / 2 - 5, ChatFormatting.DARK_RED.getColor());
        guiGraphics.pose().popPose();
    }
}
