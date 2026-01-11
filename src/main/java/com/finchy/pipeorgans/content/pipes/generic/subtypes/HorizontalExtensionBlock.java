package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class HorizontalExtensionBlock extends GenericExtensionBlock<ExtensionShapes.Horizontal> {

    public static final EnumProperty<ExtensionShapes.Horizontal> SHAPE = EnumProperty.create("shape", ExtensionShapes.Horizontal.class);

    public HorizontalExtensionBlock(Properties properties) {
        super(properties, SHAPE);
    }

    @Override
    protected void registerDefaultStateWithSize() {
        BlockState state = defaultBlockState()
                .setValue(SHAPE, ExtensionShapes.Horizontal.SINGLE)
                .setValue(SIZE, PipeSize.MEDIUM)
                .setValue(FACING, Direction.NORTH);

        registerDefaultState(state);
    }

    @Override
    public BlockPos findRoot(LevelAccessor pLevel, BlockPos pPos, BlockState state) {
        Direction towardRoot = state.getValue(FACING);
        BlockPos currentPos = pPos.relative(towardRoot);
        while (true) {
            BlockState blockState = pLevel.getBlockState(currentPos);
            if (blockState.getBlock() instanceof GenericExtensionBlock) {
                currentPos = currentPos.relative(towardRoot);
                continue;
            }
            return currentPos;
        }
    }

    @Override
    public boolean isDirectional() {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, SIZE, FACING);

        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction towardBase = state.getValue(FACING);
        BlockState support = level.getBlockState(pos.relative(towardBase));

        return (support.is(this) && support.getValue(SHAPE) == ExtensionShapes.Horizontal.DOUBLE_CONNECTED)
                || support.getBlock() == pipeBlock.get();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {

        Direction towardBase = state.getValue(FACING);

        if (direction == towardBase.getOpposite()) {
            boolean connected = state.getValue(SHAPE) == ExtensionShapes.Horizontal.DOUBLE_CONNECTED;
            boolean shouldConnect = level.getBlockState(pos.relative(towardBase.getOpposite()))
                    .is(this);
            if (!connected && shouldConnect)
                return state.setValue(SHAPE, ExtensionShapes.Horizontal.DOUBLE_CONNECTED);
            if (connected && !shouldConnect)
                return state.setValue(SHAPE, ExtensionShapes.Horizontal.DOUBLE);
        }

        return state.canSurvive(level, pos)
                ? state.setValue(SIZE, level.getBlockState(pos.relative(towardBase)).getValue(SIZE)) // same size as the extension before
                : Blocks.AIR.defaultBlockState();
    }


    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!(level instanceof ServerLevel))
            return InteractionResult.SUCCESS;

        if (state.getValue(SHAPE) == ExtensionShapes.Horizontal.SINGLE)
            return callSuperOnSneakWrenched(state, context);

        Direction outFacing = state.getValue(FACING).getOpposite();

        if (switch (outFacing) { // if the player clicked the "lower" half of the extension, in which case remove the whole block
            case UP, DOWN -> false; // unreachable
            case NORTH -> context.getClickLocation().z > context.getClickedPos().getZ() + .5f;
            case SOUTH -> context.getClickLocation().z < context.getClickedPos().getZ() + .5f;
            case EAST -> context.getClickLocation().x < context.getClickedPos().getX() + .5f;
            case WEST -> context.getClickLocation().x > context.getClickedPos().getX() + .5f;
        })
            return callSuperOnSneakWrenched(state, context);

        level.setBlock(pos,
                state.setValue(SHAPE, ExtensionShapes.Horizontal.SINGLE), 3);

        IWrenchable.playRemoveSound(level, pos);
        return InteractionResult.SUCCESS;
    }
}
