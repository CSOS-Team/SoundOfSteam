package com.finchy.pipeorgans.block;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.Objects;

import static com.finchy.pipeorgans.block.WindchestMasterBlock.WINDY;

public class WindchestBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public WindchestBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false));
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, POWERED);
    }

    public boolean isMasterWindy(Level level, Direction facing, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, facing, pos);
        if (masterPos != pos) { return level.getBlockState(masterPos).getValue(WINDY); }
        return false;
    }

    public boolean isMasterPowered(Level level, Direction facing, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, facing, pos);
        if (masterPos != pos) { return level.getBlockState(masterPos).getValue(POWERED); }
        return false;
    }

    public boolean isMasterActive(Level level, Direction facing, )

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
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction face = pContext.getClickedFace().getOpposite();
        BlockPos parentPos = clickedPos.relative(face.getOpposite());

        if (level.getBlockState(parentPos).getBlock() instanceof WindchestBlock) { // if placed on windchest block
            face = level.getBlockState(parentPos).getValue(FACING);

        } else if (level.getBlockState(parentPos).getBlock() instanceof WindchestMasterBlock) { // if placed on windchest master
            face = level.getBlockState(parentPos).getValue(FACING).getOpposite();
        } else if (face.getAxis() == Direction.Axis.Y) { face = pContext.getHorizontalDirection(); }

        return Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(FACING, face)
                .setValue(POWERED, isMasterPowered(level, face, clickedPos));

    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {

        Direction facing = pState.getValue(FACING);
        if (pPos.relative(facing).equals(pNeighborPos) ) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, isMasterPowered(pLevel, facing, pPos)), 3);
        }
    }
}
