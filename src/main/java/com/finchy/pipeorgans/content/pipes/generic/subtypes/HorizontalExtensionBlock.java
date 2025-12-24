package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.EExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class HorizontalExtensionBlock
        extends GenericExtensionBlock<EExtensionShapes.HorizontalShape> {

    public static final EnumProperty<EExtensionShapes.HorizontalShape> SHAPE =
            EnumProperty.create("shape", EExtensionShapes.HorizontalShape.class);

    public HorizontalExtensionBlock(Properties properties) {
        super(properties, SHAPE);
    }

    @Override
    protected void registerDefaultStateWithSize() {
        BlockState state = defaultBlockState()
                .setValue(SHAPE, EExtensionShapes.HorizontalShape.SINGLE)
                .setValue(SIZE, EPipeSizes.PipeSize.MEDIUM);

        if (isDirectional())
            state = state.setValue(FACING, Direction.NORTH);

        registerDefaultState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, SIZE);
        if (isDirectional())
            builder.add(FACING);

        super.createBlockStateDefinition(builder);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!isDirectional())
            return true;

        Direction towardBase = state.getValue(FACING);
        BlockState support = level.getBlockState(pos.relative(towardBase));

        return (support.is(this)
                && support.getValue(SHAPE) == EExtensionShapes.HorizontalShape.DOUBLE_CONNECTED)
                || support.getBlock() == baseBlock.get();
    }


    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (!isDirectional())
            return state;

        Direction towardBase = state.getValue(FACING);

        // Only react to changes toward the base
        if (direction != towardBase)
            return state;

        boolean connected =
                state.getValue(SHAPE) == EExtensionShapes.HorizontalShape.DOUBLE_CONNECTED;

        boolean shouldConnect =
                level.getBlockState(pos.relative(towardBase)).is(this);

        if (!connected && shouldConnect)
            return state.setValue(SHAPE, EExtensionShapes.HorizontalShape.DOUBLE_CONNECTED);

        if (connected && !shouldConnect)
            return state.setValue(SHAPE, EExtensionShapes.HorizontalShape.DOUBLE);

        return state.canSurvive(level, pos)
                ? state
                : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }


    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!(level instanceof ServerLevel))
            return InteractionResult.SUCCESS;

        if (state.getValue(SHAPE) == EExtensionShapes.HorizontalShape.SINGLE)
            return callSuperOnSneakWrenched(state, context);

        if (context.getClickLocation().x < pos.getX() + 0.5f)
            return callSuperOnSneakWrenched(state, context);

        level.setBlock(pos,
                state.setValue(SHAPE, EExtensionShapes.HorizontalShape.SINGLE), 3);

        IWrenchable.playRemoveSound(level, pos);
        return InteractionResult.SUCCESS;
    }
}
