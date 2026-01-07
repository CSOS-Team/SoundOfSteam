package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class SingleExtensionBlock extends GenericExtensionBlock<ExtensionShapes.Single> {

    public static final EnumProperty<ExtensionShapes.Single> SHAPE = EnumProperty.create("shape", ExtensionShapes.Single.class);

    public SingleExtensionBlock(Properties pProperties) {
        super(pProperties, SHAPE);
    }

    @Override
    protected void registerDefaultStateWithSize() {
        BlockState blockState = defaultBlockState()
                .setValue(SHAPE, ExtensionShapes.Single.SINGLE)
                .setValue(SIZE, PipeSize.MEDIUM);
        if (isDirectional())
            blockState.setValue(FACING, Direction.NORTH);
        registerDefaultState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) { // need to call this once SHAPE has been initialised to avoid crash
        pBuilder.add(SHAPE, SIZE);
        if (isDirectional())
            pBuilder.add(FACING);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState below = pLevel.getBlockState(pPos.below());
        return (below.is(this) && below.getValue(SHAPE) == ExtensionShapes.Single.SINGLE_CONNECTED)
                || below.getBlock() == baseBlock.get();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection.getAxis() != Direction.Axis.Y)
            return pState;

        if (pDirection == Direction.UP) {
            boolean connected = pState.getValue(SHAPE) == ExtensionShapes.Single.SINGLE_CONNECTED;
            boolean shouldConnect = pLevel.getBlockState(pPos.above())
                    .is(this);
            if (!connected && shouldConnect)
                return pState.setValue(SHAPE, ExtensionShapes.Single.SINGLE_CONNECTED);
            if (connected && !shouldConnect)
                return pState.setValue(SHAPE, ExtensionShapes.Single.SINGLE);
            return pState;
        }

        return !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState()
                : pState.setValue(SIZE, pLevel.getBlockState(pPos.below())
                .getValue(SIZE));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return callSuperOnSneakWrenched(state, context);
    }
}
