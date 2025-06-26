package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.WindchestBlock;
import com.finchy.pipeorgans.init.AllShapes;
import com.finchy.pipeorgans.item.GenericPipeBlockItem;
import com.simibubi.create.AllSoundEvents;

import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NullableProblems")
public class GenericPipeBlock extends Block implements IBE<GenericPipeBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = WhistleBlock.WALL;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Generic.WhistleSize> SIZE = EnumProperty.create("size", Generic.WhistleSize.class);

    public int extensionsPerBlock;

    public DeferredHolder<Block, ? extends GenericPipeBlock> baseBlock;
    public DeferredHolder<Block, ? extends GenericExtensionBlock> extensionBlock;
    public Holder<BlockEntityType<?>> blockEntity;

    // declare block and default blockstate
    public GenericPipeBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, Generic.WhistleSize.MEDIUM));
        extensionsPerBlock = 2;
    }

    // custom hitbox
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape whistle = AllShapes.getGenericBase(pState.getValue(SIZE));
        return Shapes.or(whistle,
                !pState.getValue(WALL) ?
                        AllShapes.BASE_FLOOR : AllShapes.getBase(pState.getValue(FACING)));
        // if block is not on wall, add BASE_FLOOR, else add correct wall base for direction
    }

    @Override
    public Class<GenericPipeBlockEntity> getBlockEntityClass() { return GenericPipeBlockEntity.class; }

    @Override
    public BlockEntityType<? extends GenericPipeBlockEntity> getBlockEntityType() {return (BlockEntityType<? extends GenericPipeBlockEntity>)blockEntity.value(); }

    // create block entity at block coords upon block placement
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntity.value().create(pos, state);
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WALL, POWERED, SIZE);
    }

    // on right-click
    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.getItem() == baseBlock.get().asItem()) {
                incrementSize(level, pos);
                return ItemInteractionResult.SUCCESS;
            }

        if (heldItem.getItem() instanceof GenericPipeBlockItem) {
            GenericPipeBlock held = (GenericPipeBlock) ((GenericPipeBlockItem) heldItem.getItem()).getBlock();
            if (substitutePipe(state, level, pos, held)) {
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                    player.setItemInHand(hand, heldItem);

                    player.getInventory().placeItemBackInInventory(new ItemStack(baseBlock.get().asItem()));
                }
                return ItemInteractionResult.SUCCESS;
            } else { // FAIL
                AllSoundEvents.DENY.playOnServer(level, pos);
                player.displayClientMessage(Component.translatable("pipeorgans.blocks.pipes.replace_pipe_deny"), true);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public boolean substitutePipe(BlockState state, Level level, BlockPos pos, GenericPipeBlock held) {
        GenericPipeBlock base = (GenericPipeBlock) state.getBlock();
        if (level.getBlockEntity(pos) instanceof GenericPipeBlockEntity be) {
            if (held.extensionsPerBlock >= base.extensionsPerBlock) { // if new pipe is smaller
                // success
                int removeDist = (int) Math.ceil(be.pitch / (float) base.extensionsPerBlock);
                BlockPos currentPos = pos;
                for (int i = 1; i <= removeDist; i++) {
                    currentPos = currentPos.above();
                    level.destroyBlock(currentPos, false);
                }
                placeNewPipe(state, level, pos, held, be.pitch);

            } else { // if new pipe is larger
                if (be.pitch > 0) { // if there are actually any extensions to replace

                    // check space (pitch/heldEPB) above base, rounded up
                    int checkDist = (int) Math.ceil(be.pitch / (float) held.extensionsPerBlock);
                    BlockPos currentPos = pos;
                    for (int i = 1; i <= checkDist; i++) {
                        currentPos = currentPos.above();
                        BlockState currentState = level.getBlockState(currentPos);
                        if (currentState.canBeReplaced() || (
                                currentState.getBlock() instanceof GenericExtensionBlock
                        )) {
                            continue;
                        }
                        return false; // something in the way
                    }
                    // success
                    int removeDist = (int) Math.ceil(be.pitch / (float) base.extensionsPerBlock);
                    currentPos = pos;
                    for (int i = 1; i <= removeDist; i++) {
                        currentPos = currentPos.above();
                        level.destroyBlock(currentPos, false);
                    }

                }
                placeNewPipe(state, level, pos, held, be.pitch);
            }
        }
        return true;
    }

    public void incrementSize(LevelAccessor level, BlockPos pos) {
        incrementSize(level, pos, true);
    }

    // increase length of whistle
    public void incrementSize(LevelAccessor pLevel, BlockPos pPos, boolean playSound) {
        BlockState base = pLevel.getBlockState(pPos);
        if (!base.hasProperty(SIZE))
            return;

        Generic.WhistleSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        BlockPos currentPos = pPos.above();
        Direction facing = base.getValue(FACING);

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.value();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 6; i++) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock() instanceof GenericExtensionBlock) {
                if (blockState.getValue(GenericExtensionBlock.SHAPE) == Generic.QuadrupleExtensionShape.DOUBLE) {
                    pLevel.setBlock(currentPos,
                            blockState
                                    .setValue(GenericExtensionBlock.SHAPE, Generic.QuadrupleExtensionShape.QUAD)
                                    .setValue(FACING, facing), 3);

                    if (playSound ) {
                        float pPitch = (float) Math.pow(2, -(i * 2) / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }

                    return;
                }
                currentPos = currentPos.above();
                continue;
            }
            if (!blockState.canBeReplaced())
                return;

            pLevel.setBlock(currentPos, extensionBlock.get().defaultBlockState()
                    .setValue(SIZE, size)
                    .setValue(FACING, facing), 3);

            if (playSound) {
                float pPitch = (float) Math.pow(2, -(i * 2 - 1) / 12.0);
                pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
            }
            return;
        }
    }

    public void placeNewPipe(BlockState state, Level level, BlockPos pos, GenericPipeBlock pipe, int pitch) {
        Generic.WhistleSize size = state.getValue(SIZE);
        Direction facing = state.getValue(FACING);
        boolean wall = state.getValue(WALL);
        boolean powered = state.getValue(POWERED);
        if (pipe instanceof PedalPipeBlock && size == Generic.WhistleSize.TINY) { size = Generic.WhistleSize.SMALL; }
        level.destroyBlock(pos, false);

        level.setBlock(pos, pipe.defaultBlockState()
                .setValue(SIZE, size)
                .setValue(FACING, facing)
                .setValue(WALL, wall)
                .setValue(POWERED, powered), 3);

        GenericPipeBlock newPipe = (GenericPipeBlock) level.getBlockState(pos).getBlock();
        if (pitch > 0) {
            for (int i = 1; i <= pitch; i++) {
                newPipe.incrementSize(level, pos);
            }
        }
    }

    public static void queuePitchUpdate(LevelAccessor level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof GenericPipeBlock whistle && !level.getBlockTicks()
                .hasScheduledTick(pos, whistle))
            level.scheduleTick(pos, whistle, 1);
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

    // called when wrenched
    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState.cycle(SIZE);
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

        BlockState state = super.getStateForPlacement(context)
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

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

}
