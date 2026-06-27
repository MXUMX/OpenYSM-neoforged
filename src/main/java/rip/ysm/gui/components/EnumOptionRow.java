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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import rip.ysm.gui.Option;
import rip.ysm.gui.OptionRow;

public class EnumOptionRow<E extends Enum<E>>
extends OptionRow<E> {
    private final E[] values;
    private boolean open;
    private float listScroll;

    public EnumOptionRow(int x, int y, int width, int height, Option<E> option, E[] values) {
        super(x, y, width, height, option);
        this.values = values;
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
        g.fill(cx, cy, cx + cw, cy + ch, EnumOptionRow.blendBg(hover, 1053345992));
        g.renderOutline(cx, cy, cw, ch, 0x60FFFFFF);
        MutableComponent text = Component.literal((String)EnumOptionRow.prettify(((Enum)this.option.get()).name()));
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
            this.listScroll = Math.max(0, Math.min(cur, this.values.length - 8)) * 14;
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
        if (!this.open) {
            return;
        }
        int cx = this.controlX();
        int cw = this.controlWidth();
        int cy = this.controlY() - (int)scrollDisplay;
        int ch = this.controlHeight();
        int visible = Math.min(8, this.values.length);
        int listH = visible * 14 + 2;
        int listX = cx;
        int listY = cy + ch;
        g.pose().pushPose();
        g.pose().translate(0.0f, 0.0f, 200.0f);
        g.fill(listX, listY, listX + cw, listY + listH, -15658735);
        int first = (int)(this.listScroll / 14.0f);
        first = Math.max(0, Math.min(first, Math.max(0, this.values.length - visible)));
        for (int i = 0; i < visible && (idx = first + i) < this.values.length; ++i) {
            int bg;
            boolean selected;
            int itemY = listY + 1 + i * 14;
            boolean hover = mouseX >= listX && mouseX < listX + cw && mouseY >= itemY && mouseY < itemY + 14;
            boolean bl = selected = idx == this.currentIndex();
            bg = selected ? new Color(255, 255, 255, 60).getRGB() : (hover ? -13421773 : 0);
            if (bg != 0) {
                g.fill(listX + 1, itemY, listX + cw - 1, itemY + 14, bg);
            }
            g.drawString(Minecraft.getInstance().font, (Component)Component.literal((String)EnumOptionRow.prettify(((Enum)this.values[idx]).name())), listX + 6, itemY + 3, -1, true);
        }
        if (this.values.length > visible) {
            int trackX = listX + cw - 3;
            int trackTop = listY + 1;
            int trackBot = listY + listH - 1;
            int trackH = trackBot - trackTop;
            int thumbH = Math.max(8, trackH * visible / this.values.length);
            int thumbY = trackTop + (int)((float)(trackH - thumbH) * this.listScroll / (float)Math.max(1, (this.values.length - visible) * 14));
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
        int visible = Math.min(8, this.values.length);
        int listH = visible * 14 + 2;
        int listX = cx;
        int listY = cy + ch;
        if (mouseX < (double)listX || mouseX >= (double)(listX + cw) || mouseY < (double)listY || mouseY >= (double)(listY + listH)) {
            return false;
        }
        int first = (int)(this.listScroll / 14.0f);
        int idx = (first = Math.max(0, Math.min(first, Math.max(0, this.values.length - visible)))) + (slot = (int)((mouseY - (double)listY - 1.0) / 14.0));
        if (idx >= 0 && idx < this.values.length) {
            this.option.setPending(this.values[idx]);
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
        int visible = Math.min(8, this.values.length);
        int listH = visible * 14 + 2;
        int listX = cx;
        int listY = cy + ch;
        if (mouseX < (double)listX || mouseX >= (double)(listX + cw) || mouseY < (double)listY || mouseY >= (double)(listY + listH)) {
            return false;
        }
        int max = Math.max(0, (this.values.length - visible) * 14);
        this.listScroll = (float)Math.max(0.0, Math.min((double)max, (double)this.listScroll - delta * 14.0));
        return true;
    }

    private static String prettify(String name) {
        String[] parts = name.split("_");
        StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < parts.length; ++i) {
            String p = parts[i];
            if (p.isEmpty()) continue;
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(p.charAt(0)));
            if (p.length() <= 1) continue;
            sb.append(p.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    private int currentIndex() {
        Enum current = (Enum)this.option.get();
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] != current) continue;
            return i;
        }
        return 0;
    }
}
