package net.minecraftforge.common.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LazyOptional<T> {
    private static final LazyOptional<?> EMPTY = new LazyOptional<>(null);

    private final Supplier<? extends T> supplier;

    private LazyOptional(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazyOptional<T> empty() {
        @SuppressWarnings("unchecked")
        LazyOptional<T> empty = (LazyOptional<T>) EMPTY;
        return empty;
    }

    public static <T> LazyOptional<T> of(Supplier<? extends T> supplier) {
        return new LazyOptional<>(Objects.requireNonNull(supplier));
    }

    public boolean isPresent() {
        return this.supplier != null && this.supplier.get() != null;
    }

    public T orElse(T fallback) {
        T value = this.supplier == null ? null : this.supplier.get();
        return value == null ? fallback : value;
    }

    public T orElseGet(Supplier<? extends T> fallback) {
        T value = this.supplier == null ? null : this.supplier.get();
        return value == null ? fallback.get() : value;
    }

    public T orElseThrow() {
        T value = this.supplier == null ? null : this.supplier.get();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        T value = this.supplier == null ? null : this.supplier.get();
        if (value != null) {
            consumer.accept(value);
        }
    }

    public Optional<T> resolve() {
        T value = this.supplier == null ? null : this.supplier.get();
        return Optional.ofNullable(value);
    }

    public <R> LazyOptional<R> map(Function<? super T, ? extends R> mapper) {
        return this.supplier == null ? empty() : of(() -> mapper.apply(this.supplier.get()));
    }

    @SuppressWarnings("unchecked")
    public <R> LazyOptional<R> flatMap(Function<? super T, ?> mapper) {
        if (this.supplier == null) {
            return empty();
        }
        return of(() -> {
            T value = this.supplier.get();
            if (value == null) {
                return null;
            }
            Object mapped = mapper.apply(value);
            if (mapped instanceof LazyOptional<?> lazyOptional) {
                return (R) lazyOptional.orElse(null);
            }
            if (mapped instanceof Optional<?> optional) {
                return (R) optional.orElse(null);
            }
            return (R) mapped;
        });
    }

    @SuppressWarnings("unchecked")
    public <R> LazyOptional<R> cast() {
        return (LazyOptional<R>) this;
    }
}
