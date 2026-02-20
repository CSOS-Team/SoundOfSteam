package com.finchy.pipeorgans.content.windchest.tremulant;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.content.windchest.WindchestMasterBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.Objects;
import java.lang.Override;

public class TremulantBlock extends Block implements IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED; //powered is just to show if the windchests are active
    public static final BooleanProperty TREM = BooleanProperty.create("trem"); //whether the windchests should be wobbly
    public static final BooleanProperty WINDY = WindchestMasterBlock.WINDY;

    public TremulantBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(TREM, false)
                .setValue(WINDY, false)
        );
    }

    //Adding blockstates to the block itself
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, POWERED, TREM, WINDY);
    }

    //Check to see if the Windchest controller is powered
    public static boolean TisMasterPowered(Level pLevel, BlockPos pPos, Direction pFacing) {
        BlockPos frontPos = pPos.relative(pFacing);
        BlockState frontState = pLevel.getBlockState(frontPos);

        if (frontState.getBlock() instanceof WindchestMasterBlock
                && frontState.hasProperty(BlockStateProperties.POWERED)
                && frontState.getValue(BlockStateProperties.POWERED)
                && frontState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                && frontState.getValue(BlockStateProperties.HORIZONTAL_FACING) == pFacing.getOpposite()) {
            return true;
        }
        return false;
    }

    //Check if the Windchest Controller has wind
    public static boolean TisMasterWindy(Level pLevel, BlockPos pPos, Direction pFacing) {
        BlockPos frontPos = pPos.relative(pFacing);
        BlockState frontState = pLevel.getBlockState(frontPos);

        if (frontState.getBlock() instanceof WindchestMasterBlock
                && frontState.hasProperty(WindchestMasterBlock.WINDY)
                && frontState.getValue(WindchestMasterBlock.WINDY)) {
            return true;
        }
        return false;
    }

    public void updateTrem(Level pLevel, BlockPos pPos, BlockState pState) {
        Direction facing = pState.getValue(FACING);
        BlockPos currentPos = pPos;

        boolean trem = pState.getValue(TREM);
        for (int i = 0; i <= 12; i++) {
            currentPos = currentPos.relative(facing.getOpposite());
            BlockState chestState = pLevel.getBlockState(currentPos);
            if (chestState.getBlock() instanceof WindchestBlock && chestState.hasProperty(TREM)) {

                    pLevel.setBlock(currentPos, chestState.setValue(TREM, trem), 2);
                    continue;
            }
            return;
        }
    }

    // Called when block placed
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {

        Direction facing = pContext.getHorizontalDirection();
        Direction direction = pContext.getPlayer().isShiftKeyDown() ? facing.getOpposite() : facing;
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();;

        BlockState state = Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(FACING, direction)
                .setValue(TREM, level.hasNeighborSignal(clickedPos))
                .setValue(POWERED, TisMasterPowered(level, clickedPos, facing))
                .setValue(WINDY, TisMasterWindy(level, clickedPos, facing));

        updateTrem(level, clickedPos, state);
        return state;

    }

    //Called when wrenched
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return IWrenchable.super.onWrenched(state, context);
    }


    //Whenever neighbour block is updated
//TODO Fix update when fan broken
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {

        Direction facing = state.getValue(FACING);

        boolean trem = level.hasNeighborSignal(pos);

        BlockState newState = state
                .setValue(TREM, trem)
                .setValue(POWERED, TisMasterPowered(level, pos, facing))
                .setValue(WINDY, TisMasterWindy(level, pos, facing));

        if (!newState.equals(state)) {
            level.setBlock(pos, newState, 3);
            updateTrem(level, pos, newState);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) {
            updateTrem(level, pos, state.setValue(TREM, false));
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }


    //Create Rotation Logic
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}