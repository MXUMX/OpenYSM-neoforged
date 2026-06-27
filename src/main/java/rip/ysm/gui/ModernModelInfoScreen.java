/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Util
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.ConfirmLinkScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.apache.commons.lang3.StringUtils
 */
package rip.ysm.gui;

import org.openysm.client.gui.ModelMetadataPresenter;
import org.openysm.client.gui.PlayerModelScreen;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.texture.OuterFileTexture;
import org.openysm.client.upload.IResourceLocatable;
import org.openysm.client.upload.UploadManager;
import org.openysm.model.format.ServerModelInfo;
import org.openysm.resource.models.AuthorInfo;
import org.openysm.resource.models.Metadata;
import org.openysm.util.data.OrderedStringMap;
import org.openysm.util.data.StringPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import rip.ysm.gui.OptionRow;
import rip.ysm.gui.OptionScreen;
import rip.ysm.gui.components.AuthorRow;
import rip.ysm.gui.components.HeaderRow;
import rip.ysm.gui.components.LabelValueRow;
import rip.ysm.gui.components.LinkRow;
import rip.ysm.gui.components.TipsRow;
import rip.ysm.gui.components.buttons.FooterButton;
import rip.ysm.gui.components.groups.InfoGroup;

public class ModernModelInfoScreen
extends OptionScreen {
    public final ModelAssembly renderContext;
    private final ServerModelInfo modelData;
    private final List<IResourceLocatable> avatarLocatables = new ArrayList<IResourceLocatable>();

    public ModernModelInfoScreen(PlayerModelScreen parent, ModelAssembly modelAssembly) {
        super((Component)Component.translatable((String)"gui.openysm.model_info.title"), parent);
        this.renderContext = modelAssembly;
        this.modelData = modelAssembly.getModelData();
        this.resolveAvatarTextures();
    }

    private void resolveAvatarTextures() {
        this.avatarLocatables.clear();
        List<AuthorInfo> authors = this.modelData.getExtraInfo().getAuthors();
        Map<String, OuterFileTexture> avatars = this.renderContext.getTextureRegistry().getAuthorAvatars();
        for (AuthorInfo author : authors) {
            OuterFileTexture avatar = avatars.get(author.getName());
            this.avatarLocatables.add(avatar != null ? UploadManager.getOrCreateLocatable(avatar, true) : null);
        }
    }

    @Override
    protected int computePanelWidth() {
        return Math.min(this.width - 40, 640);
    }

    @Override
    protected int computePanelHeight() {
        return Math.min(this.height - 40, 380);
    }

    @Override
    protected boolean showTabs() {
        return false;
    }

    @Override
    protected void registerGroups() {
        OrderedStringMap<String, String> links;
        String tips;
        StringPair license;
        Metadata meta = this.modelData.getExtraInfo();
        if (meta == null) {
            return;
        }
        InfoGroup page = new InfoGroup("page");
        String name = ModelMetadataPresenter.getLocalizedModelString(this.renderContext, "metadata.name", meta.getName());
        if (StringUtils.isNotBlank((CharSequence)name)) {
            page.add(new HeaderRow(name));
        }
        if ((license = meta.getLicense()) != null && StringUtils.isNotBlank((CharSequence)license.getFirst())) {
            String licenseValue = StringUtils.isNotBlank((CharSequence)license.getSecond()) ? license.getFirst() + "  \u2014  " + license.getSecond() : license.getFirst();
            page.add(new LabelValueRow("gui.openysm.model_info.license", licenseValue));
        }
        if (StringUtils.isNotBlank((CharSequence)(tips = ModelMetadataPresenter.getLocalizedModelString(this.renderContext, "metadata.tips", meta.getTips())))) {
            page.add(new TipsRow(tips));
        }
        if ((links = meta.getLink()) != null && !links.isEmpty()) {
            for (int i = 0; i < links.size(); ++i) {
                page.add(new LinkRow(this, links.getKeyAt(i), links.getValueAt(i)));
            }
        }
        List<AuthorInfo> authors = meta.getAuthors();
        for (int i = 0; i < authors.size(); ++i) {
            page.add(new AuthorRow(this, authors.get(i), i, this.avatarLocatables.get(i)));
        }
        if (!page.getRows().isEmpty()) {
            this.groups.add(page);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(this.applyBtn);
        this.addRenderableWidget(this.undoBtn);
        this.addRenderableWidget(this.cancelBtn);
        this.applyBtn.visible = false;
        this.undoBtn.visible = false;
        this.cancelBtn.visible = false;
        this.applyBtn.active = false;
        this.undoBtn.active = false;
        this.saveBtn.setMessage((Component)Component.translatable((String)"gui.openysm.config.done"));
        this.saveBtn.setX(this.panelRight - this.saveBtn.getWidth());
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parentScreen);
        }
    }

    @Override
    protected void collectBlurRegions(List<int[]> out) {
        out.add(new int[]{this.panelLeft, this.panelTop, this.panelRight - this.panelLeft, 18});
        int rowScroll = Math.round(this.rowScrollDisplay);
        for (OptionRow row : this.activeRows) {
            int y = row.getY() - rowScroll;
            int yBot = y + row.getHeight();
            if (yBot <= this.rowAreaTop || y >= this.rowAreaBottom) continue;
            int top = Math.max(y, this.rowAreaTop);
            int bot = Math.min(yBot, this.rowAreaBottom);
            out.add(new int[]{row.getX(), top, row.getWidth(), bot - top});
        }
        FooterButton btn = this.saveBtn;
        if (btn != null && btn.visible) {
            out.add(new int[]{btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight()});
        }
    }

    public void openUrlWithConfirm(String url) {
        if (StringUtils.isBlank((CharSequence)url)) {
            return;
        }
        Minecraft.getInstance().setScreen((Screen)new ConfirmLinkScreen(confirmed -> {
            if (confirmed) {
                Util.getPlatform().openUri(url);
            }
            Minecraft.getInstance().setScreen((Screen)this);
        }, url, true));
    }
}

