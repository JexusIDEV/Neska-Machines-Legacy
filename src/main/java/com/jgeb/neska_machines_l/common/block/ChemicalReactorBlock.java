package com.jgeb.neska_machines_l.common.block;

import com.jgeb.neska_machines_l.common.block.api.AHorizontalMachineBlock;
import com.jgeb.neska_machines_l.common.block.be.ChemicalReactorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChemicalReactorBlock extends AHorizontalMachineBlock implements EntityBlock {

    public ChemicalReactorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ChemicalReactorBE(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pBlockEntity instanceof ChemicalReactorBE machine) {
                machine.serverTick(pLevel1, pPos, pState1, pBlockEntity);
            }
        };
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, @NotNull Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
            if(!pLevel.isClientSide) {
                BlockEntity be = pLevel.getBlockEntity(pPos);
                if(be instanceof ChemicalReactorBE machine) {
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, machine, pPos);
                } else {
                    throw new IllegalStateException("ERROR: ¡¡¡BE or MenuProvider is Broken or Missing!!!");
                }
            }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}
