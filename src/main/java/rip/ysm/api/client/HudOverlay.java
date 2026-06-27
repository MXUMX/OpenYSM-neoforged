/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 */
package rip.ysm.api.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface HudOverlay {
    public void render(GuiGraphics var1, Font var2, float var3, int var4, int var5);
}

