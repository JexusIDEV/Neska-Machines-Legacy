package com.jgeb.neska_machines_l.common.block.menu;

import com.jgeb.neska_machines_l.common.block.be.ChemicalReactorBE;
import com.jgeb.neska_machines_l.common.block.be.CombustionGeneratorBE;
import com.jgeb.neska_machines_l.common.block.menu.api.AMenuContainer;
import com.jgeb.neska_machines_l.registry.BlockReg;
import com.jgeb.neska_machines_l.registry.MenuReg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ChemicalReactorMenu extends AMenuContainer {

    public final ChemicalReactorBE BE;

    public ChemicalReactorMenu(int pContainerId, Inventory inventory, BlockEntity be, Block block, ContainerData containerData) {
        super(MenuReg.CHEMICAL_REACTOR_MENU.get(), pContainerId, inventory, be, block, containerData);
        this.BE = (ChemicalReactorBE) be;

        xHotBar = 8;
        yHotBar = 141;
        xInv = 8;
        yInv = 83;

        ((ChemicalReactorBE)be).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 35, 21));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 53, 21));
            this.addSlot(new SlotItemHandler(iItemHandler, 2,113, 21));
            this.addSlot(new SlotItemHandler(iItemHandler, 3,137, 21));
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 8, 49));
        });
    }

    public ChemicalReactorMenu(int pContainerId, Inventory inventory, FriendlyByteBuf dataX) {
        this(pContainerId, inventory, inventory.player.level.getBlockEntity(dataX.readBlockPos()), BlockReg.CHEMICAL_REACTOR.get(), new SimpleContainerData(2));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return super.stillValid(pPlayer);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public int getDataContainer(int data) {
        return this.getBE().getContainerData().get(data);
    }

    public ChemicalReactorBE getBE() {
        return this.BE;
    }
}
