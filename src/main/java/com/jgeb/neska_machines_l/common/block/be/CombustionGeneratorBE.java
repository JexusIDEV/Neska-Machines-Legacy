package com.jgeb.neska_machines_l.common.block.be;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.api.caps.NeskaEnergy;
import com.jgeb.neska_machines_l.common.block.be.api.AEnergyContainerBE;
import com.jgeb.neska_machines_l.common.block.be.api.ANeskaMachineBE;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.common.block.menu.CombustionGeneratorMenu;
import com.jgeb.neska_machines_l.registry.BlockEntityReg;
import com.jgeb.neska_machines_l.registry.NetworkReg;
import com.jgeb.neska_machines_l.registry.packets.common.SCCombustionGeneratorPacket;
import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import com.jgeb.neska_machines_l.util.lang.NeskaLangTranslate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CombustionGeneratorBE extends ANeskaMachineBE implements MenuProvider {

    public CombustionGeneratorBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityReg.COMBUSTION_GENERATOR_BE.get(), pPos, pBlockState, 3, 64000, 0, 10, 0, 0);

        containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CombustionGeneratorBE.this.burn;
                    case 1 -> CombustionGeneratorBE.this.maxBurn;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> CombustionGeneratorBE.this.setBurn(pValue);
                    case 1 -> CombustionGeneratorBE.this.setMaxBurn(pValue);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.itemManager = new ItemStackHandler(this.containerSize) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                switch (slot) {
                    case 0: return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
                    case 1, 2: return true;
                    default:
                        return super.isItemValid(slot, stack);
                }
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if(slot == 0 && ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) <= 0) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };

        this.energyManager = new NeskaEnergy(this.energyCapacity, this.receive, this.extract) {

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                setChanged();
                NetworkReg.sendToClients(new SCCombustionGeneratorPacket(this.energy, getBlockPos()));
                return super.extractEnergy(maxExtract, simulate);
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                setChanged();
                NetworkReg.sendToClients(new SCCombustionGeneratorPacket(this.energy, getBlockPos()));
                return super.receiveEnergy(maxReceive, simulate);
            }

            @Override
            public int setEnergy(int amount) {
                return super.setEnergy(amount);
            }

            @Override
            public int setReceive(int amount) {
                return super.setReceive(amount);
            }

            @Override
            public int setExtract(int amount) {
                return super.setExtract(amount);
            }

            @Override
            public int setCapacity(int amount) {
                return super.setCapacity(amount);
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.itemHandler = LazyOptional.of(() -> this.itemManager);
        this.energyHandler = LazyOptional.of(() -> this.energyManager);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandler.invalidate();
        this.energyHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandler.cast();
        }
        if(cap == ForgeCapabilities.ENERGY) {
            return this.energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.itemManager.serializeNBT());
        pTag.putInt("energy", this.energyManager.getEnergyStored());
        pTag.putInt("burning", this.burn);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemManager.deserializeNBT(pTag.getCompound("inventory"));
        this.energyManager.setEnergy(pTag.getInt("energy"));
        this.burn = pTag.getInt("burning");
    }

    @Override
    public Component getDisplayName() {
        return NeskaLangTranslate.createContainerTittle(getBlockState().getBlock());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CombustionGeneratorMenu(pContainerId, pPlayerInventory, this, getBlockState().getBlock(), this.containerData);
    }

    @Override
    public <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState state, T BE) {
        if(level.isClientSide) return;

        if(this.energyManager.getEnergyStored() < this.getEnergyCapacity()) {
            if (this.itemManager.isItemValid(0, this.getItemManager().getStackInSlot(0)) && burn <= 0) {
                this.maxBurn = ForgeHooks.getBurnTime(this.itemManager.getStackInSlot(0), RecipeType.SMELTING);
                this.burn = maxBurn;
                this.itemManager.extractItem(0, 1, false);
            }
            if (this.burn > 0) {
                this.burn--;
                getEnergyManager().setEnergy(getEnergyManager().getEnergyStored() + 10);
                NetworkReg.sendToClients(new SCCombustionGeneratorPacket(this.getEnergyManager().getEnergyStored(), getBlockPos()));
            }

            BlockState blockState = level.getBlockState(worldPosition);
            if (blockState.getValue(NeskaBooleansProperties.WORKING).booleanValue() != this.getContainerData().get(0) < 0) {
                blockState = blockState.setValue(NeskaBooleansProperties.WORKING, Boolean.valueOf(this.getContainerData().get(0)  > 0));
                this.level.setBlock(worldPosition, blockState, Block.UPDATE_ALL);
                setChanged();
            }

            sendEnergyOutputAllSides(this, this.energyManager, this.extract);
        }
    }

    @Override
    public boolean hasRecipe() {
        return false;
    }

    @Override
    public void craftItem() {

    }

    @Override
    public NeskaEnergy getEnergyManager() {
        return super.getEnergyManager();
    }

    @Override
    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return super.getEnergyHandler();
    }

    @Override
    public ItemStackHandler getItemManager() {
        return super.getItemManager();
    }

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return super.getItemHandler();
    }

    @Override
    public int getBurn() {
        return super.getBurn();
    }

    @Override
    public int getMaxBurn() {
        return super.getMaxBurn();
    }

    @Override
    public void setBurn(int burn) {
        super.setBurn(burn);
    }

    @Override
    public void setMaxBurn(int maxBurn) {
        super.setMaxBurn(maxBurn);
    }

    @Override
    public ContainerData getContainerData() {
        return super.getContainerData();
    }
}
