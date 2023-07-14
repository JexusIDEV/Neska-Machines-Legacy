package com.jgeb.neska_machines_l.common.block.be;

import com.jgeb.neska_machines_l.api.caps.NeskaEnergy;
import com.jgeb.neska_machines_l.common.block.be.api.ANeskaMachineBE;
import com.jgeb.neska_machines_l.common.block.menu.CompressorMenu;
import com.jgeb.neska_machines_l.common.recipes.CompressorRecipe;
import com.jgeb.neska_machines_l.registry.BlockEntityReg;
import com.jgeb.neska_machines_l.registry.NetworkReg;
import com.jgeb.neska_machines_l.registry.RecipeReg;
import com.jgeb.neska_machines_l.registry.packets.common.SCCompressorPacket;
import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import com.jgeb.neska_machines_l.util.lang.NeskaLangTranslate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CompressorBE extends ANeskaMachineBE implements MenuProvider {

    private CompressorRecipe recipe;

    public CompressorBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityReg.COMPRESSOR_BE.get(), pPos, pBlockState, 3, 10000, 10, 0, 0 ,0);

        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CompressorBE.this.progress;
                    case 1 -> CompressorBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0: CompressorBE.this.progress = pValue;
                    case 1: CompressorBE.this.maxProgress = pValue;
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
                    case 0, 1:
                        return true;
                    case 2:
                        return false;
                    default:
                        return super.isItemValid(slot, stack);
                }
            }
        };
        this.containerAccess = new SimpleContainer(this.getItemManager().getSlots());

        this.energyManager = new NeskaEnergy(this.energyCapacity, this.receive, this.extract) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                setChanged();
                NetworkReg.sendToClients(new SCCompressorPacket(this.energy, getBlockPos()));
                return super.receiveEnergy(maxReceive, simulate);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                setChanged();
                NetworkReg.sendToClients(new SCCompressorPacket(this.energy, getBlockPos()));
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
        if(this.level.isClientSide || this.level == null) return;
    }

    @Override
    public boolean hasRecipe() {
        for (int i = 0; i < getItemManager().getSlots(); i++) {
            this.containerAccess.setItem(i, getItemManager().getStackInSlot(i));
        }
        Optional<CompressorRecipe> recipe = level.getRecipeManager().getRecipeFor(CompressorRecipe.Type.INSTANCE, this.containerAccess, level);
        return recipe.isPresent() && canInsertOutput(this.containerAccess, recipe.get().getResultItem(), 2, 2) && enoughItems(this.containerAccess, 0);
    }

    @Override
    public void craftItem() {
        for (int i = 0; i < this.itemManager.getSlots(); i++) {
            this.containerAccess.setItem(i, this.itemManager.getStackInSlot(i));
        }
        Optional<CompressorRecipe> recipe = level.getRecipeManager().getRecipeFor(CompressorRecipe.Type.INSTANCE, this.containerAccess, level);

        if(hasRecipe()) {
            this.itemManager.extractItem(0, recipe.get().getCount(), false);
            this.itemManager.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem()));
            this.setProgress(0);
            setChanged();
        }
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
        return new CompressorMenu(pContainerId, pPlayerInventory, this, getBlockState().getBlock(), this.containerData);
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
