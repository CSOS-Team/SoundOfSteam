package com.finchy.pipeorgans.content.traps.tapCymbal;

import com.finchy.pipeorgans.content.pipes.generic.PercussionMode;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TapCymbalBlock extends HorizontalDirectionalBlock
        implements IBE<TapCymbalBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<PercussionMode> MODE = EnumProperty.create("mode", PercussionMode.class);


    public TapCymbalBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(WALL, false)
                .setValue(POWERED, false)
                .setValue(MODE, PercussionMode.TAP)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WALL, POWERED, MODE);
    }

    // direction this block is attached to
    public static Direction getAttachedDirection(BlockState state) {
        return state.getValue(WALL)
                ? state.getValue(FACING)
                : Direction.DOWN;
    }

    // must be attached to tank or windchest
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos attachedPos = pos.relative(getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);

        return FluidTankBlock.isTank(attachedState)
                || attachedState.getBlock() instanceof WindchestBlock;
    }

    // redstone updates
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {

        if (level.isClientSide)
            return;

        boolean hasSignal = level.hasNeighborSignal(pos);
        if (state.getValue(POWERED) != hasSignal) {
            level.setBlock(pos, state.setValue(POWERED, hasSignal), 2);
        }
    }

    // placement logic
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        boolean wall = true;
        if (face.getAxis() == Direction.Axis.Y) {
            face = context.getHorizontalDirection().getOpposite();
            wall = false;
        }

        BlockState state = Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, face.getOpposite())
                .setValue(WALL, wall)
                .setValue(POWERED, level.hasNeighborSignal(pos));

        return canSurvive(state, level, pos) ? state : null;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos,
                        BlockState oldState, boolean isMoving) {
        FluidTankBlock.updateBoilerState(
                state, level, pos.relative(getAttachedDirection(state))
        );
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {
        FluidTankBlock.updateBoilerState(
                state, level, pos.relative(getAttachedDirection(state))
        );
        super.onRemove(state, level, pos, newState, isMoving);
    }


    //when wrenched
    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState.cycle(MODE);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE
                ? state
                : state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public Class<TapCymbalBlockEntity> getBlockEntityClass() {
        return TapCymbalBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TapCymbalBlockEntity> getBlockEntityType() {
        return AllBlockEntities.TAP_CYMBAL_BLOCK_ENTITY.get();
    }
}
