package com.jgeb.neska_machines_l.common.block.be.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public interface IItemHandlerHelper {

    default ItemStack getItemInSlot(int slot, LazyOptional<IItemHandler> handler) {
        return handler.map(inv -> inv.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    default ItemStack extractItem(int slot, LazyOptional<IItemHandler> handler) {
        final int count = getItemInSlot(slot, handler).getCount();
        return handler.map(inv -> inv.extractItem(slot, count, false)).orElse(ItemStack.EMPTY);
    }
    default ItemStack insertItem(int slot, ItemStack stack, LazyOptional<IItemHandler> handler) {
        final ItemStack copy = stack.copy();
        stack.shrink(copy.getCount());
        return handler.map(inv -> inv.insertItem(slot, copy, false)).orElse(ItemStack.EMPTY);
    }

    default boolean isItemValid(int slot, ItemStack stack, LazyOptional<IItemHandler> handler) {
        final ItemStack copy = stack.copy();
        return handler.map(inv -> inv.isItemValid(slot, copy)).orElse(false);
    }

    default <T extends BlockEntity> void dropItems(@NotNull ItemStackHandler handler, Level level, BlockPos pos) {
        SimpleContainer inventory = new SimpleContainer(handler.getSlots());
        for (int i = 0; i < handler.getSlots(); i++) {
            inventory.setItem(i, handler.getStackInSlot(i));
        }
        Containers.dropContents(level, pos, inventory);
    }

    default <T extends BlockEntity> boolean canInputItemSlot(T be, int Slot, ItemStack stack) {
        return false;
    }
    default <T extends BlockEntity> boolean canOutputItemSlot(T be, int Slot, ItemStack stack) {
        return false;
    }
}
