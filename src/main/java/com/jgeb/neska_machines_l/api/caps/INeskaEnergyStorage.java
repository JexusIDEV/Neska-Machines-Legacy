package com.jgeb.neska_machines_l.api.caps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public interface INeskaEnergyStorage {

    int setEnergy(int amount);
    int setReceive(int amount);
    int setExtract(int amount);
    int setCapacity(int amount);

    default boolean isEnergyZero(int energy) {
        return energy == 0;
    }

    default boolean negativeToInfinite(int energy) {
        return energy < 0;
    }

    default void sendOutPower(EnergyStorage storage, Level level, BlockPos pos, int extract) {
        AtomicInteger capacity = new AtomicInteger(storage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
                BlockEntity be = level.getBlockEntity(pos.relative(direction));
                if (be != null) {
                    boolean doContinue = be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).map(handler -> {
                                if (handler.canReceive()) {
                                    int received = handler.receiveEnergy(Math.min(capacity.get(), extract), false);
                                    capacity.addAndGet(-received);
                                    storage.extractEnergy(received, false);
                                    be.setChanged();
                                    return capacity.get() > 0;
                                } else {
                                    return true;
                                }
                            }
                    ).orElse(true);
                    if (!doContinue) {
                        return;
                    }
                }
            }
        }
    }
}
