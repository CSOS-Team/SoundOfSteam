package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.compat.ModCompat;
import com.finchy.pipeorgans.compat.create_connected.CreateConnectedCompat;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.init.AllTriggers;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"NullableProblems", "deprecation"})
public abstract class GenericPipeBlock extends Block implements PipeBehaviour, IBE<GenericPipeBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<PipeSize> SIZE = EnumProperty.create("size", PipeSize.class);

    protected final BlockEntry<? extends GenericExtensionBlock<?>> extensionBlock;
    protected final BlockEntityEntry<? extends GenericPipeBlockEntity> blockEntityType;

    protected final PipeDirection pipeDirection;
    protected final SoundEvent growSound;

    protected final TriFunction<PipeSize, Boolean, Direction, VoxelShape> voxelShapeGetter;
    // WHY IS A TRIFUNCTION A THING???

    //cuz tuv(r)

    public GenericPipeBlock(Properties pProperties, PipeDirection pipeDirection,
                            PipeMaterial pipeMaterial,
                            BlockEntry<? extends GenericExtensionBlock<?>> extensionBlock,
                            BlockEntityEntry<? extends GenericPipeBlockEntity> blockEntityType,
                            TriFunction<PipeSize, Boolean, Direction, VoxelShape> voxelShapeGetter) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, PipeSize.MEDIUM))
        ;

        this.extensionBlock = extensionBlock;
        this.blockEntityType = blockEntityType;

        this.pipeDirection = pipeDirection;
        this.growSound = pipeMaterial.getGrowSound();

        this.voxelShapeGetter = voxelShapeGetter;
    }

    public boolean isHorizontal() {
        return pipeDirection.equals(PipeDirection.HORIZONTAL);
    }

    @Override
    public Direction getExtensionDirection(BlockState pipeState) {
        return pipeDirection.getExtensionDirection(pipeState);
    }

    @Override
    public Direction getPipeDirectionFromExtension(BlockState extensionState) {
        return pipeDirection.getPipeDirectionFromExtension(extensionState);
    }

    @Override
    public double getExtensionClickPosition(BlockPos extensionPos, Vec3 clickLocation, Direction facing) {
        return pipeDirection.getExtensionClickPosition(extensionPos, clickLocation, facing);
    }

    @Override
    public GenericExtensionBlock<?> getExtensionBlock() {
        return extensionBlock.get();
    }

    @Override
    // get direction attached from
    public Direction getAttachedDirection(BlockState state) {
        return state.getValue(WALL) ? state.getValue(FACING) : Direction.DOWN;
    }

    @Override
    public void incrementSize(Level pLevel, BlockPos pos, boolean playSound) {
        BlockState base = pLevel.getBlockState(pos);
        if (!base.hasProperty(SIZE))
            return;

        PipeSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        Direction iterateDirection = getExtensionDirection(base); // the direction along which to iterate
        BlockPos currentPos = pos.relative(iterateDirection);
        Direction facing = base.getValue(FACING);

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = getGrowSound();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 12; i+=extensionsPerBlock()) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock().equals(getExtensionBlock())) { // if block is this pipe's extension block

                ExtensionShapes.IExtensionShape<?> shape = blockState.getValue(getExtensionBlock().SHAPE);
                if (!shape.isFullBlockLong()) { // if another extension can be added without placing a new block

                    BlockState toSet = blockState.cycle(getExtensionBlock().SHAPE); // cycle to the next shape
                    if (getExtensionBlock().isDirectional())         // only set direction if the extension is directional
                        toSet = toSet.setValue(FACING, facing);    // (would cause a crash otherwise)
                    pLevel.setBlock(currentPos, toSet, 3);

                    if (playSound) {
                        i += shape.extensionNumber();
                        float pPitch = (float) Math.pow(2, -i / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }
                    return;
                }
                currentPos = currentPos.relative(iterateDirection);
                continue;
            }
            if (!blockState.canBeReplaced()) {
                return;
            }

            BlockState toSet = getExtensionBlock().defaultBlockState().setValue(SIZE, size);
            if (getExtensionBlock().isDirectional())      // only set direction if the extension is directional
                toSet = toSet.setValue(FACING, facing);    // (would cause a crash otherwise)
            pLevel.setBlock(currentPos, toSet, 3);

            if (playSound) {
                float pPitch = (float) Math.pow(2, -i / 12.0);
                pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
            }
            return;
        }
    }

    @Override
    public void placeNewPipe(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, int pitch) {
        PipeSize size = state.getValue(SIZE);
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

    public static void queuePitchUpdate(LevelAccessor level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof GenericPipeBlock pipe && !level.getBlockTicks()
                .hasScheduledTick(pos, pipe))
            level.scheduleTick(pos, pipe, 1);
    }

    public SoundEvent getGrowSound() {
        return growSound;
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
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return voxelShapeGetter.apply(pState.getValue(SIZE), pState.getValue(WALL), pState.hasProperty(FACING) ? pState.getValue(FACING) : Direction.SOUTH);
    }

    // on right-click
    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //if (pLevel.isClientSide()) { return InteractionResult.PASS; }

        ItemStack heldItem = pPlayer.getItemInHand(pHand); // extending pipe
        //goooggly eyes
        if (heldItem.getItem() instanceof GogglesItem) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof GenericPipeBlockEntity pipeBE) {
                if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer sp) {
                    pipeBE.setGoggles(!pipeBE.hasGoggles());
                    pipeBE.setChanged();
                    pipeBE.sendData();
                    SoundEvent goggleSound;
                    goggleSound = SoundEvents.ARMOR_EQUIP_GENERIC;
                    pLevel.playSound(null, pPos, goggleSound, SoundSource.BLOCKS, 0.5f, 1f);

                    AllTriggers.PIPE_GOGGLES.trigger(sp);
                    /*
                    //In case you want the pipes to eat your goggles
                    if (!pPlayer.isCreative())
                        heldItem.shrink(1);
                    */
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        }
        //longer-ing (extending pipe)
        if (heldItem.getItem() == this.asItem()) {
            incrementSize(pLevel, pPos, true);
            return InteractionResult.SUCCESS;
        }
        //swapping
        if (heldItem.getItem() instanceof GenericPipeBlockItem) { // swapping pipes
            if (substitutePipe(pState, pLevel, pPos, heldItem, pPlayer) == InteractionResult.SUCCESS) {
                if (!pPlayer.isCreative()) {
                    heldItem.shrink(1);
                    pPlayer.setItemInHand(pHand, heldItem);

                    pPlayer.getInventory().placeItemBackInInventory(new ItemStack(this.asItem()));
                }
                return InteractionResult.SUCCESS;
            } else { // FAIL
                AllSoundEvents.DENY.playOnServer(pLevel, pPos);
                pPlayer.displayClientMessage(Component.translatable("pipeorgans.blocks.pipes.replace_pipe_deny"), true);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        withBlockEntityDo(pLevel, pPos, GenericPipeBlockEntity::updatePitch);
    }

    // check if placed on fluid tank or windchest
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState attachedState = pLevel.getBlockState(pPos.relative(getAttachedDirection(pState)));

        //Compat for Create Connected's Fluid Vessel (Sideway fluid tanks)
        if (ModCompat.CREATE_CONNECTED && CreateConnectedCompat.isFluidVessel(attachedState))
            return true;
        return (FluidTankBlock.isTank(attachedState)
                || attachedState.getBlock() instanceof WindchestBlock);
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
