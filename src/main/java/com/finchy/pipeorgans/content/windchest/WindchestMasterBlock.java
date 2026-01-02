package com.finchy.pipeorgans.content.windchest;

import com.finchy.pipeorgans.content.windchest.tremulant.TremulantBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.createmod.catnip.data.Iterate;
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
import org.jetbrains.annotations.Nullable;

public class WindchestMasterBlock extends Block implements IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WINDY = BooleanProperty.create("windy");
    public static final BooleanProperty TREM = BooleanProperty.create("trem");

    public WindchestMasterBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WINDY, false)
                .setValue(TREM, false)
        );
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, POWERED, WINDY, TREM);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        //Direction face = pContext.getClickedFace();
        Direction facing = pContext.getHorizontalDirection();

        //if (face.getAxis() == Direction.Axis.Y) { face = pContext.getHorizontalDirection().getOpposite(); }

        return super.getStateForPlacement(pContext)
                .setValue(FACING, pContext.getPlayer().isShiftKeyDown() ? facing.getOpposite() : facing)
                .setValue(POWERED, level.hasNeighborSignal(clickedPos))
                .setValue(TREM, false);
    }

    public void updateSlaves(BlockState state, Level level, BlockPos pos, boolean powered) {

        Direction facing = state.getValue(FACING);
        BlockPos currentPos = pos;
        for (int i = 0; i <= 12; i++) {
            currentPos = currentPos.relative(facing);
            BlockState currentBlock = level.getBlockState(currentPos);

            // Windchest: make the 13 block chain
            if (currentBlock.getBlock() instanceof WindchestBlock
                    && currentBlock.getValue(FACING) == facing.getOpposite()) {

                level.setBlock(currentPos, currentBlock.setValue(POWERED, powered), 2);
                continue;
            }
            // Tremulant: only one block
            if (currentBlock.getBlock() instanceof TremulantBlock
                    && currentBlock.getValue(FACING) == facing.getOpposite()) {

                level.setBlock(currentPos, currentBlock.setValue(POWERED, powered), 2);
                return;
            }
            return;
        }
    }

    // if neighbour updates
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean isMoving) {
        if (pLevel.isClientSide) // only on serverside
            return;
        if (pPos.relative(pState.getValue(FACING)).equals(pNeighborPos)) { return; } // suppress updates from where windchests would be

        boolean previouslyPowered = pState.getValue(POWERED);
        boolean powered = false;
        Direction facing = pState.getValue(FACING);
        for (Direction i : Direction.values()) {
            if (i == facing) { continue; }
            if (pLevel.getSignal(pPos.relative(i), i)>0) { powered = true; break; }
        }
        //boolean powered = pLevel.hasNeighborSignal(pPos);
        if (previouslyPowered != powered) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, powered), 2);
            updateSlaves(pState, pLevel, pPos, powered);
        }
        if (pNeighborBlock instanceof EncasedFanBlock) { updateMasterWindy(pLevel, pPos); }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pLevel.isClientSide) // only on serverside
            return;
        updateSlaves(pState, pLevel, pPos, pLevel.hasNeighborSignal(pPos));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.isClientSide) // only on serverside
            return;

        updateSlaves(pState, pLevel, pPos, false);
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    public static boolean updateMasterWindy(Level level, BlockPos masterPos) {
        if (level.isClientSide) { return false; }
        int activeFans = 0;
        for (Direction d : Iterate.directions) {
            if (level.getBlockEntity(masterPos.relative(d)) instanceof EncasedFanBlockEntity fanBE) {
                BlockState fanState = fanBE.getBlockState();
                if (fanState.getValue(EncasedFanBlock.FACING) == d.getOpposite() && (fanBE.getSpeed()*d.getAxisDirection().getStep() < 0)) {
                    activeFans++;
                }
            }
        }
        level.setBlock(masterPos, level.getBlockState(masterPos).setValue(WINDY, activeFans>0), 2);
        return activeFans > 0;
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
