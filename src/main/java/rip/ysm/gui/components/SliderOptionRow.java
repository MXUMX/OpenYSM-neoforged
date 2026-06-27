/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 */
package rip.ysm.gui.components;

import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import rip.ysm.gui.Option;
import rip.ysm.gui.OptionRow;

public class SliderOptionRow
extends OptionRow<Double> {
    private final double min;
    private final double max;
    private final double step;
    private final String suffix;
    private final DecimalFormat format;
    private boolean dragging;

    public SliderOptionRow(int x, int y, int width, int height, Option<Double> option, double min, double max, double step, String suffix) {
        super(x, y, width, height, option);
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        this.step = Math.max(step, 0.0);
        this.suffix = suffix == null ? "" : suffix;
        this.format = this.step >= 1.0 ? new DecimalFormat("0") : new DecimalFormat("0.0");
    }

    @Override
    protected int controlWidth() {
        return Mth.clamp((int)(this.getWidth() / 2), (int)100, (int)260);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int cx = this.controlX();
        int cy = this.controlY();
        int cw = this.controlWidth();
        int ch = this.controlHeight();
        boolean hover = this.isMouseOverControl(mouseX, mouseY) || this.dragging;
        g.fill(cx, cy, cx + cw, cy + ch, SliderOptionRow.blendBg(hover, 0x41000000));
        g.renderOutline(cx, cy, cw, ch, -1862270977);
        double value = this.option.get() == null ? this.min : (Double)this.option.get();
        double range = this.max - this.min;
        double t = range <= 0.0 ? 0.0 : Mth.clamp((double)((value - this.min) / range), (double)0.0, (double)1.0);
        int fillW = (int)(t * (double)(cw - 2));
        g.fill(cx + 1, cy + 1, cx + 1 + fillW, cy + ch - 1, 0x50FFFFFF);
        String text = this.format.format(value) + this.suffix;
        int tw = Minecraft.getInstance().font.width(text);
        g.drawString(Minecraft.getInstance().font, (Component)Component.literal((String)text), cx + (cw - tw) / 2, cy + (ch - 8) / 2, -1, true);
    }

    public void onClick(double mouseX, double mouseY) {
        if (this.isMouseOverControl(mouseX, mouseY)) {
            this.dragging = true;
            this.updateFromMouse(mouseX);
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dx, double dy) {
        if (this.dragging) {
            this.updateFromMouse(mouseX);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateFromMouse(double mouseX) {
        int cx = this.controlX();
        int cw = this.controlWidth();
        double t = Mth.clamp((double)((mouseX - (double)cx - 1.0) / (double)(cw - 2)), (double)0.0, (double)1.0);
        double raw = this.min + t * (this.max - this.min);
        if (this.step > 0.0) {
            raw = this.step * (double)Math.round(raw / this.step);
        }
        this.option.setPending(Mth.clamp((double)raw, (double)this.min, (double)this.max));
    }
}
