/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 */
package rip.ysm.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;
import rip.ysm.gui.OptionRow;

public class OptionGroup {
    private final String translationKey;
    private final List<OptionRow<?>> rows = new ArrayList();

    public OptionGroup(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public Component getTitle() {
        return Component.translatable((String)("gui.openysm.config.group." + this.translationKey));
    }

    public OptionGroup add(OptionRow<?> row) {
        this.rows.add(row);
        return this;
    }

    public List<OptionRow<?>> getRows() {
        return Collections.unmodifiableList(this.rows);
    }

    public boolean isDirty() {
        for (OptionRow<?> row : this.rows) {
            if (row.getOption() == null || !row.getOption().isDirty()) continue;
            return true;
        }
        return false;
    }

    public void apply() {
        for (OptionRow<?> row : this.rows) {
            if (row.getOption() == null) continue;
            row.getOption().apply();
        }
    }

    public void undo() {
        for (OptionRow<?> row : this.rows) {
            if (row.getOption() != null) {
                row.getOption().undo();
            }
            row.refresh();
        }
    }
}

