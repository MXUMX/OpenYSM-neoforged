package net.minecraftforge.common.capabilities;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraftforge.common.util.LazyOptional;

public final class Capability<T> {
    public <R> LazyOptional<R> orEmpty(Capability<R> requested, LazyOptional<T> value) {
        return Objects.equals(this, requested) ? value.cast() : LazyOptional.empty();
    }

    public <R> LazyOptional<R> orEmpty(Capability<R> requested, Supplier<T> value) {
        return this.orEmpty(requested, LazyOptional.of(value));
    }
}
