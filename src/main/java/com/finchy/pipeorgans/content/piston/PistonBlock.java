package com.finchy.pipeorgans.content.piston;

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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class PistonBlock extends Block implements IBE<PistonBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PistonBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public Class<PistonBlockEntity> getBlockEntityClass() {
        return PistonBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PistonBlockEntity> getBlockEntityType() {
        return AllBlockEntities.PISTON_BLOCK_ENTITY.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;
        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        withBlockEntityDo(level, pos, be -> be.openMenu((ServerPlayer) player));
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState mirrorState, Mirror mirror) {
        return mirror == Mirror.NONE ? mirrorState : mirrorState.rotate(mirror.getRotation(mirrorState.getValue(FACING)));
    }
}
