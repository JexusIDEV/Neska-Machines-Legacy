package com.jgeb.neska_machines_l.common.block.be.api;

import com.jgeb.neska_machines_l.api.caps.INeskaEnergyStorage;
import com.jgeb.neska_machines_l.api.caps.NeskaEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AEnergyContainerBE extends AContainerBE implements INeskaEnergyStorage {

    public final NeskaEnergy energyManager;
    public LazyOptional<IEnergyStorage> energyHandler;
    public int energy;
    public int receive;
    public int extract;
    public int capacity;

    public int FE_PRODUCTION_PER_TICK;

    public AEnergyContainerBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int sizeContainer, int energy, int receive, int extract, int capacity) {
        super(pType, pPos, pBlockState, sizeContainer);
        this.energy = energy;
        this.receive = receive;
        this.extract = extract;
        this.capacity = capacity;

        this.energyManager = new NeskaEnergy(capacity,receive, extract, energy) {
            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                setChanged();
                return super.extractEnergy(maxExtract, simulate);
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                setChanged();
                return super.receiveEnergy(maxReceive, simulate);
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.energyHandler = LazyOptional.of(() -> this.energyManager);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ENERGY ? this.energyHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("energy", this.energyManager.getEnergyStored());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.energyManager.setEnergy(pTag.getInt("energy"));
    }

    public EnergyStorage getEnergyManager() {
        return this.energyManager;
    }

    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return this.energyHandler;
    }

    @Override
    public int setEnergy(int amount) {
        return this.energy = amount;
    }

    @Override
    public int setReceive(int amount) {
        return this.receive = amount;
    }

    @Override
    public int setExtract(int amount) {
        return this.extract = amount;
    }

    @Override
    public int setCapacity(int amount) {
        return this.capacity = amount;
    }

    public void setFE_PRODUCTION_PER_TICK(int FE_PRODUCTION_PER_TICK) {
        this.FE_PRODUCTION_PER_TICK = FE_PRODUCTION_PER_TICK;
    }
}
