/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 */
package rip.ysm.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import rip.ysm.gui.Option;
import rip.ysm.gui.OptionRow;

public class BooleanOptionRow
extends OptionRow<Boolean> {
    public BooleanOptionRow(int x, int y, int width, int height, Option<Boolean> option) {
        super(x, y, width, height, option);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int size = Math.min(this.controlHeight(), 14);
        int cx = this.controlX() + this.controlWidth() - size;
        int cy = this.controlY() + (this.controlHeight() - size) / 2;
        boolean value = (Boolean)this.option.get();
        boolean hover = this.isMouseOverControl(mouseX, mouseY);
        g.fill(cx, cy, cx + size, cy + size, BooleanOptionRow.blendBg(hover, -15066598));
        g.renderOutline(cx, cy, size, size, -1);
        if (value) {
            g.fill(cx + 3, cy + 3, cx + size - 3, cy + size - 3, -1);
        }
    }

    public void onClick(double mouseX, double mouseY) {
        if (this.isMouseOverControl(mouseX, mouseY)) {
            this.option.setPending((Boolean)this.option.get() == false);
        }
    }
}

