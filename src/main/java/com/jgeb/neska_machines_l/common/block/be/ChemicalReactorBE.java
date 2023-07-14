package com.jgeb.neska_machines_l.common.block.be;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.api.caps.NeskaEnergy;
import com.jgeb.neska_machines_l.common.block.be.api.AContainerBE;
import com.jgeb.neska_machines_l.common.block.be.api.AEnergyContainerBE;
import com.jgeb.neska_machines_l.common.block.be.api.ANeskaMachineBE;
import com.jgeb.neska_machines_l.common.block.be.api.IEnergyUtils;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.registry.BlockEntityReg;
import com.jgeb.neska_machines_l.registry.NetworkReg;
import com.jgeb.neska_machines_l.registry.packets.common.SCChemicalReactorPacket;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChemicalReactorBE extends ANeskaMachineBE implements MenuProvider {

    public ChemicalReactorBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityReg.CHEMICAL_REACTOR_BE.get(), pPos, pBlockState, 5, 16000, 10, 0, 0 ,0);

        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ChemicalReactorBE.this.progress;
                    case 1 -> ChemicalReactorBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0: ChemicalReactorBE.this.progress = pValue;
                    case 1: ChemicalReactorBE.this.maxProgress = pValue;
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
                    case 0, 1, 4:
                        return true;
                    case 2, 3:
                        return false;
                    default:
                        return super.isItemValid(slot, stack);
                }
            }
        };
        this.energyManager = new NeskaEnergy(this.energyCapacity, this.receive, this.extract) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                setChanged();
                NetworkReg.sendToClients(new SCChemicalReactorPacket(this.energy, getBlockPos()));
                return super.receiveEnergy(maxReceive, simulate);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                setChanged();
                NetworkReg.sendToClients(new SCChemicalReactorPacket(this.energy, getBlockPos()));
                return super.extractEnergy(maxExtract, simulate);
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
    public <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState state, T BE) {
        if(level.isClientSide) return;

        //NetworkReg.sendToClients(new SCChemicalReactorPacket(this.getEnergyManager().getEnergyStored(), getBlockPos()));

        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.getValue(NeskaBooleansProperties.WORKING) != this.progress < 0) {
            level.setBlock(worldPosition, blockState.setValue(NeskaBooleansProperties.WORKING, this.progress > 0),
                    Block.UPDATE_ALL);
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
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemManager.deserializeNBT(pTag.getCompound("inventory"));
        this.energyManager.setEnergy(pTag.getInt("energy"));
        this.progress = pTag.getInt("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.itemManager.serializeNBT());
        pTag.putInt("energy", this.energyManager.getEnergyStored());
        pTag.putInt("progress", this.progress);
    }

    @Override
    public Component getDisplayName() {
        return NeskaLangTranslate.createContainerTittle(getBlockState().getBlock());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ChemicalReactorMenu(pContainerId, pPlayerInventory, this, getBlockState().getBlock(), this.containerData);
    }

    @Override
    public int getProgress() {
        return super.getProgress();
    }

    @Override
    public int getMaxProgress() {
        return super.getMaxProgress();
    }

    @Override
    public void setProgress(int progress) {
        super.setProgress(progress);
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        super.setMaxProgress(maxProgress);
    }

    @Override
    public NeskaEnergy getEnergyManager() {
        return super.getEnergyManager();
    }

    @Override
    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return super.getEnergyHandler();
    }
}
