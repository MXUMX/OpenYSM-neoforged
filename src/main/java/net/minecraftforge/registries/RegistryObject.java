package net.minecraftforge.registries;

import java.util.function.Supplier;

public class RegistryObject<T> implements Supplier<T> {
    private final Supplier<T> supplier;

    public RegistryObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return this.supplier.get();
    }
}
