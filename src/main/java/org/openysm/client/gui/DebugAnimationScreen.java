package org.openysm.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DebugAnimationScreen extends Screen {
    private final Screen parent;

    public DebugAnimationScreen(Screen parent) {
        super(Component.translatable("gui.openysm.debug_animation"));
        this.parent = parent;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
