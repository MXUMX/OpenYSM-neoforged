/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.neoforged.neoforge.common.ModConfigSpec$BooleanValue
 *  net.neoforged.neoforge.common.ModConfigSpec$DoubleValue
 *  net.neoforged.neoforge.common.ModConfigSpec$EnumValue
 */
package rip.ysm.gui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Option<T> {
    private final String translationKey;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private T pending;
    private boolean dirty;

    public Option(String translationKey, Supplier<T> getter, Consumer<T> setter) {
        this.translationKey = translationKey;
        this.getter = getter;
        this.setter = setter;
        this.pending = getter.get();
    }

    public static Option<Boolean> ofBoolean(String key, ModConfigSpec.BooleanValue cfg) {
        return new Option<Boolean>(key, () -> ((ModConfigSpec.BooleanValue)cfg).get(), arg_0 -> ((ModConfigSpec.BooleanValue)cfg).set(arg_0));
    }

    public static Option<Double> ofDouble(String key, ModConfigSpec.DoubleValue cfg) {
        return new Option<Double>(key, () -> ((ModConfigSpec.DoubleValue)cfg).get(), arg_0 -> ((ModConfigSpec.DoubleValue)cfg).set(arg_0));
    }

    public static <E extends Enum<E>> Option<E> ofEnum(String key, ModConfigSpec.EnumValue<E> cfg) {
        return new Option<E>(key, cfg::get, cfg::set);
    }

    public Component getLabel() {
        return Component.translatable((String)("gui.openysm.config." + this.translationKey));
    }

    public Component getDescription() {
        String descKey = "gui.openysm.config." + this.translationKey + ".desc";
        return Component.translatable((String)descKey);
    }

    public T get() {
        return this.pending;
    }

    public void setPending(T value) {
        this.pending = value;
        this.dirty = !Objects.equals(value, this.getter.get());
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void apply() {
        if (this.dirty) {
            this.setter.accept(this.pending);
            this.dirty = false;
        }
    }

    public void undo() {
        this.pending = this.getter.get();
        this.dirty = false;
    }
}
