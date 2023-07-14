package com.jgeb.neska_machines_l.common.block.api;

import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class AHorizontalMachineBlock extends HorizontalDirectionalBlock {

    public static BooleanProperty WORKING = NeskaBooleansProperties.WORKING;

    public AHorizontalMachineBlock(Properties pProperties) {
        super(pProperties.lightLevel(state -> state.getValue(WORKING) ? 8 : 0));
        this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WORKING, false);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING).add(WORKING);
    }
}
