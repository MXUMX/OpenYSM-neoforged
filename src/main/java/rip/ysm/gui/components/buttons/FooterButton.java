/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 */
package rip.ysm.gui.components.buttons;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class FooterButton
extends AbstractWidget {
    private final Runnable onPress;

    public FooterButton(int x, int y, int width, int height, Component label, Runnable onPress) {
        super(x, y, width, height, label);
        this.onPress = onPress;
    }

    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int bg = !this.active ? -1876416472 : (this.isHoveredOrFocused() ? new Color(-1877534953, true).getRGB() : -1879048192);
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bg);
        int tw = Minecraft.getInstance().font.width((FormattedText)this.getMessage());
        int color = this.active ? -1 : -7829368;
        g.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + (this.getWidth() - tw) / 2, this.getY() + (this.getHeight() - 8) / 2, color, false);
    }

    public void onClick(double mouseX, double mouseY) {
        if (this.active) {
            this.onPress.run();
        }
    }

    protected void updateWidgetNarration(NarrationElementOutput out) {
        this.defaultButtonNarrationText(out);
    }
}

