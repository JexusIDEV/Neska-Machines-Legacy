package com.jgeb.neska_machines_l.common.block.api;

import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import com.jgeb.neska_machines_l.util.math.NeskaMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public abstract class ADirectionalModeledMachineBlock extends DirectionalBlock {

    public static BooleanProperty WORKING = NeskaBooleansProperties.WORKING;

    public static Optional<VoxelShape> SHAPE = Optional.of(createModel());
    public static Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    protected ADirectionalModeledMachineBlock(Properties pProperties) {
        super(pProperties.lightLevel(state -> state.getValue(WORKING) ? 8 : 0));
        this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WORKING, false);
    }

    public static VoxelShape createModel() {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES.get(pState.getValue(FACING));
    }

    protected void runCollisionVoxel(VoxelShape shape) {
        for (Direction direction : Direction.values())
            SHAPES.put(direction, NeskaMath.calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING).add(WORKING);
    }
}
