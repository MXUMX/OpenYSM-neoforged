package net.minecraftforge.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICapabilityProvider {
    @NotNull
    <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction);

    @NotNull
    default <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability) {
        return this.getCapability(capability, null);
    }
}
