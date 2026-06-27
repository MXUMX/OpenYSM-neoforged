/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.network.chat.FormattedText
 */
package rip.ysm.gui.components.buttons;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.FormattedText;
import rip.ysm.gui.OptionGroup;

public class TabButton
extends AbstractWidget {
    private final OptionGroup group;
    private final Consumer<OptionGroup> onSelect;
    private boolean selected;
    private boolean horizontal;

    public TabButton(int x, int y, int width, int height, OptionGroup group, Consumer<OptionGroup> onSelect) {
        super(x, y, width, height, group.getTitle());
        this.group = group;
        this.onSelect = onSelect;
    }

    public OptionGroup getGroup() {
        return this.group;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int bg = this.selected ? -1877534953 : (this.isHoveredOrFocused() ? -1878324469 : -1879048192);
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bg);
        if (this.selected) {
            if (this.horizontal) {
                g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + 1, -1);
            } else {
                g.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.getHeight(), -1);
            }
        }
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        if (this.horizontal) {
            int tw = Minecraft.getInstance().font.width((FormattedText)this.getMessage());
            int textX = this.getX() + Math.max(6, (this.getWidth() - tw) / 2);
            g.drawString(Minecraft.getInstance().font, this.getMessage(), textX, textY, -1, false);
        } else {
            g.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 10, textY, -1, false);
        }
    }

    public void onClick(double mouseX, double mouseY) {
        this.onSelect.accept(this.group);
    }

    protected void updateWidgetNarration(NarrationElementOutput out) {
        this.defaultButtonNarrationText(out);
    }
}

