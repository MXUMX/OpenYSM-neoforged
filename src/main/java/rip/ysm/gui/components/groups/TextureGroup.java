/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 */
package rip.ysm.gui.components.groups;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import rip.ysm.gui.components.groups.CategoryGroup;

public final class TextureGroup
extends CategoryGroup {
    public TextureGroup() {
        super("_textures");
    }

    @Override
    public Component getTitle() {
        String key = "gui.openysm.animation.category._textures";
        if (I18n.exists((String)key)) {
            return Component.translatable((String)key);
        }
        return Component.literal((String)"Textures");
    }
}

