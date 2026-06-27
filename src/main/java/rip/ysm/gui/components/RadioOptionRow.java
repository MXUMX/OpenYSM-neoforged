/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.Mth
 */
package rip.ysm.gui.components;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import rip.ysm.gui.Option;
import rip.ysm.gui.OptionRow;

public class RadioOptionRow
extends OptionRow<Integer> {
    private final List<String> labels;
    private boolean open;
    private float listScroll;

    public RadioOptionRow(int x, int y, int width, int height, Option<Integer> option, List<String> labels) {
        super(x, y, width, height, option);
        this.labels = labels;
    }

    @Override
    protected int controlWidth() {
        return Mth.clamp((int)(this.getWidth() / 2), (int)100, (int)220);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int cx = this.controlX();
        int cy = this.controlY();
        int cw = this.controlWidth();
        int ch = this.controlHeight();
        boolean hover = this.isMouseOverControl(mouseX, mouseY);
        g.fill(cx, cy, cx + cw, cy + ch, RadioOptionRow.blendBg(hover, 1053345992));
        g.renderOutline(cx, cy, cw, ch, 0x60FFFFFF);
        MutableComponent text = Component.literal((String)this.labelAt(this.currentIndex()));
        g.drawString(Minecraft.getInstance().font, (Component)text, cx + 6, cy + (ch - 8) / 2, -1, false);
        int arrowX = cx + cw - 10;
        int arrowY = cy + ch / 2 - 1;
        g.fill(arrowX, arrowY, arrowX + 6, arrowY + 1, -3355444);
        g.fill(arrowX + 1, arrowY + 1, arrowX + 5, arrowY + 2, -3355444);
        g.fill(arrowX + 2, arrowY + 2, arrowX + 4, arrowY + 3, -3355444);
    }

    public void onClick(double mouseX, double mouseY) {
        int firstVisible;
        int cur;
        if (!this.isMouseOverControl(mouseX, mouseY)) {
            return;
        }
        boolean bl = this.open = !this.open;
        if (this.open && ((cur = this.currentIndex()) < (firstVisible = (int)(this.listScroll / 14.0f)) || cur >= firstVisible + 8)) {
            this.listScroll = Math.max(0, Math.min(cur, this.labels.size() - 8)) * 14;
        }
    }

    @Override
    public boolean isOverlayOpen() {
        return this.open;
    }

    @Override
    public void closeOverlay() {
        this.open = false;
    }

    @Override
    public void renderOverlay(GuiGraphics g, int mouseX, int mouseY, float partialTick, float scrollDisplay) {
        int idx;
        if (!this.open || this.labels.isEmpty()) {
            return;
        }
        int cx = this.controlX();
        int cw = this.controlWidth();
        int cy = this.controlY() - (int)scrollDisplay;
        int ch = this.controlHeight();
        int visible = Math.min(8, this.labels.size());
        int listH = visible * 14 + 2;
        int listX = cx;
        int listY = cy + ch;
        g.pose().pushPose();
        g.pose().translate(0.0f, 0.0f, 200.0f);
        g.fill(listX, listY, listX + cw, listY + listH, -15658735);
        int first = (int)(this.listScroll / 14.0f);
        first = Math.max(0, Math.min(first, Math.max(0, this.labels.size() - visible)));
        for (int i = 0; i < visible && (idx = first + i) < this.labels.size(); ++i) {
            int bg;
            boolean selected;
            int itemY = listY + 1 + i * 14;
            boolean hover = mouseX >= listX && mouseX < listX + cw && mouseY >= itemY && mouseY < itemY + 14;
            boolean bl = selected = idx == this.currentIndex();
            bg = selected ? new Color(255, 255, 255, 60).getRGB() : (hover ? -13421773 : 0);
            if (bg != 0) {
                g.fill(listX + 1, itemY, listX + cw - 1, itemY + 14, bg);
            }
            g.drawString(Minecraft.getInstance().font, (Component)Component.literal((String)this.labelAt(idx)), listX + 6, itemY + 3, -1, true);
        }
        if (this.labels.size() > visible) {
            int trackX = listX + cw - 3;
            int trackTop = listY + 1;
            int trackBot = listY + listH - 1;
            int trackH = trackBot - trackTop;
            int thumbH = Math.max(8, trackH * visible / this.labels.size());
            int thumbY = trackTop + (int)((float)(trackH - thumbH) * this.listScroll / (float)Math.max(1, (this.labels.size() - visible) * 14));
            g.fill(trackX, trackTop, trackX + 2, trackBot, -2143009724);
            g.fill(trackX, thumbY, trackX + 2, thumbY + thumbH, -5592406);
        }
        g.pose().popPose();
    }

    @Override
    public boolean overlayMouseClicked(double mouseX, double mouseY, int button, float scrollDisplay) {
        int slot;
        if (!this.open) {
            return false;
        }
        int cx = this.controlX();
        int cw = this.controlWidth();
        int cy = this.controlY() - (int)scrollDisplay;
        int ch = this.controlHeight();
        int visible = Math.min(8, this.labels.size());
        int listH = visible * 14 + 2;
        int listX = cx;
        int listY = cy + ch;
        if (mouseX < (double)listX || mouseX >= (double)(listX + cw) || mouseY < (double)listY || mouseY >= (double)(listY + listH)) {
            return false;
        }
        int first = (int)(this.listScroll / 14.0f);
        int idx = (first = Math.max(0, Math.min(first, Math.max(0, this.labels.size() - visible)))) + (slot = (int)((mouseY - (double)listY - 1.0) / 14.0));
        if (idx >= 0 && idx < this.labels.size()) {
            this.option.setPending(idx);
            this.open = false;
        }
        return true;
    }

    @Override
    public boolean overlayMouseScrolled(double mouseX, double mouseY, double delta, float scrollDisplay) {
        if (!this.open) {
            return false;
        }
        int cx = this.controlX();
        int cw = this.controlWidth();
        int cy = this.controlY() - (int)scrollDisplay;
        int ch = this.controlHeight();
        int visible = Math.min(8, this.labels.size());
        int listH = visible * 14 + 2;
        int listX = cx;
        int listY = cy + ch;
        if (mouseX < (double)listX || mouseX >= (double)(listX + cw) || mouseY < (double)listY || mouseY >= (double)(listY + listH)) {
            return false;
        }
        int max = Math.max(0, (this.labels.size() - visible) * 14);
        this.listScroll = (float)Math.max(0.0, Math.min((double)max, (double)this.listScroll - delta * 14.0));
        return true;
    }

    private String labelAt(int idx) {
        if (idx < 0 || idx >= this.labels.size()) {
            return "";
        }
        return this.labels.get(idx);
    }

    private int currentIndex() {
        Integer cur = (Integer)this.option.get();
        if (cur == null) {
            return 0;
        }
        return Mth.clamp((int)cur, (int)0, (int)Math.max(0, this.labels.size() - 1));
    }
}
