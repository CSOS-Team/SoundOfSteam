package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings({"NullableProblems", "deprecation"})
public abstract class GenericPipeBlock extends Block implements IBE<GenericPipeBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<EPipeSizes.PipeSize> SIZE = EnumProperty.create("size", EPipeSizes.PipeSize.class);

    protected BlockEntry<? extends GenericPipeBlock> baseBlock;
    protected BlockEntry<? extends GenericExtensionBlock<? extends EExtensionShapes.ExtensionShape>> extensionBlock;
    protected BlockEntityEntry<? extends GenericPipeBlockEntity> blockEntityType;

    public final int EPB;

    public GenericPipeBlock(Properties pProperties, int EPB) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, EPipeSizes.PipeSize.MEDIUM));
        this.EPB = EPB;
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WALL, POWERED, SIZE);
    }
    @Override
    public Class<GenericPipeBlockEntity> getBlockEntityClass() {
        return GenericPipeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GenericPipeBlockEntity> getBlockEntityType() {
        return blockEntityType.get();
    }

    @Override
    public abstract VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext);

    public abstract void incrementSize(Level pLevel, BlockPos pos, boolean playSound);

    public static void queuePitchUpdate(LevelAccessor level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof GenericPipeBlock pipe && !level.getBlockTicks()
                .hasScheduledTick(pos, pipe))
            level.scheduleTick(pos, pipe, 1);
    }

    // on right-click
    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //if (pLevel.isClientSide()) { return InteractionResult.PASS; }

        ItemStack heldItem = pPlayer.getItemInHand(pHand); // extending pipe
        if (heldItem.getItem() == baseBlock.get().asItem()) {
            incrementSize(pLevel, pPos, true);
            return InteractionResult.SUCCESS;
        }
        if (heldItem.getItem() instanceof GenericPipeBlockItem) { // swapping pipes
            if (substitutePipe(pState, pLevel, pPos, heldItem, pPlayer) == InteractionResult.SUCCESS) {
                if (!pPlayer.isCreative()) {
                    heldItem.shrink(1);
                    pPlayer.setItemInHand(pHand, heldItem);

                    pPlayer.getInventory().placeItemBackInInventory(new ItemStack(this.baseBlock.get().asItem()));
                }
                return InteractionResult.SUCCESS;
            } else { // FAIL
                AllSoundEvents.DENY.playOnServer(pLevel, pPos);
                pPlayer.displayClientMessage(Component.translatable("pipeorgans.blocks.pipes.replace_pipe_deny"), true);
            }
        }

        return InteractionResult.PASS;
    }

    public boolean validateReplacementSpace(BlockState state, Level level, BlockPos pos, int pitch) {
        if (pitch > 0) { // if there are actually any extensions to place

            // check space (pitch/EPB) above base, rounded up
            int checkDist = (int) Math.ceil(pitch/(float)EPB);
            BlockPos currentPos = pos;
            for (int i=1; i<=checkDist; i++) {
                currentPos = currentPos.above();
                BlockState currentState = level.getBlockState(currentPos);
                if (currentState.canBeReplaced() || (currentState.getBlock() instanceof GenericExtensionBlock))
                    continue;
                return false; // something in the way
            }
        }
        return true;
    }

    public void clearOldExtensions(BlockState state, Level level, BlockPos pos, int pitch) {
        int removeDistance = (int) Math.ceil(pitch/(float)EPB);
        BlockPos currentPos = pos;
        for (int i=1; i<=removeDistance; i++) {
            currentPos = currentPos.above();
            level.destroyBlock(currentPos, false);
        }
    }

    public InteractionResult substitutePipe(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player) {
        GenericPipeBlock held = (GenericPipeBlock) ((GenericPipeBlockItem) heldItem.getItem()).getBlock();
        if (level.getBlockEntity(pos) instanceof GenericPipeBlockEntity be) {

            if (held.validateReplacementSpace(state, level, pos, be.pitch)) { // if the new pipe has space to be placed
                clearOldExtensions(state, level, pos, be.pitch); // remove the extensions that are about to be replaced
                placeNewPipe(state, level, pos, heldItem, player, be.pitch); // place the new pipe
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public static void placeNewPipe(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, int pitch) {
        EPipeSizes.PipeSize size = state.getValue(SIZE);
        Direction facing = state.getValue(FACING);
        boolean wall = state.getValue(WALL);
        boolean powered = state.getValue(POWERED);
        level.destroyBlock(pos, false);

        GenericPipeBlock pipe = (GenericPipeBlock) ((GenericPipeBlockItem) heldItem.getItem()).getBlock();

        level.setBlock(pos, pipe.defaultBlockState()
                .setValue(SIZE, size)
                .setValue(FACING, facing)
                .setValue(WALL, wall)
                .setValue(POWERED, powered), 3);

        GenericPipeBlock newPipe = (GenericPipeBlock) level.getBlockState(pos).getBlock();
        if (pitch > 0) {
            for (int i = 1; i <= pitch; i++) {
                newPipe.incrementSize(level, pos, false);
            }
        }
        if (player instanceof ServerPlayer sp) {
            CriteriaTriggers.PLACED_BLOCK.trigger(sp, pos, heldItem);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        withBlockEntityDo(pLevel, pPos, GenericPipeBlockEntity::updatePitch);
    }

    // check if placed on fluid tank or windchest
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState attachedState = pLevel.getBlockState(pPos.relative(getAttachedDirection(pState)));
        return (FluidTankBlock.isTank(attachedState)
                || attachedState.getBlock() instanceof WindchestBlock);
    }

    // get direction attached from
    public static Direction getAttachedDirection(BlockState state) {
        return state.getValue(WALL) ? state.getValue(FACING) : Direction.DOWN;
    }

    // set blockstates when placing block
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        boolean wall = true;
        if (face.getAxis() == Direction.Axis.Y) { // if placed on floor
            face = context.getHorizontalDirection() // face = the opposite of the player's direction
                    .getOpposite();
            wall = false; // not on wall
        }

        BlockState state = Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, face.getOpposite()) // set facing to the opposite of Direction face
                // (this results in orientation being the same as player's, so
                // model is rotated in blockstate json)
                .setValue(POWERED, level.hasNeighborSignal(clickedPos)) // true if power source adjacent, else false
                .setValue(WALL, wall);
        if (!canSurvive(state, level, clickedPos)) // if placed on fluid tank or windchest
            return null;
        return state;
    }

    // if neighbour updates
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean isMoving) {
        if (pLevel.isClientSide) // only on serverside
            return;
        boolean previouslyPowered = pState.getValue(POWERED);
        if (previouslyPowered != pLevel.hasNeighborSignal(pPos))
            pLevel.setBlock(pPos, pState.cycle(POWERED), 2); // if redstone signal has changed, toggle powered
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        return getAttachedDirection(pState) == pFacing && !pState.canSurvive(pLevel, pCurrentPos)
                ? Blocks.AIR.defaultBlockState()
                : pState;
    }

    // on block placed
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
        if (pOldState.getBlock() != this || pOldState.getValue(SIZE) != pState.getValue(SIZE))
            queuePitchUpdate(pLevel, pPos);
    }

    // on block broken
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    // called when wrenched
    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState.cycle(SIZE);
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
