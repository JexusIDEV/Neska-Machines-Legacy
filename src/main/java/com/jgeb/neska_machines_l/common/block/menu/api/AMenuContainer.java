package com.jgeb.neska_machines_l.common.block.menu.api;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AMenuContainer extends AbstractContainerMenu {

    protected Block targetBlock;
    protected ContainerLevelAccess access;
    protected BlockEntity targetBE;
    public static int xInv = 0;
    public static int yInv = 0;
    public static int xHotBar = 0;
    public static int yHotBar = 0;

    protected AMenuContainer(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, BlockEntity be, Block block, ContainerData containerData) {
        super(pMenuType, pContainerId);

        this.targetBE = be;
        this.access = ContainerLevelAccess.create(inventory.player.level, targetBE.getBlockPos());
        this.targetBlock = block;

        //hot-bar player
        for(int h=0; h<9; h++) {
            this.addSlot(new Slot(inventory, h, xHotBar + (h * 18), yHotBar));
        }

        //inventory player
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, xInv + i1 * 18, yInv + k * 18));
            }
        }

        addDataSlots(containerData);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, targetBlock);
    }

    public void createIteratedSlotsItem(int x, int y, int slotsX, int slotsY, int startSlots, IItemHandler handle) {
        for(int row=0; row<slotsX; row++) {
            for(int file=0; file<slotsY; file++) {
                this.addSlot(new SlotItemHandler(handle, (row + file) * startSlots, x + (row*18), y+(file*18)));
            }
        }
    }
    public void createIteratedSlotsItem(int x, int y, int slotsX, int slotsY, int startSlots, int dataFix, IItemHandler handle) {
        for(int row=0; row<slotsX; row++) {
            for(int file=0; file<slotsY; file++) {
                this.addSlot(new SlotItemHandler(handle, (row + file) * startSlots + dataFix, x + (row*18), y+(file*18)));
            }
        }
    }
    public void createSingleSlotsItemHorizontal(int x, int y, int slotsH, IItemHandler handle) {
        for(int h=0; h<slotsH; h++) {
            this.addSlot(new SlotItemHandler(handle, slotsH, x + (h * 18), y));
        }
    }
    public void createSingleSlotsItemHorizontal(int x, int y, int slotsH, int startSlots, IItemHandler handle) {
        for(int h=0; h<slotsH; h++) {
            this.addSlot(new SlotItemHandler(handle, slotsH + startSlots, x + (h * 18), y));
        }
    }
    public void createSingleSlotsItemVertical(int x, int y, int slotsV, IItemHandler handle) {
        for(int v=0; v<slotsV; v++) {
            this.addSlot(new SlotItemHandler(handle, slotsV, x, y+(v * 18)));
        }
    }
    public void createSingleSlotsItemVertical(int x, int y, int slotsV, int startSlots, IItemHandler handle) {
        for(int v=0; v<slotsV; v++) {
            this.addSlot(new SlotItemHandler(handle, slotsV + startSlots, x, y+(v * 18)));
        }
    }
}
