package com.finchy.pipeorgans.content.windchest.tremulant;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.content.windchest.WindchestMasterBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class TremulantBlock extends Block implements IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public TremulantBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        Direction facing = context.getHorizontalDirection();
        Direction direction = context.getPlayer() != null && context.getPlayer().isShiftKeyDown()
                ? facing.getOpposite()
                : facing;

        boolean powered = level.hasNeighborSignal(clickedPos);

        return super.getStateForPlacement(context)
                .setValue(FACING, direction)
                .setValue(POWERED, powered);
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block neighborBlock,
            BlockPos neighborPos,
            boolean isMoving
    ) {
        if (level.isClientSide)
            return;

        boolean powered = level.hasNeighborSignal(pos);
        boolean wasPowered = state.getValue(POWERED);

        if (powered != wasPowered) {
            level.setBlock(pos, state.setValue(POWERED, powered), 2);
            updateWindchests(level, pos, powered);
        }
    }

    @Override
    public void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        if (level.isClientSide)
            return;

        updateWindchests(level, pos, state.getValue(POWERED));
    }

    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {
        if (level.isClientSide)
            return;

        updateWindchests(level, pos, false);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private void updateWindchests(Level level, BlockPos pos, boolean trem) {
        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.relative(dir);
            BlockState targetState = level.getBlockState(targetPos);

            if (targetState.getBlock() instanceof WindchestMasterBlock) {
                setTremRecursive(level, targetPos, targetState, trem);
            }
        }
    }

    private void setTremRecursive(Level level, BlockPos masterPos, BlockState masterState, boolean trem) {
        Direction facing = masterState.getValue(WindchestMasterBlock.FACING);

        // Update master
        level.setBlock(masterPos, masterState.setValue(WindchestMasterBlock.TREM, trem), 3);

        // Update slaves
        BlockPos currentPos = masterPos;
        for (int i = 0; i <= 12; i++) {
            currentPos = currentPos.relative(facing);
            BlockState state = level.getBlockState(currentPos);

            if (state.getBlock() instanceof WindchestBlock
                    && state.getValue(FACING) == facing.getOpposite()) {

                level.setBlock(currentPos, state.setValue(WindchestBlock.TREM, trem), 3);
            } else {
                return;
            }
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
