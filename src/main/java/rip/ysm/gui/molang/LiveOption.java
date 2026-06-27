/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 */
package rip.ysm.gui.molang;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import rip.ysm.gui.Option;

final class LiveOption<T>
extends Option<T> {
    private final Supplier<T> liveGetter;
    private final String titleText;
    private final String descText;

    LiveOption(String titleText, String descText, Supplier<T> getter, Consumer<T> setter) {
        super("", getter, setter);
        this.liveGetter = getter;
        this.titleText = titleText;
        this.descText = descText == null ? "" : descText;
    }

    @Override
    public T get() {
        return this.liveGetter.get();
    }

    @Override
    public Component getLabel() {
        return Component.literal((String)this.titleText);
    }

    @Override
    public Component getDescription() {
        return Component.literal((String)this.descText);
    }

    @Override
    public void setPending(T value) {
        super.setPending(value);
        this.apply();
    }
}

