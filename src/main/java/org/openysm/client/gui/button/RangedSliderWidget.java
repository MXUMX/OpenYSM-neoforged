/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.InputType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractSliderButton
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 */
package org.openysm.client.gui.button;

import java.text.DecimalFormat;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RangedSliderWidget
extends AbstractSliderButton {
    protected static final ResourceLocation SLIDER_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/slider.png");
    protected Component prefix;
    protected Component suffix;
    protected double minValue;
    protected double maxValue;
    protected double stepSize;
    protected boolean drawString;
    private boolean canChangeValue;
    private final DecimalFormat format;

    public RangedSliderWidget(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, (Component)Component.empty(), 0.0);
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = Math.abs(stepSize);
        this.value = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.drawString = drawString;
        if (stepSize == 0.0) {
            precision = Math.min(precision, 4);
            StringBuilder builder = new StringBuilder("0");
            if (precision > 0) {
                builder.append('.');
            }
            while (precision-- > 0) {
                builder.append('0');
            }
            this.format = new DecimalFormat(builder.toString());
        } else {
            this.format = Mth.equal((double)this.stepSize, (double)Math.floor(this.stepSize)) ? new DecimalFormat("0") : new DecimalFormat(Double.toString(this.stepSize).replaceAll("\\d", "0"));
        }
        this.updateMessage();
    }

    public RangedSliderWidget(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString) {
        this(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, 1.0, 0, drawString);
    }

    public double getValue() {
        return this.value * (this.maxValue - this.minValue) + this.minValue;
    }

    public void setValue(double value) {
        double oldValue = this.value;
        this.value = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        if (!Mth.equal((double)oldValue, (double)this.value)) {
            this.applyValue();
        }
        this.updateMessage();
    }

    public String getValueString() {
        return this.format.format(this.getValue());
    }

    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);
        this.setValueFromMouse(mouseX);
    }

    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.canChangeValue = false;
        } else {
            InputType inputType = Minecraft.getInstance().getLastInputType();
            if (inputType == InputType.MOUSE || inputType == InputType.KEYBOARD_TAB) {
                this.canChangeValue = true;
            }
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag;
        boolean bl = flag = keyCode == 263;
        if (flag || keyCode == 262) {
            float f;
            if (this.minValue > this.maxValue) {
                flag = !flag;
            }
            float f2 = f = flag ? -1.0f : 1.0f;
            if (this.stepSize <= 0.0) {
                this.setSliderValue(this.value + (double)(f / (float)(this.getWidth() - 8)));
            } else {
                this.setValue(this.getValue() + (double)f * this.stepSize);
            }
            return true;
        }
        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (double)(this.getX() + 4)) / (double)(this.getWidth() - 8));
    }

    private void setSliderValue(double value) {
        double oldValue = this.value;
        this.value = this.snapToNearest(value);
        if (!Mth.equal((double)oldValue, (double)this.value)) {
            this.applyValue();
        }
        this.updateMessage();
    }

    private double snapToNearest(double value) {
        if (this.stepSize <= 0.0) {
            return Mth.clamp((double)value, (double)0.0, (double)1.0);
        }
        value = Mth.lerp((double)Mth.clamp((double)value, (double)0.0, (double)1.0), (double)this.minValue, (double)this.maxValue);
        value = this.stepSize * (double)Math.round(value / this.stepSize);
        value = this.minValue > this.maxValue ? Mth.clamp((double)value, (double)this.maxValue, (double)this.minValue) : Mth.clamp((double)value, (double)this.minValue, (double)this.maxValue);
        return Mth.inverseLerp(value, this.minValue, this.maxValue);
    }

    protected void updateMessage() {
        if (this.drawString) {
            this.setMessage((Component)Component.literal((String)"").append(this.prefix).append(this.getValueString()).append(this.suffix));
        } else {
            this.setMessage((Component)Component.empty());
        }
    }

    protected void applyValue() {
    }

    protected int getTextureY() {
        int i = this.isFocused() && !this.canChangeValue ? 1 : 0;
        return i * 20;
    }

    protected int getHandleTextureY() {
        int i = !this.isHoveredOrFocused() && !this.canChangeValue ? 2 : 3;
        return i * 20;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        this.blitWithBorder(guiGraphics, SLIDER_LOCATION, this.getX(), this.getY(), 0, this.getTextureY(), this.getWidth(), this.getHeight(), 200, 20, 2, 3, 2, 2);
        int handleX = this.getX() + (int)(this.value * (double)(this.getWidth() - 8));
        this.blitWithBorder(guiGraphics, SLIDER_LOCATION, handleX, this.getY(), 0, this.getHandleTextureY(), 8, this.getHeight(), 200, 20, 2, 3, 2, 2);
        int color = this.active ? 0xFFFFFF : 0xA0A0A0;
        this.renderScrollingString(guiGraphics, mc.font, 2, color | Mth.ceil((float)(this.alpha * 255.0f)) << 24);
    }

    protected void blitWithBorder(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;
        guiGraphics.blit(texture, x, y, (float)(u), (float)(v), leftBorder, topBorder, textureWidth, textureHeight);
        guiGraphics.blit(texture, x + leftBorder + canvasWidth, y, (float)(u + leftBorder + fillerWidth), (float)(v), rightBorder, topBorder, textureWidth, textureHeight);
        guiGraphics.blit(texture, x, y + topBorder + canvasHeight, (float)(u), (float)(v + topBorder + fillerHeight), leftBorder, bottomBorder, textureWidth, textureHeight);
        guiGraphics.blit(texture, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, (float)(u + leftBorder + fillerWidth), (float)(v + topBorder + fillerHeight), rightBorder, bottomBorder, textureWidth, textureHeight);
        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); ++i) {
            int drawWidth = i == xPasses ? remainderWidth : fillerWidth;
            guiGraphics.blit(texture, x + leftBorder + i * fillerWidth, y, (float)(u + leftBorder), (float)(v), drawWidth, topBorder, textureWidth, textureHeight);
            guiGraphics.blit(texture, x + leftBorder + i * fillerWidth, y + topBorder + canvasHeight, (float)(u + leftBorder), (float)(v + topBorder + fillerHeight), drawWidth, bottomBorder, textureWidth, textureHeight);
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); ++j) {
                int drawHeight = j == yPasses ? remainderHeight : fillerHeight;
                guiGraphics.blit(texture, x + leftBorder + i * fillerWidth, y + topBorder + j * fillerHeight, (float)(u + leftBorder), (float)(v + topBorder), drawWidth, drawHeight, textureWidth, textureHeight);
            }
        }
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); ++j) {
            int drawHeight = j == yPasses ? remainderHeight : fillerHeight;
            guiGraphics.blit(texture, x, y + topBorder + j * fillerHeight, (float)(u), (float)(v + topBorder), leftBorder, drawHeight, textureWidth, textureHeight);
            guiGraphics.blit(texture, x + leftBorder + canvasWidth, y + topBorder + j * fillerHeight, (float)(u + leftBorder + fillerWidth), (float)(v + topBorder), rightBorder, drawHeight, textureWidth, textureHeight);
        }
    }
}
