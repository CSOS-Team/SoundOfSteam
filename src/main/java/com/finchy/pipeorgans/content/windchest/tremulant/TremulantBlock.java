package com.finchy.pipeorgans.content.windchest.tremulant;

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
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty TREM = BooleanProperty.create("trem");
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
                && frontState.getValue(BlockStateProperties.POWERED)) {
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

    // Called when block placed
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {

        Direction facing = pContext.getHorizontalDirection();
        Direction direction = pContext.getPlayer().isShiftKeyDown() ? facing.getOpposite() : facing;
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();

        return Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(FACING, direction)
                .setValue(TREM, level.hasNeighborSignal(clickedPos))
                .setValue(POWERED, TisMasterPowered(level, clickedPos, facing))
                .setValue(WINDY, TisMasterWindy(level, clickedPos, facing));
    }

    //Called when wrenched
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return IWrenchable.super.onWrenched(state, context);
    }


    //Whenever neighbour block is updated
//TODO Fix update when fan broken
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {

        Direction facing = pState.getValue(FACING);
        if (pPos.relative(facing).equals(pNeighborPos) ) {
            pLevel.setBlock(pPos, pState
                    .setValue(POWERED, TisMasterPowered(pLevel,pPos, facing))
                    .setValue(WINDY, TisMasterWindy(pLevel, pPos, facing)), 3);
        }
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