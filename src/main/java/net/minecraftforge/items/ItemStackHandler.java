package net.minecraftforge.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ItemStackHandler implements IItemHandlerModifiable {
    protected final NonNullList<ItemStack> stacks;

    public ItemStackHandler() {
        this(1);
    }

    public ItemStackHandler(int size) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getSlots() {
        return this.stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= 0 && slot < this.stacks.size() ? this.stacks.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.stacks.size()) {
            this.stacks.set(slot, stack);
        }
    }
}
