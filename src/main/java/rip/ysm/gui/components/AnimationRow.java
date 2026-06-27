/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 */
package rip.ysm.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import rip.ysm.gui.ModernPlayerTextureScreen;
import rip.ysm.gui.OptionRow;

public final class AnimationRow
extends OptionRow<Object> {
    public final String animKey;
    private final ModernPlayerTextureScreen owner;

    public AnimationRow(int x, int y, int width, int height, String animKey, ModernPlayerTextureScreen owner) {
        super(x, y, width, height, null);
        this.animKey = animKey;
        this.owner = owner;
        String i18nKey = "gui.openysm.texture.button." + animKey.replace(':', '.');
        MutableComponent label = I18n.exists((String)i18nKey) ? Component.translatable((String)i18nKey) : Component.literal((String)animKey);
        this.setMessage((Component)label);
    }

    public boolean matches(String lowerSearch) {
        return this.animKey.toLowerCase().contains(lowerSearch) || this.getMessage().getString().toLowerCase().contains(lowerSearch);
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean selected = this.animKey.equals(this.owner.currentAnimation());
        int bg = selected ? -1875692749 : (this.isHoveredOrFocused() ? -1877534953 : -1879048192);
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bg);
        if (selected) {
            g.fill(this.getX(), this.getY(), this.getX() + 2, this.getY() + this.getHeight(), -1);
        }
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        g.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 8, textY, -1, false);
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    public void onClick(double mouseX, double mouseY) {
        this.owner.selectAnimation(this.animKey);
    }
}

