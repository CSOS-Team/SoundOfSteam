package com.finchy.pipeorgans.content.stop;

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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class StopBlock extends Block implements IBE<StopBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public StopBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, POWERED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        return Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, facing)
                .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public Class<StopBlockEntity> getBlockEntityClass() {
        return StopBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StopBlockEntity> getBlockEntityType() {
        return AllBlockEntities.STOP_BLOCK_ENTITY.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;
        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        withBlockEntityDo(level, pos, be -> be.openMainMenu((ServerPlayer) player));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && level.hasNeighborSignal(pos) && !state.getValue(POWERED))
            level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.isClientSide)
            return;
        boolean powered = level.hasNeighborSignal(pos);
        if (powered == state.getValue(POWERED))
            return;
        level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
        if (powered) // rising edge clears all pressed stops
            withBlockEntityDo(level, pos, StopBlockEntity::clearAllPressed);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
