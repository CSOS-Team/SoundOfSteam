package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.PedalPipeBlock;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class GenericPipeBlock extends Block implements IBE<GenericPipeBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<GenericWhistleProperties.WhistleSize> SIZE = EnumProperty.create("size", GenericWhistleProperties.WhistleSize.class);

    public GenericWhistleProperties.WhistleShape shape;
    public int extensionsPerBlock;

    public RegistryObject<? extends GenericPipeBlock> baseBlock;
    public RegistryObject<? extends GenericExtensionBlock> extensionBlock;
    public RegistryObject<BlockEntityType> blockEntity;

    // declare block and default blockstate
    public GenericPipeBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, GenericWhistleProperties.WhistleSize.MEDIUM));
        shape = GenericWhistleProperties.WhistleShape.GENERIC;
        extensionsPerBlock = 2;
    }

    // custom hitbox
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.getCompleteWhistleShape(
                pState.getValue(SIZE),
                shape,
                pState.getValue(WALL),
                pState.getValue(FACING)
        );
    }

    @Override
    public Class<GenericPipeBlockEntity> getBlockEntityClass() { return GenericPipeBlockEntity.class; }

    @Override
    public BlockEntityType<? extends GenericPipeBlockEntity> getBlockEntityType() { return this.blockEntity.get(); }

    // create block entity at block coords upon block placement
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.blockEntity.get().create(pos, state);
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WALL, POWERED, SIZE);
    }

    // on right-click
    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //if (pLevel.isClientSide()) { return InteractionResult.PASS; }

        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (heldItem.getItem() == this.baseBlock.get().asItem()) {
            incrementSize(pLevel, pPos);
            return InteractionResult.SUCCESS;
        }
        if (heldItem.getItem() instanceof GenericPipeBlockItem) {
            GenericPipeBlock held = (GenericPipeBlock) ((GenericPipeBlockItem) heldItem.getItem()).getBlock();
            if (substitutePipe(pState, pLevel, pPos, held)) {
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

    public boolean substitutePipe(BlockState state, Level level, BlockPos pos, GenericPipeBlock held) {
        GenericPipeBlock base = (GenericPipeBlock) state.getBlock();
        if (level.getBlockEntity(pos) instanceof GenericPipeBlockEntity be) {
            if (held.extensionsPerBlock >= base.extensionsPerBlock) {
                // success
                int removeDist = (int) Math.ceil(be.pitch/(float)base.extensionsPerBlock);
                BlockPos currentPos = pos;
                for (int i=1; i<=removeDist; i++) {
                    currentPos = currentPos.above();
                    level.destroyBlock(currentPos, false);
                }
                placeNewPipe(state, level, pos, held, be.pitch);

            } else {
                if (be.pitch > 0) {

                    // check space (pitch/heldEPB) above base, rounded up
                    int checkDist = (int) Math.ceil(be.pitch/(float)held.extensionsPerBlock);
                    BlockPos currentPos = pos;
                    for (int i=1; i<=checkDist; i++) {
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
                    int removeDist = (int) Math.ceil(be.pitch/(float)base.extensionsPerBlock);
                    currentPos = pos;
                    for (int i=1; i<=removeDist; i++) {
                        currentPos = currentPos.above();
                        level.destroyBlock(currentPos, false);
                    }

                }
                placeNewPipe(state, level, pos, held, be.pitch);
            }
        }
        return true;
    }

    public void incrementSize(LevelAccessor pLevel, BlockPos pPos) {
        incrementSize(pLevel, pPos, true);
    }

    // increase length of whistle
    public void incrementSize(LevelAccessor pLevel, BlockPos pPos, boolean playSound) {
        BlockState base = pLevel.getBlockState(pPos);
        if (!base.hasProperty(SIZE))
            return;

        GenericWhistleProperties.WhistleSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        BlockPos currentPos = pPos.above();
        Direction facing = base.getValue(FACING);

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 6; i++) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock() instanceof GenericExtensionBlock) {
                if (blockState.getValue(GenericExtensionBlock.SHAPE) == GenericWhistleProperties.QuadrupleExtensionShape.DOUBLE) {
                    pLevel.setBlock(currentPos,
                            blockState.setValue(GenericExtensionBlock.SHAPE, GenericWhistleProperties.QuadrupleExtensionShape.QUAD)
                                    .setValue(FACING, facing), 3);

                    if (playSound) {
                        float pPitch = (float) Math.pow(2, -(i * 2) / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }

                    return;
                }
                currentPos = currentPos.above();
                continue;
            }
            if (!blockState.canBeReplaced()) {
                return;
            }

            pLevel.setBlock(currentPos, this.extensionBlock.get().defaultBlockState()
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
        GenericWhistleProperties.WhistleSize size = state.getValue(SIZE);
        Direction facing = state.getValue(FACING);
        boolean wall = state.getValue(WALL);
        boolean powered = state.getValue(POWERED);
        if (pipe instanceof PedalPipeBlock && size == GenericWhistleProperties.WhistleSize.TINY) { size = GenericWhistleProperties.WhistleSize.SMALL; }
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

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

}
