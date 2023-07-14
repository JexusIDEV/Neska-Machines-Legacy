package com.jgeb.neska_machines_l.common.block.be.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public interface IBlockEntityHelpers {

    default void checkSizeContainer(int size) {
        if(size <= 0) {
            size = 1;
            throw new IllegalStateException("WARN: Can't set a container size to 0");
        }
    }

    default <T extends BlockEntity> void clientTick() {

    }

    default <T extends BlockEntity> void commonTick() {

    }

    default <T extends BlockEntity> void serverTick() {

    }
}
