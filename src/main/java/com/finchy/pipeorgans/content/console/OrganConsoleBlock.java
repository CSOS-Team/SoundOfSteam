package com.finchy.pipeorgans.content.console;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class OrganConsoleBlock extends Block implements IBE<OrganConsoleBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty PEDALBOARD = BooleanProperty.create("pedalboard");
    public static final IntegerProperty MANUAL_COUNT = IntegerProperty.create("manual_count", 1, 4);

    public OrganConsoleBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(RIGHT, false)
                .setValue(PEDALBOARD, false)
                .setValue(MANUAL_COUNT, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, HALF, RIGHT, PEDALBOARD, MANUAL_COUNT);
    }

    // Multiblock geometry helpers
    public static Direction rightDir(Direction facing) {
        return facing.getCounterClockWise();
    }

    public static boolean isMaster(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !state.getValue(RIGHT);
    }

    public static BlockPos getMasterPos(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos result = pos;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
            result = result.below();
        if (state.getValue(RIGHT))
            result = result.relative(rightDir(facing).getOpposite());
        return result;
    }

    public static BlockPos[] partPositions(BlockPos master, Direction facing) {
        Direction right = rightDir(facing);
        return new BlockPos[] {
                master,                              // lower-left
                master.relative(right),              // lower-right
                master.above(),                      // upper-left
                master.above().relative(right)       // upper-right
        };
    }

    @Nullable
    public OrganConsoleBlockEntity getMasterBE(Level level, BlockState state, BlockPos pos) {
        BlockPos masterPos = getMasterPos(state, pos);
        BlockEntity be = level.getBlockEntity(masterPos);
        return be instanceof OrganConsoleBlockEntity console ? console : null;
    }

    // Block entity wiring
    @Override
    public Class<OrganConsoleBlockEntity> getBlockEntityClass() {
        return OrganConsoleBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends OrganConsoleBlockEntity> getBlockEntityType() {
        return AllBlockEntities.ORGAN_CONSOLE_BLOCK_ENTITY.get();
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (!isMaster(state))
            return null;
        return getBlockEntityType().create(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return AllShapes.organConsoleShape(
                state.getValue(HALF) == DoubleBlockHalf.UPPER,
                state.getValue(PEDALBOARD),
                state.getValue(MANUAL_COUNT),
                state.getValue(FACING),
                world,
                pos);
    }

    // Placement
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos master = context.getClickedPos();
        Direction right = rightDir(facing);

        Level level = context.getLevel();
        BlockPos[] toCheck = new BlockPos[] {
                master.relative(right),
                master.above(),
                master.above().relative(right)
        };
        for (BlockPos p : toCheck) {
            if (p.getY() > level.getMaxBuildHeight() - 1)
                return null;
            if (!level.getBlockState(p).canBeReplaced(context))
                return null;
        }

        return Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, facing)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(RIGHT, false);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide)
            return;
        Direction facing = state.getValue(FACING);
        Direction right = rightDir(facing);

        // Ensure PEDALBOARD starts false and MANUAL_COUNT starts 1 on all parts
        BlockState base = state.setValue(PEDALBOARD, false).setValue(MANUAL_COUNT, 1);
        level.setBlock(pos.relative(right),
                base.setValue(HALF, DoubleBlockHalf.LOWER).setValue(RIGHT, true), Block.UPDATE_ALL);
        level.setBlock(pos.above(),
                base.setValue(HALF, DoubleBlockHalf.UPPER).setValue(RIGHT, false), Block.UPDATE_ALL);
        level.setBlock(pos.above().relative(right),
                base.setValue(HALF, DoubleBlockHalf.UPPER).setValue(RIGHT, true), Block.UPDATE_ALL);
    }

    // Removing any part removes the whole structure
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockPos master = getMasterPos(state, pos);
            for (BlockPos part : partPositions(master, state.getValue(FACING))) {
                if (part.equals(pos))
                    continue;
                BlockState partState = level.getBlockState(part);
                if (partState.getBlock() instanceof OrganConsoleBlock)
                    level.removeBlock(part, false); // no drops; the mined part drops the single item
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    // Wrench configure manuals / pedalboard
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        OrganConsoleBlockEntity be = getMasterBE(level, state, pos);
        if (be == null)
            return InteractionResult.PASS;

        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            be.cycleManualCount();
            // Propagate manual count value to all 4 parts
            int manualCount = be.getManualCount();
            BlockPos master = getMasterPos(state, pos);
            BlockState masterState = level.getBlockState(master);
            Direction facing = masterState.getValue(FACING);
            for (BlockPos part : partPositions(master, facing)) {
                BlockState partState = level.getBlockState(part);
                if (partState.getBlock() instanceof OrganConsoleBlock) {
                    level.setBlock(part, partState.setValue(MANUAL_COUNT, manualCount), Block.UPDATE_CLIENTS);
                }
            }
        } else {
            be.togglePedalboard();
            // Propagate pedalboard val to all 4 parts
            boolean pedalboard = be.hasPedalboard();
            BlockPos master = getMasterPos(state, pos);
            BlockState masterState = level.getBlockState(master);
            Direction facing = masterState.getValue(FACING);
            for (BlockPos part : partPositions(master, facing)) {
                BlockState partState = level.getBlockState(part);
                if (partState.getBlock() instanceof OrganConsoleBlock) {
                    level.setBlock(part, partState.setValue(PEDALBOARD, pedalboard), Block.UPDATE_CLIENTS);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    // Right-click: open the appropriate GUI
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;

        // Let the wrench fall through to onWrenched or onSneakWrenched.
        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() instanceof WrenchItem)
            return InteractionResult.PASS;

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        OrganConsoleBlockEntity be = getMasterBE(level, state, pos);
        if (be == null)
            return InteractionResult.PASS;

        boolean pedalboardMode = state.getValue(HALF) == DoubleBlockHalf.LOWER;
        if (pedalboardMode && !be.hasPedalboard())
            return InteractionResult.SUCCESS; // nothing to open on the bottom until a pedalboard is added

        be.openMenu((ServerPlayer) player, pedalboardMode);
        return InteractionResult.SUCCESS;
    }

    // Rotation / mirroring
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
