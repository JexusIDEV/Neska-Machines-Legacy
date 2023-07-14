package com.jgeb.neska_machines_l.api.caps;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;

public abstract class NeskaEnergy extends EnergyStorage implements INeskaEnergyStorage {

    public NeskaEnergy(int capacity) {
        super(capacity);
    }

    public NeskaEnergy(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public NeskaEnergy(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public NeskaEnergy(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int setEnergy(int amount) {
        return this.energy = Math.max(0, Math.min(amount, this.capacity));
    }

    @Override
    public int setReceive(int amount) {
        return this.maxReceive = amount;
    }

    @Override
    public int setExtract(int amount) {
        return this.maxExtract = amount;
    }

    @Override
    public int setCapacity(int amount) {
        return this.capacity = amount;
    }
}
