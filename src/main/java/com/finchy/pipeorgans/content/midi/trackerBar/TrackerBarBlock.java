package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.content.midi.MidiSequencerBehaviour;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

@SuppressWarnings("NullableProblems")
public class TrackerBarBlock extends HorizontalKineticBlock implements IBE<TrackerBarBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty TRANSMITTING = KeyboardRelayBlock.TRANSMITTING;

    public TrackerBarBlock(Properties pProperties) {
        super(pProperties
                .isViewBlocking((state, level, pos) -> false));
        registerDefaultState(defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(TRANSMITTING, false)
                .setValue(POWERED, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.TRACKER_BAR.get(state.getValue(HORIZONTAL_FACING));
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, TRANSMITTING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<TrackerBarBlockEntity> getBlockEntityClass() {
        return TrackerBarBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrackerBarBlockEntity> getBlockEntityType() {
        return AllBlockEntities.TRACKER_BAR_BLOCK_ENTITY.get();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof TrackerBarBlockEntity trackerBar) {
            return (int) (trackerBar.getBehaviour(MidiSequencerBehaviour.TYPE).getPlaybackPercentage() * 15);
        }
        return 0;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean isMoving) {
        boolean previouslyPowered = pState.getValue(POWERED);
        boolean neighbourPowered = pLevel.hasNeighborSignal(pPos);
        if (!previouslyPowered && neighbourPowered) {
            if (pLevel.getBlockEntity(pPos) instanceof TrackerBarBlockEntity be) {
                be.pressTogglePlayButton();
            }
            pLevel.setBlock(pPos, pState.setValue(POWERED, true), 3);
        } else if (previouslyPowered && !neighbourPowered) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, false), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pHit.getDirection().equals(pState.getValue(HORIZONTAL_FACING).getClockWise())
                || pHit.getDirection().equals(pState.getValue(HORIZONTAL_FACING).getCounterClockWise()))
            return InteractionResult.PASS; // prevent opening the menu when clicking on either of the shaft faces

        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;
        if (pHand.equals(InteractionHand.OFF_HAND))
            return InteractionResult.SUCCESS;

        withBlockEntityDo(pLevel, pPos, be -> NetworkHooks.openScreen((ServerPlayer) pPlayer, be, be::sendToMenu));
        return InteractionResult.SUCCESS;
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            if (!pLevel.isClientSide) {
                withBlockEntityDo(pLevel, pPos, TrackerBarBlockEntity::onBlockRemoved);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return getRotationAxis(state) == face.getAxis();
    }
}
