package com.jgeb.neska_machines_l.common.block.be.api;

import com.jgeb.neska_machines_l.api.caps.NeskaEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ANeskaMachineBE extends BlockEntity {

    public ItemStackHandler itemManager = null;
    public LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();
    public static ContainerData containerData = null;
    protected int containerSize = 0;
    public SimpleContainer containerAccess;

    public NeskaEnergy energyManager = null;
    public LazyOptional<IEnergyStorage> energyHandler = LazyOptional.empty();
    public int energyCapacity = 0;
    public int receive = 0;
    public int extract = 0;
    public int energyStored = 0;
    public int productionPerTick = 0;
    public int consumePerTick = 0;

    public FluidTank fluidManager = null;
    public LazyOptional<IFluidTank> fluidHandler = LazyOptional.empty();
    public int fluidCapacity = 0;

    public int burn, maxBurn = 0;
    public int progress, maxProgress = 0;

    //other things
    protected boolean isRedstone = false;

    public ANeskaMachineBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int containerSize, int energyCapacity, int receive, int extract, int energyStored, int fluidCapacity) {
        super(pType, pPos, pBlockState);
        this.containerSize = containerSize;
        this.energyCapacity = energyCapacity;
        this.receive = receive;
        this.extract = extract;
        this.energyStored = energyStored;
        this.fluidCapacity = fluidCapacity;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if(tag != null) {
            handleUpdateTag(tag);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if(tag != null) {
            load(tag);
        }
    }

    abstract public <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState state, T BE);

    public <T extends BlockEntity> void sendEnergyOutputAllSides(T _be, NeskaEnergy energyManager, int extract) {
        if (energyManager.getEnergyStored() >= extract && energyManager.canExtract()) {
            for (final var direction : Direction.values()) {
                final BlockEntity be = _be.getLevel().getBlockEntity(_be.getBlockPos().relative(direction));
                if (be == null) {
                    continue;
                }
                be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                    if (be != this && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                        final int toSend = energyManager.extractEnergy(extract,
                                false);
                        final int received = storage.receiveEnergy(toSend, false);
                        energyManager.setEnergy(energyManager.getEnergyStored() + toSend - received);
                    }
                });
            }
        }
    }

    abstract public boolean hasRecipe();

    abstract public void craftItem();

    public boolean enoughEnergy(int consumePerTick) {
        return getEnergyManager().getEnergyStored() > consumePerTick;
    }

    public boolean enoughItems(SimpleContainer container, int slot) {
        return container.getItem(slot).getMaxStackSize() > container.getItem(slot).getCount();
    }

    public boolean canInsertOutput(SimpleContainer container, ItemStack stack, int slot1, int slot2) {
        return container.getItem(slot1).getItem() == stack.getItem() || container.getItem(slot2).isEmpty();
    }

    public ContainerData getContainerData() {
        return this.containerData;
    }

    public ItemStackHandler getItemManager() {
        return this.itemManager;
    }

    public void setItemManager(ItemStackHandler itemManager) {
        this.itemManager = itemManager;
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return this.itemHandler;
    }

    public void setItemHandler(LazyOptional<IItemHandler> itemHandler) {
        this.itemHandler = itemHandler;
    }

    public int getContainerSize() {
        return this.containerSize;
    }

    public void setContainerSize(int containerSize) {
        this.containerSize = containerSize;
    }

    public NeskaEnergy getEnergyManager() {
        return this.energyManager;
    }

    public void setEnergyManager(NeskaEnergy energyManager) {
        this.energyManager = energyManager;
    }

    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return this.energyHandler;
    }

    public void setEnergyHandler(LazyOptional<IEnergyStorage> energyHandler) {
        this.energyHandler = energyHandler;
    }

    public int getEnergyCapacity() {
        return this.energyCapacity;
    }

    public void setEnergyCapacity(int energyCapacity) {
        this.energyCapacity = energyCapacity;
    }

    public int getReceive() {
        return this.receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public int getExtract() {
        return this.extract;
    }

    public void setExtract(int extract) {
        this.extract = extract;
    }

    public int getEnergyStored() {
        return this.energyStored;
    }

    public void setEnergyStored(int energyStored) {
        this.energyStored = energyStored;
    }

    public int getProductionPerTick() {
        return this.productionPerTick;
    }

    public void setProductionPerTick(int productionPerTick) {
        this.productionPerTick = productionPerTick;
    }

    public int getConsumePerTick() {
        return this.consumePerTick;
    }

    public void setConsumePerTick(int consumePerTick) {
        this.consumePerTick = consumePerTick;
    }

    public FluidTank getFluidManager() {
        return this.fluidManager;
    }

    public void setFluidManager(FluidTank fluidManager) {
        this.fluidManager = fluidManager;
    }

    public LazyOptional<IFluidTank> getFluidHandler() {
        return this.fluidHandler;
    }

    public void setFluidHandler(LazyOptional<IFluidTank> fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    public int getFluidCapacity() {
        return this.fluidCapacity;
    }

    public void setFluidCapacity(int fluidCapacity) {
        this.fluidCapacity = fluidCapacity;
    }

    public int getBurn() {
        return this.burn;
    }

    public void setBurn(int burn) {
        this.burn = burn;
    }

    public int getMaxBurn() {
        return this.maxBurn;
    }

    public void setMaxBurn(int maxBurn) {
        this.maxBurn = maxBurn;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public boolean isRedstone() {
        return this.isRedstone;
    }

    public void setRedstone(boolean redstone) {
        this.isRedstone = redstone;
    }
}
