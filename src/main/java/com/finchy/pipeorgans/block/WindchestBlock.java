package com.finchy.pipeorgans.block;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;

import static com.finchy.pipeorgans.block.WindchestMasterBlock.WINDY;

public class WindchestBlock extends Block implements IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public WindchestBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, POWERED);
    }

    public boolean isMasterWindy(Level level, Direction facing, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, facing, pos);
        if (masterPos != pos) {
            return level.getBlockState(masterPos).getValue(WINDY);
        }
        return false;
    }

    public boolean isMasterPowered(Level level, Direction facing, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, facing, pos);
        if (masterPos != pos) {
            return level.getBlockState(masterPos).getValue(POWERED);
        }
        return false;
    }

    public boolean isMasterActive(Level level, Direction facing, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, facing, pos);
        BlockState masterState = level.getBlockState(masterPos);
        if (masterPos != pos) {
            return masterState.getValue(POWERED) && masterState.getValue(WINDY);
        }
        return false;

    }

    public BlockPos getMasterPos(Level level, Direction facing, BlockPos pos) {
        BlockPos currentPos = pos;
        for (int i=0; i<=12; i++) {
            currentPos = currentPos.relative(facing);
            BlockState currentBlock = level.getBlockState(currentPos);
            if (currentBlock.getBlock() instanceof WindchestMasterBlock && currentBlock.getValue(FACING) == facing.getOpposite()) {
                return currentPos;
            }
            if ( !(currentBlock.getBlock() instanceof WindchestBlock
                    && (currentBlock.getValue(FACING) == facing)) ) {
                break;
            }
        }
        return pos;
    }

    @Override

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = IWrenchable.super.onWrenched(state, context);

        // check whether block should be powered and adjust accordingly
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        state = level.getBlockState(clickedPos);
        boolean shouldPower = isMasterPowered(level, state.getValue(FACING), clickedPos);

        level.setBlock(clickedPos, state.setValue(POWERED, shouldPower), 3);

        return result;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction facing = pContext.getHorizontalDirection();
        Direction direction = pContext.getPlayer().isShiftKeyDown() ? facing.getOpposite() : facing;
        
        return Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(FACING, direction)
                .setValue(POWERED, isMasterPowered(level, direction, clickedPos));

    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {

        Direction facing = pState.getValue(FACING);
        if (pPos.relative(facing).equals(pNeighborPos) ) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, isMasterPowered(pLevel, facing, pPos)), 3);
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}
