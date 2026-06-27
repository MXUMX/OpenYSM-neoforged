package org.openysm.client.gui;

import org.openysm.model.ServerModelManager;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OpenModelFolderScreen extends Screen {

    private final PlayerModelScreen parentScreen;

    public OpenModelFolderScreen(PlayerModelScreen modelScreen) {
        super(Component.literal("Open Model Folder"));
        this.parentScreen = modelScreen;
    }

    public void init() {
        int x = (this.width - 310) / 2;
        int y = (this.height / 2) + 60;
        clearWidgets();
        addRenderableWidget(Button.builder(Component.translatable("gui.openysm.open_model_folder.open"), button -> {
            Util.getPlatform().openFile(ServerModelManager.CUSTOM.toFile());
        }).bounds(x, y, 150, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.openysm.model.return"), button2 -> {
            getMinecraft().setScreen(this.parentScreen);
        }).bounds(x + 160, y, 150, 20).build());
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 1000.0f);
        guiGraphics.drawWordWrap(this.font, Component.translatable("gui.openysm.open_model_folder.tips"), (this.width - 400) / 2, (this.height / 2) - 80, 400, 16777215);
        guiGraphics.pose().popPose();
    }
}
