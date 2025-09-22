package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

@SuppressWarnings("NullableProblems")
public class TrackerBarBlock extends HorizontalKineticBlock implements IBE<TrackerBarBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public TrackerBarBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(POWERED, false)
        );
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<TrackerBarBlockEntity> getBlockEntityClass() {
        return TrackerBarBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrackerBarBlockEntity> getBlockEntityType() {
        return AllBlockEntities.TRACKER_BAR_BLOCK_ENTITY.get();
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;
        if (pHand.equals(InteractionHand.OFF_HAND))
            return InteractionResult.SUCCESS;

        withBlockEntityDo(pLevel, pPos, be -> {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, be, be::sendToMenu);
        });
        return InteractionResult.SUCCESS;
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            if (!pLevel.isClientSide) {
                withBlockEntityDo(pLevel, pPos, TrackerBarBlockEntity::onBlockRemoved);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return getRotationAxis(state) == face.getAxis();
    }
}
