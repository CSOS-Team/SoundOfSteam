package com.finchy.pipeorgans.content.noteLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.google.common.collect.ImmutableMap;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class NoteLinkBlock extends WrenchableDirectionalBlock implements IBE<NoteLinkBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty RECEIVER = BooleanProperty.create("receiver");


    public NoteLinkBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(POWERED, false)
                .setValue(RECEIVER, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, RECEIVER);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        state = state.setValue(FACING, context.getClickedFace());
        return state;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (toggleMode(state, context.getLevel(), context.getClickedPos()) == InteractionResult.SUCCESS) {
             context.getLevel()
                     .scheduleTick(context.getClickedPos(), this, 1);
            return InteractionResult.SUCCESS;
        }
        return super.onWrenched(state, context);
    }

    public InteractionResult onEmptyHandShiftUse(BlockState state, Level level, BlockPos pos) {
        if (toggleMode(state, level, pos) == InteractionResult.SUCCESS) {
            level.scheduleTick(pos, this, 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public InteractionResult toggleMode(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        PipeOrgans.LOGGER.debug("Toggling Musical Link Block mode at {}", pos);
        return onBlockEntityUse(level, pos, be -> {
            boolean wasReceiver = state.getValue(RECEIVER);
            level.setBlock(pos, state.setValue(RECEIVER, !wasReceiver), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AllShapes.REDSTONE_BRIDGE.get(state.getValue(FACING));
    }

    @Override
    protected @NotNull ImmutableMap<BlockState, VoxelShape> getShapeForEachState(@NotNull Function<BlockState, VoxelShape> pShapeGetter) {
        return super.getShapeForEachState(state -> AllShapes.REDSTONE_BRIDGE.get(state.getValue(FACING)));
    }

    @Override
    public Class<NoteLinkBlockEntity> getBlockEntityClass() {
        return NoteLinkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends NoteLinkBlockEntity> getBlockEntityType() {
        return AllBlockEntities.NOTE_LINK_BLOCK_ENTITY.get();
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (state.hasBlockEntity()) {
                IBE.onRemove(state, level, pos, newState);
            }
        }
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction _targetedFace) {
        return originalState;
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos,
                                boolean isMoving) {
        if (level.isClientSide)
            return;

        Direction blockFacing = state.getValue(FACING);
        if (fromPos.equals(pos.relative(blockFacing.getOpposite()))) {
            if (!canSurvive(state, level, pos)) {
                level.destroyBlock(pos, true);
                return;
            }
        }

        if (!level.getBlockTicks()
                .willTickThisTick(pos, this))
            level.scheduleTick(pos, this, 1);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos neighbourPos = pos.relative(state.getValue(FACING)
                .getOpposite());
        BlockState neighbour = worldIn.getBlockState(neighbourPos);
        return !neighbour.canBeReplaced();
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource r) {
        updateTransmittedSignal(state, level, pos);

        if (state.getValue(RECEIVER))
            return;
        Direction attachedFace = state.getValue(RedstoneLinkBlock.FACING)
                .getOpposite();
        BlockPos attachedPos = pos.relative(attachedFace);
        level.blockUpdated(pos, level.getBlockState(pos)
                .getBlock());
        level.blockUpdated(attachedPos, level.getBlockState(attachedPos)
                .getBlock());
    }

    @Override
    public void onPlace(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getBlock() == oldState.getBlock() || isMoving)
            return;
        updateTransmittedSignal(state, worldIn, pos);
    }

    public void updateTransmittedSignal(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide)
            return;
        if (state.getValue(RECEIVER))
            return;

        int power = getPower(level, state, pos);

        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != power > 0)
            level.setBlock(pos, state.cycle(POWERED), Block.UPDATE_CLIENTS);

        withBlockEntityDo(level, pos, be -> be.transmit(power));
    }

    private static int getPower(Level level, BlockState state, BlockPos pos) {
        int power = 0;
        for (Direction direction : Iterate.directions)
            power = Math.max(level.getSignal(pos.relative(direction), direction), power);
        for (Direction direction : Iterate.directions) {
            if (state.getValue(FACING).getOpposite() != direction)
                power = Math.max(level.getSignal(pos.relative(direction), Direction.UP), power);
        }
        return power;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(POWERED) && state.getValue(RECEIVER);
    }

    @Override
    public int getDirectSignal(BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        if (side != blockState.getValue(FACING))
            return 0;
        return getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState state, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        if (!state.getValue(RECEIVER))
            return 0;
        return getBlockEntityOptional(blockAccess, pos).map(NoteLinkBlockEntity::getReceivedSignal)
                .orElse(0);
    }
}
