package com.finchy.pipeorgans.content.traps.crashCymbal;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


import static com.finchy.pipeorgans.content.base.BaseBlock.getAttachedDirection;


public class CrashCymbalBlock extends Block implements IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CrashCymbalBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(WALL, false)
                .setValue(POWERED, false)
                );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WALL, POWERED);
    }


// check if placed on fluid tanks or windchest
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState attachedState = pLevel.getBlockState(pPos.relative(getAttachedDirection(pState)));
        return (FluidTankBlock.isTank(attachedState)
                || attachedState.getBlock() instanceof WindchestBlock);
    }

    // get direction attached from
    public static Direction getAttachedDirection(BlockState state) {
        return state.getValue(WALL) ? state.getValue(FACING) : Direction.DOWN;
    }

    // if neighbour updates
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide) // only on serverside
            return;
        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != worldIn.hasNeighborSignal(pos))
            worldIn.setBlock(pos, state.cycle(POWERED), 2); // if redstone signal has changed, toggle powered
    }

    // set blockstates when placing block
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        boolean wall = true;
        if (face.getAxis() == Direction.Axis.Y) { // if placed on floor
            face = context.getHorizontalDirection() // face = the opposite of the player's direction
                    .getOpposite();
            wall = false; // not on wall
        }

        BlockState baseState = Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, face.getOpposite()) // set facing to the opposite of Direction face
                // (this results in orientation being the same as player's, so
                // model is rotated in blockstate json)
                .setValue(POWERED, level.hasNeighborSignal(clickedPos))
                .setValue(WALL, wall);

        if (!canSurvive(baseState, level, clickedPos))
            return null;

        return baseState;
    }

    // on block placed
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
    }

    // on block broken
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    // called when wrenched

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}
