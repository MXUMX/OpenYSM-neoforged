/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 */
package rip.ysm.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import rip.ysm.gui.ModernModelInfoScreen;
import rip.ysm.gui.OptionRow;

public final class LinkRow
extends OptionRow<Object> {
    private final ModernModelInfoScreen owner;
    private final String label;
    private final String url;

    public LinkRow(ModernModelInfoScreen owner, String label, String url) {
        super(0, 0, 0, 20, null);
        this.owner = owner;
        this.label = label;
        this.url = url;
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean hover = this.isHoveredOrFocused();
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), hover ? -1877534953 : -1879048192);
        Font font = Minecraft.getInstance().font;
        String i18nKey = "gui.openysm.url." + this.label;
        MutableComponent nameComponent = I18n.exists((String)i18nKey) ? Component.translatable((String)i18nKey) : Component.literal((String)this.label);
        g.drawString(font, (Component)nameComponent.copy().withStyle(ChatFormatting.AQUA, ChatFormatting.UNDERLINE), this.getX() + 8, this.getY() + (this.getHeight() - 8) / 2, -1, false);
        MutableComponent urlComp = Component.literal((String)this.url).withStyle(ChatFormatting.GRAY);
        int urlW = font.width((FormattedText)urlComp);
        int urlX = Math.max(this.getX() + 8 + font.width((FormattedText)nameComponent) + 12, this.getX() + this.getWidth() - urlW - 8);
        g.drawString(font, (Component)urlComp, urlX, this.getY() + (this.getHeight() - 8) / 2, -1, false);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    public void onClick(double mouseX, double mouseY) {
        this.owner.openUrlWithConfirm(this.url);
    }
}

