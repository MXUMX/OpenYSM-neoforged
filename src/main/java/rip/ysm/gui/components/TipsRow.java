/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.util.FormattedCharSequence
 */
package rip.ysm.gui.components;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import rip.ysm.gui.OptionRow;

public final class TipsRow
extends OptionRow<Object> {
    private final String text;
    private List<FormattedCharSequence> cachedLines;
    private int cachedWidth = -1;

    public TipsRow(String text) {
        super(0, 0, 0, 0, null);
        this.text = text;
    }

    private void recomputeLines() {
        if (this.cachedWidth == this.getWidth() && this.cachedLines != null) {
            return;
        }
        Font font = Minecraft.getInstance().font;
        this.cachedLines = font.split((FormattedText)Component.literal((String)this.text), Math.max(20, this.getWidth() - 16));
        this.cachedWidth = this.getWidth();
        this.setHeight(Math.max(18, this.cachedLines.size() * 10 + 8));
    }

    @Override
    public void setWidth(int w) {
        super.setWidth(w);
        this.cachedLines = null;
        this.cachedWidth = -1;
        this.recomputeLines();
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.recomputeLines();
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), -1879048192);
        Font font = Minecraft.getInstance().font;
        int y = this.getY() + 4;
        for (FormattedCharSequence line : this.cachedLines) {
            g.drawString(font, line, this.getX() + 8, y, -1118482, false);
            y += 10;
        }
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }
}
