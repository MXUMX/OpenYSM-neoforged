/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FormattedCharSequence
 *  org.apache.commons.lang3.StringUtils
 */
package rip.ysm.gui.components;

import org.openysm.client.gui.ModelMetadataPresenter;
import org.openysm.client.upload.IResourceLocatable;
import org.openysm.resource.models.AuthorInfo;
import org.openysm.util.data.OrderedStringMap;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;
import rip.ysm.gui.ModernModelInfoScreen;
import rip.ysm.gui.OptionRow;

public final class AuthorRow
extends OptionRow<Object> {
    private static final ResourceLocation DEFAULT_AVATAR = ResourceLocation.fromNamespaceAndPath("openysm", "texture/default_avatar.png");
    private static final int AVATAR_SIZE = 48;
    private final ModernModelInfoScreen owner;
    private final AuthorInfo author;
    private final int authorIndex;
    private final IResourceLocatable avatarLocatable;
    private int hoveredContactIndex = -1;

    public AuthorRow(ModernModelInfoScreen owner, AuthorInfo author, int authorIndex, IResourceLocatable avatarLocatable) {
        super(0, 0, 0, 56, null);
        this.owner = owner;
        this.author = author;
        this.authorIndex = authorIndex;
        this.avatarLocatable = avatarLocatable;
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean hover = this.isHoveredOrFocused();
        g.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), hover ? -1877534953 : -1879048192);
        int ax = this.getX() + 4;
        int ay = this.getY() + 4;
        ResourceLocation avatar = this.avatarLocatable != null ? this.avatarLocatable.getResourceLocation().orElse(DEFAULT_AVATAR) : DEFAULT_AVATAR;
        g.blit(avatar, ax, ay, 48, 48, 0.0f, 0.0f, 64, 64, 64, 64);
        Font font = Minecraft.getInstance().font;
        String name = ModelMetadataPresenter.getLocalizedModelString(this.owner.renderContext, "metadata.authors.%d.name".formatted(this.authorIndex), this.author.getName());
        String role = ModelMetadataPresenter.getLocalizedModelString(this.owner.renderContext, "metadata.authors.%d.role".formatted(this.authorIndex), this.author.getRole());
        String comment = ModelMetadataPresenter.getLocalizedModelString(this.owner.renderContext, "metadata.authors.%d.comment".formatted(this.authorIndex), this.author.getComment());
        int tx = ax + 48 + 8;
        int textRight = this.getX() + this.getWidth() - 6;
        int maxTextW = Math.max(40, textRight - tx);
        g.drawString(font, (Component)Component.literal((String)name).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), tx, this.getY() + 6, -1, false);
        if (StringUtils.isNotBlank((CharSequence)role)) {
            int nameW = font.width(name);
            g.drawString(font, (Component)Component.literal((String)role).withStyle(ChatFormatting.GREEN), tx + nameW + 8, this.getY() + 6, -1, false);
        }
        int commentY = this.getY() + 17;
        if (StringUtils.isNotBlank((CharSequence)comment)) {
            List lines = font.split((FormattedText)Component.literal((String)comment), maxTextW);
            int max = Math.min(lines.size(), 2);
            for (int i = 0; i < max; ++i) {
                g.drawString(font, (FormattedCharSequence)lines.get(i), tx, commentY, -3355444, false);
                commentY += 10;
            }
        }
        this.hoveredContactIndex = -1;
        OrderedStringMap<String, String> contacts = this.author.getContact();
        if (contacts != null && contacts.size() > 0) {
            String key;
            int chipW;
            int chipY = this.getY() + this.getHeight() - 14;
            int chipX = tx;
            for (int i = 0; i < contacts.size() && chipX + (chipW = font.width(key = contacts.getKeyAt(i)) + 8) <= textRight; ++i) {
                boolean chipHover = mouseX >= chipX && mouseX < chipX + chipW && mouseY >= chipY && mouseY < chipY + 12;
                g.fill(chipX, chipY, chipX + chipW, chipY + 12, chipHover ? -1069267900 : -2145246686);
                g.drawString(font, (Component)Component.literal((String)key).withStyle(ChatFormatting.YELLOW), chipX + 4, chipY + 2, -1, false);
                if (chipHover) {
                    this.hoveredContactIndex = i;
                }
                chipX += chipW + 4;
            }
        }
    }

    @Override
    protected void renderControl(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    public void onClick(double mouseX, double mouseY) {
        if (this.hoveredContactIndex < 0) {
            return;
        }
        OrderedStringMap<String, String> contacts = this.author.getContact();
        if (contacts == null || this.hoveredContactIndex >= contacts.size()) {
            return;
        }
        String value = contacts.getValueAt(this.hoveredContactIndex);
        if (StringUtils.isBlank((CharSequence)value)) {
            return;
        }
        if (value.startsWith("http://") || value.startsWith("https://")) {
            this.owner.openUrlWithConfirm(value);
        } else {
            Minecraft.getInstance().keyboardHandler.setClipboard(value);
        }
    }
}
