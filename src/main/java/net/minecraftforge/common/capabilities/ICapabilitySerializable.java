package net.minecraftforge.common.capabilities;

import net.minecraft.nbt.Tag;

public interface ICapabilitySerializable<T extends Tag> extends ICapabilityProvider {
    T serializeNBT();

    void deserializeNBT(T tag);
}
