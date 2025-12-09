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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class QuadrupleExtensionBlock extends GenericExtensionBlock<EExtensionShapes.QuadrupleShape> {

    public static final EnumProperty<EExtensionShapes.QuadrupleShape> SHAPE = EnumProperty.create("shape", EExtensionShapes.QuadrupleShape.class);

    public QuadrupleExtensionBlock(Properties pProperties) {
        super(pProperties, SHAPE);
    }

    @Override
    protected void registerDefaultStateWithSize() {
        BlockState blockState = defaultBlockState()
                .setValue(SHAPE, EExtensionShapes.QuadrupleShape.SINGLE)
                .setValue(SIZE, EPipeSizes.PipeSize.MEDIUM);
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
        return (below.is(this) && below.getValue(SHAPE) == EExtensionShapes.QuadrupleShape.QUAD_CONNECTED)
                || below.getBlock() == baseBlock.get();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection.getAxis() != Direction.Axis.Y)
            return pState;

        if (pDirection == Direction.UP) {
            boolean connected = pState.getValue(SHAPE) == EExtensionShapes.QuadrupleShape.QUAD_CONNECTED;
            boolean shouldConnect = pLevel.getBlockState(pPos.above())
                    .is(this);
            if (!connected && shouldConnect)
                return pState.setValue(SHAPE, EExtensionShapes.QuadrupleShape.QUAD_CONNECTED);
            if (connected && !shouldConnect)
                return pState.setValue(SHAPE, EExtensionShapes.QuadrupleShape.QUAD);
            return pState;
        }

        return !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState()
                : pState.setValue(SIZE, pLevel.getBlockState(pPos.below())
                .getValue(SIZE));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!(world instanceof ServerLevel))
            return InteractionResult.SUCCESS;

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .25f || state.getValue(SHAPE) == EExtensionShapes.QuadrupleShape.SINGLE)
            return sneakWrenchedRemove(state, context);

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .5) {
            world.setBlock(pos, state.setValue(SHAPE, EExtensionShapes.QuadrupleShape.SINGLE), 3);
            IWrenchable.playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .75f) {
            world.setBlock(pos, state.setValue(SHAPE, EExtensionShapes.QuadrupleShape.DOUBLE), 3);
            IWrenchable.playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + 1f) {
            world.setBlock(pos, state.setValue(SHAPE, EExtensionShapes.QuadrupleShape.TRIPLE), 3);
            IWrenchable.playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.SUCCESS;
    }
}
