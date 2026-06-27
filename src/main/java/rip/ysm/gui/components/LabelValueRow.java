/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 */
package rip.ysm.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import rip.ysm.gui.OptionRow;

public final class LabelValueRow
extends OptionRow<Object> {
    private final String labelKey;
    private final String value;

    public LabelValueRow(String labelKey, String value) {
        super(0, 0, 0, 18, null);
        this.labelKey = labelKey;
        this.value = value;
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), -1879048192);
        MutableComponent label = Component.translatable((String)this.labelKey).withStyle(ChatFormatting.AQUA);
        g.drawString(Minecraft.getInstance().font, (Component)label, this.getX() + 8, this.getY() + (this.getHeight() - 8) / 2, -1, false);
        int labelW = Minecraft.getInstance().font.width((FormattedText)label);
        g.drawString(Minecraft.getInstance().font, (Component)Component.literal((String)this.value), this.getX() + 8 + labelW + 6, this.getY() + (this.getHeight() - 8) / 2, -3355444, false);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }
}

