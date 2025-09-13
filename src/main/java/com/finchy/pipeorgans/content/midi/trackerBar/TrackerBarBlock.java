package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class TrackerBarBlock extends Block implements IBE<TrackerBarBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TrackerBarBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH));
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        return Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(FACING, facing);
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
            return InteractionResult.PASS;
        if (pHand.equals(InteractionHand.OFF_HAND))
            return InteractionResult.PASS;
        //if (pPlayer.getItemInHand(pHand).getItem() instanceof StopMasterBlockItem) // if player is linking stopmaster
        //    return InteractionResult.PASS;

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
}
