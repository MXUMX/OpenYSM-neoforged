/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 */
package rip.ysm.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import rip.ysm.gui.OptionRow;

public final class HeaderRow
extends OptionRow<Object> {
    private final String text;

    public HeaderRow(String text) {
        super(0, 0, 0, 22, null);
        this.text = text;
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), -1879048192);
        g.drawString(Minecraft.getInstance().font, (Component)Component.literal((String)this.text).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), this.getX() + 8, this.getY() + (this.getHeight() - 8) / 2, -1, false);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }
}

