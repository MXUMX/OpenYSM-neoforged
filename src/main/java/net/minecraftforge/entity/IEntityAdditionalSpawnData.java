package net.minecraftforge.entity;

import net.minecraft.network.FriendlyByteBuf;

public interface IEntityAdditionalSpawnData {
    default void writeSpawnData(FriendlyByteBuf buffer) {
    }

    default void readSpawnData(FriendlyByteBuf additionalData) {
    }
}
