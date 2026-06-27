/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.Nullable
 */
package rip.ysm.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import rip.ysm.gui.Option;
import rip.ysm.gui.OptionGroup;
import rip.ysm.gui.OptionRow;
import rip.ysm.gui.components.buttons.FooterButton;
import rip.ysm.gui.components.buttons.TabButton;

public abstract class OptionScreen
extends Screen {
    @Nullable
    protected final Screen parentScreen;
    protected final List<OptionGroup> groups = new ArrayList<OptionGroup>();
    protected final List<TabButton> tabButtons = new ArrayList<TabButton>();
    protected final List<OptionRow<?>> activeRows = new ArrayList();
    protected OptionGroup activeGroup;
    @Nullable
    protected OptionRow<?> hoveredRow;
    protected int panelLeft;
    protected int panelTop;
    protected int panelRight;
    protected int panelBottom;
    protected int tabAreaLeft;
    protected int tabAreaTop;
    protected int tabAreaRight;
    protected int tabAreaBottom;
    protected int rowAreaLeft;
    protected int rowAreaTop;
    protected int rowAreaRight;
    protected int rowAreaBottom;
    protected int rowScrollOffset;
    protected float rowScrollDisplay;
    protected int maxRowScroll;
    protected int rowContentHeight;
    protected int tabScrollOffset;
    protected float tabScrollDisplay;
    protected int maxTabScroll;
    private int tabContentHeight;
    private int tabContentWidth;
    protected boolean compactTabs;
    private long lastFrameNanos;
    private boolean draggingRowScrollbar;
    private boolean draggingTabScrollbar;
    protected FooterButton applyBtn;
    protected FooterButton undoBtn;
    protected FooterButton saveBtn;
    protected FooterButton cancelBtn;
    private static final Map<Class<? extends OptionScreen>, String> lastSelectedGroup = new HashMap<Class<? extends OptionScreen>, String>();

    public OptionScreen(Component title, @Nullable Screen parent) {
        super(title);
        this.parentScreen = parent;
    }

    protected abstract void registerGroups();

    protected void init() {
        this.groups.clear();
        this.tabButtons.clear();
        this.activeRows.clear();
        this.registerGroups();
        int totalWidth = this.computePanelWidth();
        int totalHeight = this.computePanelHeight();
        this.panelLeft = (this.width - totalWidth) / 2;
        this.panelTop = (this.height - totalHeight) / 2;
        this.panelRight = this.panelLeft + totalWidth;
        this.panelBottom = this.panelTop + totalHeight;
        this.compactTabs = this.shouldUseCompactTabs();
        boolean tabs = this.showTabs();
        if (!tabs) {
            this.tabAreaLeft = this.panelLeft;
            this.tabAreaRight = this.panelLeft;
            this.tabAreaBottom = this.tabAreaTop = this.panelTop + 6 + 18;
            this.rowAreaLeft = this.panelLeft;
            this.rowAreaTop = this.panelTop + 6 + 18;
            this.rowAreaRight = this.computeRowAreaRight();
            this.rowAreaBottom = this.panelBottom - 60;
        } else if (this.compactTabs) {
            this.tabAreaLeft = this.panelLeft;
            this.tabAreaRight = this.panelRight;
            this.tabAreaTop = this.panelTop + 6 + 18;
            this.tabAreaBottom = this.tabAreaTop + this.tabHeight();
            this.rowAreaLeft = this.panelLeft;
            this.rowAreaTop = this.tabAreaBottom + 4;
            this.rowAreaRight = this.computeRowAreaRight();
            this.rowAreaBottom = this.panelBottom - 60;
        } else {
            this.tabAreaLeft = this.panelLeft;
            this.tabAreaTop = this.panelTop + 6 + 18;
            this.tabAreaRight = this.panelLeft + this.tabWidth();
            this.tabAreaBottom = this.panelBottom - 60;
            this.rowAreaLeft = this.panelLeft + this.tabWidth() + 6;
            this.rowAreaTop = this.panelTop + 6 + 18;
            this.rowAreaRight = this.computeRowAreaRight();
            this.rowAreaBottom = this.panelBottom - 60;
        }
        this.tabContentHeight = 0;
        this.tabContentWidth = 0;
        if (tabs && this.compactTabs) {
            int tabX = this.tabAreaLeft;
            for (OptionGroup g : this.groups) {
                int textW = this.font.width((FormattedText)g.getTitle());
                int w = Mth.clamp((int)(textW + 16), (int)60, (int)140);
                TabButton tb = new TabButton(tabX, this.tabAreaTop, w, this.tabHeight(), g, this::selectGroup);
                tb.setHorizontal(true);
                this.tabButtons.add(tb);
                tabX += w + 2;
            }
            this.tabContentWidth = tabX - this.tabAreaLeft;
            this.maxTabScroll = Math.max(0, this.tabContentWidth - (this.tabAreaRight - this.tabAreaLeft));
        } else if (tabs) {
            int tabY = this.tabAreaTop;
            for (OptionGroup g : this.groups) {
                TabButton tb = new TabButton(this.tabAreaLeft, tabY, this.tabWidth(), this.tabHeight(), g, this::selectGroup);
                this.tabButtons.add(tb);
                tabY += this.tabHeight();
            }
            this.tabContentHeight = tabY - this.tabAreaTop;
            this.maxTabScroll = Math.max(0, this.tabContentHeight - (this.tabAreaBottom - this.tabAreaTop));
        }
        this.tabScrollOffset = 0;
        this.tabScrollDisplay = 0.0f;
        int footerY = this.panelBottom - 56;
        int btnW = this.footerButtonWidth();
        int btnH = 20;
        int gap = 4;
        this.cancelBtn = new FooterButton(this.panelRight - btnW, footerY, btnW, btnH, (Component)Component.translatable((String)"gui.openysm.config.cancel"), this::onCancel);
        this.saveBtn = new FooterButton(this.cancelBtn.getX() - btnW - gap, footerY, btnW, btnH, (Component)Component.translatable((String)"gui.openysm.config.save"), this::onSave);
        this.applyBtn = new FooterButton(this.saveBtn.getX() - btnW - gap, footerY, btnW, btnH, (Component)Component.translatable((String)"gui.openysm.config.apply"), this::onApply);
        this.undoBtn = new FooterButton(this.panelLeft, footerY, btnW, btnH, (Component)Component.translatable((String)"gui.openysm.config.undo"), this::onUndo);
        this.addRenderableWidget(this.undoBtn);
        this.addRenderableWidget(this.applyBtn);
        this.addRenderableWidget(this.saveBtn);
        this.addRenderableWidget(this.cancelBtn);
        if (!this.groups.isEmpty()) {
            OptionGroup toSelect = this.groups.get(0);
            String stored = lastSelectedGroup.get(((Object)((Object)this)).getClass());
            if (stored != null) {
                for (OptionGroup candidate : this.groups) {
                    if (!stored.equals(candidate.getTranslationKey())) continue;
                    toSelect = candidate;
                    break;
                }
            }
            this.selectGroup(toSelect);
        }
    }

    protected int computePanelWidth() {
        return Math.min(this.width - 40, 540);
    }

    protected int computePanelHeight() {
        return Math.min(this.height - 40, 320);
    }

    protected int computeRowAreaRight() {
        return this.panelRight;
    }

    protected int tabWidth() {
        return 110;
    }

    protected int tabHeight() {
        return 22;
    }

    protected int footerButtonWidth() {
        return 70;
    }

    protected boolean shouldUseCompactTabs() {
        return this.width < 500;
    }

    protected boolean showTabs() {
        return true;
    }

    protected void selectGroup(OptionGroup group) {
        if (this.activeGroup == group && !this.activeRows.isEmpty()) {
            return;
        }
        for (OptionRow<?> r : this.activeRows) {
            r.closeOverlay();
        }
        this.activeRows.clear();
        this.activeGroup = group;
        lastSelectedGroup.put(this.getClass(), group.getTranslationKey());
        int selectedIndex = -1;
        for (int i = 0; i < this.tabButtons.size(); ++i) {
            TabButton tb = this.tabButtons.get(i);
            tb.setSelected(tb.getGroup() == group);
            if (tb.getGroup() != group) continue;
            selectedIndex = i;
        }
        if (selectedIndex >= 0 && this.maxTabScroll > 0) {
            TabButton sel = this.tabButtons.get(selectedIndex);
            if (this.compactTabs) {
                int btnLeft = sel.getX() - this.tabAreaLeft;
                int btnRight = btnLeft + sel.getWidth();
                int viewW = this.tabAreaRight - this.tabAreaLeft;
                if (btnLeft < this.tabScrollOffset) {
                    this.tabScrollOffset = btnLeft;
                } else if (btnRight > this.tabScrollOffset + viewW) {
                    this.tabScrollOffset = btnRight - viewW;
                }
            } else {
                int btnTop = selectedIndex * this.tabHeight();
                int btnBot = btnTop + this.tabHeight();
                int viewH = this.tabAreaBottom - this.tabAreaTop;
                if (btnTop < this.tabScrollOffset) {
                    this.tabScrollOffset = btnTop;
                } else if (btnBot > this.tabScrollOffset + viewH) {
                    this.tabScrollOffset = btnBot - viewH;
                }
            }
            this.tabScrollOffset = Mth.clamp((int)this.tabScrollOffset, (int)0, (int)this.maxTabScroll);
        }
        int rowY = this.rowAreaTop;
        int rowW = this.rowAreaRight - this.rowAreaLeft;
        for (OptionRow<?> template : group.getRows()) {
            template.setX(this.rowAreaLeft);
            template.setY(rowY);
            template.setWidth(rowW);
            this.activeRows.add(template);
            rowY += template.getHeight() + 2;
        }
        this.rowContentHeight = rowY - this.rowAreaTop;
        this.maxRowScroll = Math.max(0, this.rowContentHeight - (this.rowAreaBottom - this.rowAreaTop));
        this.rowScrollOffset = Math.min(this.rowScrollOffset, this.maxRowScroll);
        this.rowScrollDisplay = Math.min(this.rowScrollDisplay, (float)this.maxRowScroll);
    }

    protected boolean anyDirty() {
        for (OptionGroup g : this.groups) {
            if (!g.isDirty()) continue;
            return true;
        }
        return false;
    }

    protected void onApply() {
        for (OptionGroup g : this.groups) {
            g.apply();
        }
    }

    protected void onSave() {
        this.onApply();
        Minecraft.getInstance().setScreen(this.parentScreen);
    }

    protected void onCancel() {
        for (OptionGroup g : this.groups) {
            g.undo();
        }
        Minecraft.getInstance().setScreen(this.parentScreen);
    }

    protected void onUndo() {
        if (this.activeGroup != null) {
            this.activeGroup.undo();
        }
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean dirty;
        this.renderTransparentBackground(g);
        g.fill(this.panelLeft, this.panelTop, this.panelRight, this.panelTop + 18, -1879048192);
        long now = System.nanoTime();
        if (this.lastFrameNanos == 0L) {
            this.lastFrameNanos = now;
        }
        float dt = Math.min(0.1f, (float)(now - this.lastFrameNanos) / 1.0E9f);
        this.lastFrameNanos = now;
        float lerp = 1.0f - (float)Math.exp(-dt * 18.0f);
        this.rowScrollDisplay += ((float)this.rowScrollOffset - this.rowScrollDisplay) * lerp;
        if (Math.abs((float)this.rowScrollOffset - this.rowScrollDisplay) < 0.5f) {
            this.rowScrollDisplay = this.rowScrollOffset;
        }
        this.tabScrollDisplay += ((float)this.tabScrollOffset - this.tabScrollDisplay) * lerp;
        if (Math.abs((float)this.tabScrollOffset - this.tabScrollDisplay) < 0.5f) {
            this.tabScrollDisplay = this.tabScrollOffset;
        }
        int descY = this.panelBottom - 32;
        boolean inRowArea = mouseX >= this.rowAreaLeft && mouseX < this.rowAreaRight && mouseY >= this.rowAreaTop && mouseY < this.rowAreaBottom;
        int adjMouseY = inRowArea ? mouseY + Math.round(this.rowScrollDisplay) : Integer.MIN_VALUE;
        this.hoveredRow = null;
        if (inRowArea) {
            for (OptionRow<?> row : this.activeRows) {
                if (mouseX < row.getX() || mouseX >= row.getX() + row.getWidth() || adjMouseY < row.getY() || adjMouseY >= row.getY() + row.getHeight()) continue;
                this.hoveredRow = row;
                break;
            }
        }
        this.applyBtn.active = dirty = this.anyDirty();
        this.undoBtn.active = this.activeGroup != null && this.activeGroup.isDirty();
        super.render(g, mouseX, mouseY, partialTick);
        g.drawString(this.font, this.title, this.panelLeft + 6, this.panelTop + 5, -1, false);
        if (!this.tabButtons.isEmpty()) {
            boolean inTabArea = mouseX >= this.tabAreaLeft && mouseX < this.tabAreaRight && mouseY >= this.tabAreaTop && mouseY < this.tabAreaBottom;
            int adjTabMouseX = mouseX;
            int adjTabMouseY = mouseY;
            if (this.compactTabs) {
                adjTabMouseX = inTabArea ? mouseX + Math.round(this.tabScrollDisplay) : Integer.MIN_VALUE;
            } else {
                adjTabMouseY = inTabArea ? mouseY + Math.round(this.tabScrollDisplay) : Integer.MIN_VALUE;
            }
            g.enableScissor(this.tabAreaLeft, this.tabAreaTop, this.tabAreaRight, this.tabAreaBottom);
            g.pose().pushPose();
            if (this.compactTabs) {
                g.pose().translate(-this.tabScrollDisplay, 0.0f, 0.0f);
            } else {
                g.pose().translate(0.0f, -this.tabScrollDisplay, 0.0f);
            }
            for (TabButton tb : this.tabButtons) {
                tb.render(g, adjTabMouseX, adjTabMouseY, partialTick);
            }
            g.pose().popPose();
            g.disableScissor();
            if (this.maxTabScroll > 0) {
                this.renderTabScrollbar(g);
            }
        }
        g.enableScissor(this.rowAreaLeft, this.rowAreaTop, this.rowAreaRight, this.rowAreaBottom);
        g.pose().pushPose();
        g.pose().translate(0.0f, -this.rowScrollDisplay, 0.0f);
        for (OptionRow<?> row : this.activeRows) {
            row.render(g, mouseX, adjMouseY, partialTick);
        }
        g.pose().popPose();
        g.disableScissor();
        if (this.maxRowScroll > 0) {
            this.renderRowScrollbar(g);
        }
        this.renderDescription(g, descY);
        this.renderExtras(g, mouseX, mouseY, partialTick);
        for (OptionRow<?> row : this.activeRows) {
            if (!row.isOverlayOpen()) continue;
            row.renderOverlay(g, mouseX, mouseY, partialTick, this.rowScrollDisplay);
        }
    }

    protected void renderExtras(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    protected void collectBlurRegions(List<int[]> out) {
        out.add(new int[]{this.panelLeft, this.panelTop, this.panelRight - this.panelLeft, 18});
        int tabScroll = Math.round(this.tabScrollDisplay);
        for (TabButton tabButton : this.tabButtons) {
            int n;
            if (this.compactTabs) {
                n = tabButton.getX() - tabScroll;
                int xRight = n + tabButton.getWidth();
                int left = Math.max(n, this.tabAreaLeft);
                int right = Math.min(xRight, this.tabAreaRight);
                if (right <= left) continue;
                out.add(new int[]{left, tabButton.getY(), right - left, tabButton.getHeight()});
                continue;
            }
            n = tabButton.getY() - tabScroll;
            int yBot = n + tabButton.getHeight();
            int top = Math.max(n, this.tabAreaTop);
            int bot = Math.min(yBot, this.tabAreaBottom);
            if (bot <= top) continue;
            out.add(new int[]{tabButton.getX(), top, tabButton.getWidth(), bot - top});
        }
        int rowScroll = Math.round(this.rowScrollDisplay);
        for (OptionRow<?> optionRow : this.activeRows) {
            int y = optionRow.getY() - rowScroll;
            int yBot = y + optionRow.getHeight();
            int top = Math.max(y, this.rowAreaTop);
            int bot = Math.min(yBot, this.rowAreaBottom);
            if (bot <= top) continue;
            out.add(new int[]{optionRow.getX(), top, optionRow.getWidth(), bot - top});
        }
        this.addFooterRect(out, this.applyBtn);
        this.addFooterRect(out, this.undoBtn);
        this.addFooterRect(out, this.saveBtn);
        this.addFooterRect(out, this.cancelBtn);
        if (this.hoveredRow != null) {
            int n = this.panelBottom - 32;
            out.add(new int[]{this.panelLeft, n, this.panelRight - this.panelLeft, 28});
        }
    }

    private void addFooterRect(List<int[]> out, FooterButton btn) {
        if (btn == null || !btn.visible) {
            return;
        }
        out.add(new int[]{btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight()});
    }

    private void renderRowScrollbar(GuiGraphics g) {
        int trackX = this.rowAreaRight - 1;
        int trackTop = this.rowAreaTop + 1;
        int trackBot = this.rowAreaBottom - 1;
        int trackH = trackBot - trackTop;
        int areaH = this.rowAreaBottom - this.rowAreaTop;
        int thumbH = Math.max(16, trackH * areaH / Math.max(1, this.rowContentHeight));
        int thumbY = trackTop + (int)((float)(trackH - thumbH) * this.rowScrollDisplay / (float)Math.max(1, this.maxRowScroll));
        g.fill(trackX, thumbY, trackX + 1, thumbY + thumbH, this.draggingRowScrollbar ? -1 : -5592406);
    }

    private void renderTabScrollbar(GuiGraphics g) {
        if (this.compactTabs) {
            int trackY = this.tabAreaBottom - 1;
            int trackLeft = this.tabAreaLeft + 1;
            int trackRight = this.tabAreaRight - 1;
            int trackW = trackRight - trackLeft;
            int areaW = this.tabAreaRight - this.tabAreaLeft;
            int thumbW = Math.max(16, trackW * areaW / Math.max(1, this.tabContentWidth));
            int thumbX = trackLeft + (int)((float)(trackW - thumbW) * this.tabScrollDisplay / (float)Math.max(1, this.maxTabScroll));
            g.fill(thumbX, trackY, thumbX + thumbW, trackY + 1, this.draggingTabScrollbar ? -1 : -5592406);
            return;
        }
        int trackX = this.tabAreaRight - 1;
        int trackTop = this.tabAreaTop + 1;
        int trackBot = this.tabAreaBottom - 1;
        int trackH = trackBot - trackTop;
        int areaH = this.tabAreaBottom - this.tabAreaTop;
        int thumbH = Math.max(16, trackH * areaH / Math.max(1, this.tabContentHeight));
        int thumbY = trackTop + (int)((float)(trackH - thumbH) * this.tabScrollDisplay / (float)Math.max(1, this.maxTabScroll));
        g.fill(trackX, thumbY, trackX + 1, thumbY + thumbH, this.draggingTabScrollbar ? -1 : -5592406);
    }

    protected void renderDescription(GuiGraphics g, int descY) {
        if (this.hoveredRow == null || this.hoveredRow.getOption() == null) {
            return;
        }
        g.fill(this.panelLeft, descY, this.panelRight, descY + 28, Integer.MIN_VALUE);
        Option<?> opt = this.hoveredRow.getOption();
        Component title = opt.getLabel();
        g.drawString(this.font, title, this.panelLeft + 6, descY + 4, -1, false);
        Component desc = opt.getDescription();
        int maxWidth = this.panelRight - this.panelLeft - 12;
        List lines = this.font.split((FormattedText)desc, maxWidth);
        int lineY = descY + 16;
        int max = Math.min(lines.size(), 1);
        for (int i = 0; i < max; ++i) {
            g.drawString(this.font, (FormattedCharSequence)lines.get(i), this.panelLeft + 6, lineY, -3355444, false);
            lineY += 10;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionRow<?> row : this.activeRows) {
            if (!row.isOverlayOpen() || !row.overlayMouseClicked(mouseX, mouseY, button, this.rowScrollDisplay)) continue;
            return true;
        }
        for (OptionRow<?> row : this.activeRows) {
            if (!row.isOverlayOpen()) continue;
            row.closeOverlay();
        }
        if (this.maxRowScroll > 0 && this.isOnRowScrollbar(mouseX, mouseY)) {
            this.draggingRowScrollbar = true;
            this.updateRowScrollFromMouse(mouseY);
            return true;
        }
        if (this.maxTabScroll > 0 && this.isOnTabScrollbar(mouseX, mouseY)) {
            this.draggingTabScrollbar = true;
            this.updateTabScrollFromMouse(mouseX, mouseY);
            return true;
        }
        if (mouseX >= (double)this.tabAreaLeft && mouseX < (double)this.tabAreaRight && mouseY >= (double)this.tabAreaTop && mouseY < (double)this.tabAreaBottom) {
            double adjX = this.compactTabs ? mouseX + (double)this.tabScrollDisplay : mouseX;
            double adjY = this.compactTabs ? mouseY : mouseY + (double)this.tabScrollDisplay;
            for (TabButton tb : this.tabButtons) {
                if (!tb.mouseClicked(adjX, adjY, button)) continue;
                return true;
            }
            return true;
        }
        if (mouseX >= (double)this.rowAreaLeft && mouseX < (double)this.rowAreaRight && mouseY >= (double)this.rowAreaTop && mouseY < (double)this.rowAreaBottom) {
            double adjY = mouseY + (double)this.rowScrollDisplay;
            for (OptionRow<?> row : this.activeRows) {
                if (!row.mouseClicked(mouseX, adjY, button)) continue;
                this.setFocused((GuiEventListener)row);
                if (button == 0) {
                    this.setDragging(true);
                }
                return true;
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (this.draggingRowScrollbar) {
            this.updateRowScrollFromMouse(mouseY);
            return true;
        }
        if (this.draggingTabScrollbar) {
            this.updateTabScrollFromMouse(mouseX, mouseY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.draggingRowScrollbar) {
            this.draggingRowScrollbar = false;
            return true;
        }
        if (this.draggingTabScrollbar) {
            this.draggingTabScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        for (OptionRow<?> row : this.activeRows) {
            if (!row.isOverlayOpen() || !row.overlayMouseScrolled(mouseX, mouseY, delta, this.rowScrollDisplay)) continue;
            return true;
        }
        if (mouseX >= (double)this.tabAreaLeft && mouseX < (double)this.tabAreaRight && mouseY >= (double)this.tabAreaTop && mouseY < (double)this.tabAreaBottom) {
            this.tabScrollOffset = Mth.clamp((int)((int)((double)this.tabScrollOffset - delta * 20.0)), (int)0, (int)this.maxTabScroll);
            return true;
        }
        if (mouseX >= (double)this.rowAreaLeft && mouseX < (double)this.rowAreaRight && mouseY >= (double)this.rowAreaTop && mouseY < (double)this.rowAreaBottom) {
            this.rowScrollOffset = Mth.clamp((int)((int)((double)this.rowScrollOffset - delta * 20.0)), (int)0, (int)this.maxRowScroll);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, 0.0, delta);
    }

    private boolean isOnRowScrollbar(double mouseX, double mouseY) {
        int trackX = this.rowAreaRight - 4;
        return mouseX >= (double)trackX && mouseX < (double)(trackX + 3) && mouseY >= (double)this.rowAreaTop && mouseY < (double)this.rowAreaBottom;
    }

    private boolean isOnTabScrollbar(double mouseX, double mouseY) {
        if (this.compactTabs) {
            int trackY = this.tabAreaBottom - 4;
            return mouseY >= (double)trackY && mouseY < (double)(trackY + 3) && mouseX >= (double)this.tabAreaLeft && mouseX < (double)this.tabAreaRight;
        }
        int trackX = this.tabAreaRight - 4;
        return mouseX >= (double)trackX && mouseX < (double)(trackX + 3) && mouseY >= (double)this.tabAreaTop && mouseY < (double)this.tabAreaBottom;
    }

    private void updateRowScrollFromMouse(double mouseY) {
        int trackTop = this.rowAreaTop + 1;
        int trackBot = this.rowAreaBottom - 1;
        double t = Mth.clamp((double)((mouseY - (double)trackTop) / (double)Math.max(1, trackBot - trackTop)), (double)0.0, (double)1.0);
        this.rowScrollOffset = (int)(t * (double)this.maxRowScroll);
    }

    private void updateTabScrollFromMouse(double mouseX, double mouseY) {
        if (this.compactTabs) {
            int trackLeft = this.tabAreaLeft + 1;
            int trackRight = this.tabAreaRight - 1;
            double t = Mth.clamp((double)((mouseX - (double)trackLeft) / (double)Math.max(1, trackRight - trackLeft)), (double)0.0, (double)1.0);
            this.tabScrollOffset = (int)(t * (double)this.maxTabScroll);
            return;
        }
        int trackTop = this.tabAreaTop + 1;
        int trackBot = this.tabAreaBottom - 1;
        double t = Mth.clamp((double)((mouseY - (double)trackTop) / (double)Math.max(1, trackBot - trackTop)), (double)0.0, (double)1.0);
        this.tabScrollOffset = (int)(t * (double)this.maxTabScroll);
    }

    public boolean isPauseScreen() {
        return true;
    }

    public void onClose() {
        this.onCancel();
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}
