/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 */
package rip.ysm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import rip.ysm.gui.Option;

public abstract class OptionRow<T>
extends AbstractWidget {
    protected final Option<T> option;

    protected OptionRow(int x, int y, int width, int height, Option<T> option) {
        super(x, y, width, height, (Component)(option == null ? Component.empty() : option.getLabel()));
        this.option = option;
    }

    public Option<T> getOption() {
        return this.option;
    }

    public void refresh() {
    }

    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean dirty;
        boolean bl = dirty = this.option != null && this.option.isDirty();
        int bg = this.isHoveredOrFocused() ? -1877534953 : (dirty ? -1878653434 : -1879048192);
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bg);
        Component label = this.getMessage();
        int textColor = dirty ? -1 : -1862270977;
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        g.drawString(Minecraft.getInstance().font, label, this.getX() + 8, textY, textColor, false);
        this.renderControl(g, mouseX, mouseY, partialTick);
    }

    protected abstract void renderControl(GuiGraphics var1, int var2, int var3, float var4);

    protected int controlX() {
        return this.getX() + this.getWidth() - this.controlWidth() - 6;
    }

    protected int controlY() {
        return this.getY() + (this.getHeight() - this.controlHeight()) / 2;
    }

    protected int controlWidth() {
        return 90;
    }

    protected int controlHeight() {
        return Math.min(this.getHeight() - 4, 16);
    }

    protected boolean isMouseOverControl(double mx, double my) {
        int cx = this.controlX();
        int cy = this.controlY();
        return mx >= (double)cx && mx < (double)(cx + this.controlWidth()) && my >= (double)cy && my < (double)(cy + this.controlHeight());
    }

    public boolean isOverlayOpen() {
        return false;
    }

    public void closeOverlay() {
    }

    public void renderOverlay(GuiGraphics g, int mouseX, int mouseY, float partialTick, float scrollDisplay) {
    }

    public boolean overlayMouseClicked(double mouseX, double mouseY, int button, float scrollDisplay) {
        return false;
    }

    public boolean overlayMouseScrolled(double mouseX, double mouseY, double delta, float scrollDisplay) {
        return false;
    }

    protected static int blendBg(boolean hover, int base) {
        if (!hover) {
            return base;
        }
        int a = base >>> 24 & 0xFF;
        int r = Mth.clamp((int)((base >> 16 & 0xFF) + 40), (int)0, (int)255);
        int gn = Mth.clamp((int)((base >> 8 & 0xFF) + 40), (int)0, (int)255);
        int b = Mth.clamp((int)((base & 0xFF) + 40), (int)0, (int)255);
        return a << 24 | r << 16 | gn << 8 | b;
    }

    protected void updateWidgetNarration(NarrationElementOutput out) {
        this.defaultButtonNarrationText(out);
    }
}

