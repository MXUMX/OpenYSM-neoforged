package net.minecraftforge.client.gui.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class ForgeSlider extends AbstractSliderButton {
    protected final double minValue;
    protected final double maxValue;
    protected final double stepSize;
    protected final Component prefix;
    protected final Component suffix;
    protected final boolean drawString;

    public ForgeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString) {
        this(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, 1.0D, 0, drawString);
    }

    public ForgeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, Component.empty(), normalize(minValue, maxValue, currentValue));
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = stepSize;
        this.drawString = drawString;
        this.updateMessage();
    }

    public double getValue() {
        double raw = this.minValue + (this.maxValue - this.minValue) * this.value;
        if (this.stepSize > 0.0D) {
            raw = this.stepSize * Math.round(raw / this.stepSize);
        }
        return Math.max(this.minValue, Math.min(this.maxValue, raw));
    }

    @Override
    protected void updateMessage() {
        if (this.drawString) {
            this.setMessage(this.prefix.copy().append(Component.literal(": " + this.getValue())).append(this.suffix));
        }
    }

    @Override
    public void applyValue() {
    }

    protected int getTextureY() {
        return 0;
    }

    protected int getHandleTextureY() {
        return this.isHoveredOrFocused() ? 60 : 40;
    }

    private static double normalize(double minValue, double maxValue, double value) {
        return maxValue == minValue ? 0.0D : (value - minValue) / (maxValue - minValue);
    }
}
