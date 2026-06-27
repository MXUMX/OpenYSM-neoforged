package org.openysm.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigCheckBoxForge extends AbstractButton {

    private final ModConfigSpec.BooleanValue forgeConfigSpec;
    private boolean selected;

    public ConfigCheckBoxForge(int x, int y, String str, ModConfigSpec.BooleanValue booleanValue) {
        super(x, y, 400, 20, Component.translatable("gui.openysm.config." + str));
        this.forgeConfigSpec = booleanValue;
        this.selected = booleanValue.get();
    }

    @Override
    public void onPress() {
        this.selected = !this.selected;
        this.forgeConfigSpec.set(this.selected);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Component marker = Component.literal(this.selected ? "[x] " : "[ ] ").append(this.getMessage());
        guiGraphics.drawString(Minecraft.getInstance().font, marker, this.getX(), this.getY() + 6, this.active ? 0xFFFFFF : 0xA0A0A0);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
