package net.minecraftforge.items;

import net.minecraft.world.item.ItemStack;

public interface IItemHandlerModifiable {
    int getSlots();

    ItemStack getStackInSlot(int slot);

    default void setStackInSlot(int slot, ItemStack stack) {
    }
}
