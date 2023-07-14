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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AContainerBE extends BlockEntity implements IBlockEntityHelpers, IItemHandlerHelper {

    public ItemStackHandler itemManager;
    public LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();
    protected final int sizeContainer;

    public AContainerBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int sizeContainer) {
        super(pType, pPos, pBlockState);

        this.sizeContainer = sizeContainer;
        checkSizeContainer(sizeContainer);

        this.itemManager = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.itemHandler = LazyOptional.of(() -> this.itemManager);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.itemHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.itemManager.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemManager.deserializeNBT(pTag.getCompound("inventory"));
    }

    public ItemStackHandler getItemManager() {
        return this.itemManager;
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return this.itemHandler;
    }

    public int getSizeContainer() {
        return this.sizeContainer;
    }
}
