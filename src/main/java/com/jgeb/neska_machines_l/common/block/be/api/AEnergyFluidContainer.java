package com.jgeb.neska_machines_l.common.block.be.api;

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
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AEnergyFluidContainer extends AEnergyContainerBE {

    public final FluidTank fluidManager;
    public LazyOptional<IFluidTank> fluidHandler;
    public final int fluidCapacity;

    public AEnergyFluidContainer(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int sizeContainer, int fluidCapacity) {
        super(pType, pPos, pBlockState, sizeContainer, 0, 0, 0, 0);

        this.fluidCapacity = fluidCapacity;
        if(fluidCapacity <= 0) {
            fluidCapacity = 1000;
            throw new IllegalStateException("WARN: can't set a fluid tank to 0, returning to 1 buckets capacity");
        }

        this.fluidManager = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.fluidHandler = LazyOptional.of(() -> this.fluidManager);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.fluidHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("fluid", this.fluidManager.writeToNBT(pTag));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fluidManager.readFromNBT(pTag.getCompound("fluid"));
    }

    public FluidTank getFluidManager() {
        return this.fluidManager;
    }

    public LazyOptional<IFluidTank> getFluidHandler() {
        return this.fluidHandler;
    }
}
