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
import rip.ysm.gui.OptionGroup;

public class CategoryGroup
extends OptionGroup {
    private final String catKey;

    public CategoryGroup(String catKey) {
        super("animation_category." + catKey);
        this.catKey = catKey;
    }

    @Override
    public Component getTitle() {
        String key = "gui.openysm.animation.category." + this.catKey;
        if (I18n.exists((String)key)) {
            return Component.translatable((String)key);
        }
        return Component.literal((String)this.catKey);
    }
}

