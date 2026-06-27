/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.Util
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 */
package org.openysm.client.gui;

import org.openysm.client.gui.button.FlatColorButton;
import org.openysm.client.upload.ModelUploadSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;

public class ModelUploadScreen
extends Screen
implements ModelUploadSession.Listener {
    private final Screen parentScreen;
    private long lastFlashTime = 0L;
    private String error = "";
    private float displayedProgress = 0.0f;
    private float prevProgressTarget = -1.0f;

    public ModelUploadScreen(Screen parent) {
        super((Component)Component.literal((String)"upload"));
        this.parentScreen = parent;
    }

    private static void drawBorder(GuiGraphics g, int x1, int y1, int x2, int y2, int w, int color) {
        g.fill(x1, y1, x2, y1 + w, color);
        g.fill(x1, y2 - w, x2, y2, color);
        g.fill(x1, y1, x1 + w, y2, color);
        g.fill(x2 - w, y1, x2, y2, color);
    }

    public void init() {
        this.clearWidgets();
        ModelUploadSession.addListener(this);
        this.addRenderableWidget(new FlatColorButton(this.width - 70, 10, 60, 18, (Component)Component.literal((String)"Back"), button -> Minecraft.getInstance().setScreen(this.parentScreen)));
    }

    public void tick() {
        ModelUploadSession.removeListener(this);
        ModelUploadSession.clearIfTerminal();
    }

    @Override
    public void onSessionUpdate(ModelUploadSession session) {
    }

    public void onFilesDrop(List<Path> paths) {
        byte[] data;
        if (paths.isEmpty()) {
            return;
        }
        this.error = "";
        this.lastFlashTime = Util.getMillis();
        Path path = paths.get(0);
        String fileName = path.getFileName().toString();
        if (!fileName.toLowerCase().endsWith(".ysm")) {
            this.error = "Invalid file type, expected .ysm";
            return;
        }
        ModelUploadSession existing = ModelUploadSession.getInstance();
        if (existing != null && !existing.isTerminal()) {
            this.error = "Upload already in progress";
            return;
        }
        try {
            data = Files.readAllBytes(path);
        }
        catch (IOException e) {
            this.error = "Failed to read file: " + e.getMessage();
            return;
        }
        String stem = fileName.substring(0, fileName.length() - 4);
        String modelId = stem.toLowerCase();
        if (modelId.isEmpty()) {
            this.error = "Cannot derive a valid model id from filename: " + stem;
            return;
        }
        String err = ModelUploadSession.start(modelId, data);
        if (err != null) {
            this.error = err;
        }
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int borderWidth;
        int borderColor;
        g.fill(0, 0, this.width, this.height, -1073741824);
        long sinceFlash = Util.getMillis() - this.lastFlashTime;
        if (sinceFlash < 900L) {
            float t = 1.0f - (float)sinceFlash / 900.0f;
            int alpha = Math.min(255, Math.max(0, (int)(255.0f * t)));
            borderColor = alpha << 24 | 0xFFC107;
            borderWidth = 4;
        } else {
            borderColor = 0x66808080;
            borderWidth = 2;
        }
        ModelUploadScreen.drawBorder(g, 0, 0, this.width, this.height, borderWidth, borderColor);
        ModelUploadSession session = ModelUploadSession.getInstance();
        if (session == null) {
            this.renderEmptyState(g);
        } else {
            this.renderSessionState(g, session);
        }
        if (!this.error.isEmpty()) {
            MutableComponent err = Component.literal((String)this.error).withStyle(ChatFormatting.RED);
            int w = this.font.width((FormattedText)err);
            g.drawString(this.font, (Component)err, (this.width - w) / 2, this.height - 60, -1);
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void renderEmptyState(GuiGraphics guiGraphics) {
        MutableComponent main = Component.literal((String)"Drag a YSM file into this window").withStyle(ChatFormatting.WHITE);
        MutableComponent sub = Component.literal((String)"Require standalone ysm model file.").withStyle(ChatFormatting.GRAY);
        int cx = this.width / 2;
        int cy = this.height / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)cx, (float)(cy - 14), 0.0f);
        guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
        int mw = this.font.width((FormattedText)main);
        guiGraphics.drawString(this.font, (Component)main, -mw / 2, 0, -1);
        guiGraphics.pose().popPose();
        int sw = this.font.width((FormattedText)sub);
        guiGraphics.drawString(this.font, (Component)sub, cx - sw / 2, cy + 22, -5592406);
        if (ModelUploadSession.hasServerLimits()) {
            MutableComponent limit = Component.literal((String)("Size limit: " + ModelUploadSession.formatBytes(ModelUploadSession.getLastMaxTotalBytes()))).withStyle(ChatFormatting.DARK_GRAY);
            int lw = this.font.width((FormattedText)limit);
            guiGraphics.drawString(this.font, (Component)limit, cx - lw / 2, cy + 36, -1);
        }
    }

    private void renderSessionState(GuiGraphics guiGraphics, ModelUploadSession session) {
        int cx = this.width / 2;
        int cy = this.height / 2;
        ChatFormatting color = switch (session.getState()) {
            case ModelUploadSession.State.COMPLETED -> ChatFormatting.GREEN;
            case ModelUploadSession.State.FAILED -> ChatFormatting.RED;
            default -> ChatFormatting.YELLOW;
        };
        MutableComponent title = Component.literal((String)session.getMessage()).withStyle(color);
        int tw = this.font.width((FormattedText)title);
        guiGraphics.drawString(this.font, (Component)title, cx - tw / 2, cy - 32, -1);
        MutableComponent sub = Component.literal((String)session.getModelId()).withStyle(ChatFormatting.GRAY);
        int sw = this.font.width((FormattedText)sub);
        guiGraphics.drawString(this.font, (Component)sub, cx - sw / 2, cy - 16, -1);
        int barW = 320;
        int barH = 14;
        int barX = cx - barW / 2;
        int barY = cy + 4;
        float target = session.getProgress();
        if (target < this.prevProgressTarget) {
            this.displayedProgress = target;
        }
        this.prevProgressTarget = target;
        this.displayedProgress += (target - this.displayedProgress) * 0.18f;
        if (Math.abs(target - this.displayedProgress) < 0.001f) {
            this.displayedProgress = target;
        }
        int fillW = (int)((float)barW * this.displayedProgress);
        int fillColor = session.getState() == ModelUploadSession.State.FAILED ? -3001806 : (session.getState() == ModelUploadSession.State.COMPLETED ? -11751600 : -16121);
        guiGraphics.fill(barX, barY, barX + barW, barY + barH, -14013910);
        if (fillW > 0) {
            guiGraphics.fill(barX, barY, barX + fillW, barY + barH, fillColor);
        }
        if (session.getState() == ModelUploadSession.State.UPLOADING && fillW > 4) {
            long now = Util.getMillis();
            int period = 1400;
            int travel = fillW + 40;
            int shimmerX = (int)((float)(now % (long)period) / (float)period * (float)travel) - 20;
            int shimmerW = 24;
            int left = barX + Math.max(0, shimmerX);
            int right = barX + Math.min(fillW, shimmerX + shimmerW);
            if (right > left) {
                guiGraphics.fill(left, barY + 1, right, barY + barH - 1, 0x55FFFFFF);
            }
        }
        guiGraphics.fill(barX, barY, barX + barW, barY + 1, -1);
        guiGraphics.fill(barX, barY + barH - 1, barX + barW, barY + barH, -1);
        guiGraphics.fill(barX, barY, barX + 1, barY + barH, -1);
        guiGraphics.fill(barX + barW - 1, barY, barX + barW, barY + barH, -1);
        String stat = ModelUploadSession.formatBytes(session.getSentBytes()) + " / " + ModelUploadSession.formatBytes(session.getTotalBytes());
        int statW = this.font.width(stat);
        guiGraphics.drawString(this.font, stat, cx - statW / 2, barY + barH + 6, -5592406);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void onClose() {
        Minecraft.getInstance().setScreen(this.parentScreen);
    }
}
