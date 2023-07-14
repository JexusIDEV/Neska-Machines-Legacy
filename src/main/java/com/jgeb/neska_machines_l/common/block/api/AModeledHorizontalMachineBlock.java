package com.jgeb.neska_machines_l.common.block.api;

import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import com.jgeb.neska_machines_l.util.math.NeskaMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public abstract class AModeledHorizontalMachineBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty WORKING = NeskaBooleansProperties.WORKING;
    public static Optional<VoxelShape> SHAPE = null;
    public static Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    public AModeledHorizontalMachineBlock(Properties pProperties) {
        super(pProperties.lightLevel(state -> state.getValue(WORKING) ? 8 : 0));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WORKING, false));
        runCollisionVoxel(SHAPE.orElse(Shapes.block()));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES.get(pState.getValue(FACING));
    }

    protected void runCollisionVoxel(VoxelShape shape) {
        for (Direction direction : Direction.values())
            SHAPES.put(direction, NeskaMath.calculateShapes(direction, shape));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
        pBuilder.add(WORKING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
