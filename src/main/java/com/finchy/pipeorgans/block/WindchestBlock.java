package com.finchy.pipeorgans.block;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
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
        if (masterPos != pos) { return level.getBlockState(getMasterPos(level, facing, pos)).getValue(WINDY); }
        return false;
    }

    public boolean isMasterPowered(Level level, Direction facing, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, facing, pos);
        PipeOrgans.LOGGER.info(masterPos.toShortString());
        if (masterPos != pos) { return level.getBlockState(getMasterPos(level, facing, pos)).getValue(POWERED); }
        PipeOrgans.LOGGER.info("isMasterPowered: masterPos == pos");
        return false;
    }

    public BlockPos getMasterPos(Level level, Direction facing, BlockPos pos) {
        BlockPos currentPos = pos;
        for (int i=0; i<=12; i++) {
            BlockState currentBlock = level.getBlockState(currentPos.relative(facing));
            if (currentBlock.getBlock() instanceof WindchestMasterBlock && currentBlock.getValue(FACING) == facing.getOpposite()) {
                return currentPos.relative(facing);
            }
            if (!(currentBlock.getBlock() instanceof WindchestBlock && currentBlock.getValue(FACING) == facing.getOpposite())) {
                break;
            }
            currentPos = currentPos.relative(facing);
        }
        return pos;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        PipeOrgans.LOGGER.info("neighbour changed");
        if (pLevel.isClientSide) {
            return;
        }
        Direction facing = pState.getValue(FACING);
        boolean previouslyPowered = pState.getValue(POWERED);
        if (pPos.relative(facing) == pNeighborPos       // if update is in direction of master
                && (pNeighborBlock instanceof WindchestMasterBlock       // and block is windchest master or windchest
                    || pNeighborBlock instanceof WindchestBlock)
                && previouslyPowered != isMasterPowered(pLevel, facing, pPos)) {
            pLevel.setBlock(pPos, pState.cycle(POWERED), 2);
        }
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
}
