package org.openysm.client.gui;

import org.openysm.config.GeneralConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Objects;

public class DisclaimerScreen extends Screen {

    private Checkbox checkbox;

    private int textY;

    private int textHeight;

    public DisclaimerScreen() {
        super(Component.literal("Disclaimer GUI"));
    }

    public void init() {
        clearWidgets();
        int size = this.font.split(Component.translatable("gui.openysm.disclaimer.text"), 400).size();
        Objects.requireNonNull(this.font);
        int i = (size * 9) + 20 + 20 + 10 + 20;
        this.textY = (this.width - 400) / 2;
        this.textHeight = (this.height - i) / 2;
        MutableComponent mutableComponentTranslatable = Component.translatable("gui.openysm.disclaimer.read");
        int iWidth = this.font.width(mutableComponentTranslatable);
        this.checkbox = Checkbox.builder(mutableComponentTranslatable, this.font).pos((this.width - iWidth) / 2, (this.textHeight + i) - 50).selected(!GeneralConfig.DISCLAIMER_SHOW.get().booleanValue()).maxWidth(iWidth).build();
        addRenderableWidget(this.checkbox);
        addRenderableWidget(new Button.Builder(Component.translatable("gui.openysm.disclaimer.close"), button -> {
            if (this.checkbox.selected()) {
                GeneralConfig.DISCLAIMER_SHOW.set(false);
                Minecraft.getInstance().setScreen(new PlayerModelScreen());
            } else {
                Minecraft.getInstance().setScreen(null);
            }
        }).size(300, 20).pos((this.width - 300) / 2, (this.textHeight + i) - 20).build());
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 1000.0f);
        guiGraphics.drawWordWrap(this.font, Component.translatable("gui.openysm.disclaimer.text"), this.textY, this.textHeight, 400, -1);
        guiGraphics.pose().popPose();
    }
}
