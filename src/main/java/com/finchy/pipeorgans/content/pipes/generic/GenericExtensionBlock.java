package com.finchy.pipeorgans.content.pipes.generic;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.function.TriFunction;

public abstract class GenericExtensionBlock<T extends Enum<T> & ExtensionShapes.IExtensionShape<T> & StringRepresentable> extends Block implements IWrenchable {

    public final EnumProperty<T> SHAPE;
    public static final EnumProperty<PipeSize> SIZE = GenericPipeBlock.SIZE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected final BlockEntry<? extends GenericPipeBlock> pipeBlock;
    protected final TriFunction<T, PipeSize, Direction, VoxelShape> voxelShapeGetter;

    public GenericExtensionBlock(Properties pProperties,
                                 EnumProperty<T> shapeProperty, BlockEntry<? extends GenericPipeBlock> pipeBlock,
                                 TriFunction<T, PipeSize, Direction, VoxelShape> voxelShapeGetter) {
        super(pProperties);

        this.SHAPE = shapeProperty;

        this.pipeBlock = pipeBlock;
        this.voxelShapeGetter = voxelShapeGetter;
    }

    protected abstract void registerDefaultStateWithShape();

    public boolean isDirectional() {
        return false;
    }

    public BlockPos findRoot(LevelAccessor pLevel, BlockPos pPos, BlockState state) {
        Direction towardRoot = pipeBlock.get().getPipeDirectionFromExtension(state);
        BlockPos currentPos = pPos.relative(towardRoot);
        while (true) {
            BlockState blockState = pLevel.getBlockState(currentPos);
            if (blockState.getBlock().equals(this)) {
                currentPos = currentPos.relative(towardRoot);
                continue;
            }
            return currentPos;
        }
    }

    protected UseOnContext relocateContext(UseOnContext context, BlockPos target) {
        return new UseOnContext(context.getPlayer(), context.getHand(),
                new BlockHitResult(context.getClickLocation(), context.getClickedFace(), target, context.isInside()));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (!(heldItem.getItem() instanceof GenericPipeBlockItem)) {
            return InteractionResult.PASS;
        }
        BlockPos rootPos = findRoot(pLevel, pPos, pState);
        BlockState pipeState = pLevel.getBlockState(rootPos);
        if (pipeState.getBlock() instanceof GenericPipeBlock pipe) {
            return pipe.use(pipeState, pLevel, rootPos, pPlayer, pHand,
                    new BlockHitResult(pHit.getLocation(), pHit.getDirection(), rootPos, pHit.isInside()));
        }
        return InteractionResult.PASS;
    }



    // used for blockstate gen
    public String getShapeSerialisedName(BlockState state) {
        if (!(state.getBlock() instanceof GenericExtensionBlock<?>))
            return null;
        return state.getValue(SHAPE).getSerializedName();
    }



    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState below = pLevel.getBlockState(pPos.relative(pipeBlock.get().getPipeDirectionFromExtension(pState)));
        return (below.is(this) && below.getValue(SHAPE).isConnected())
                || below.getBlock().equals(pipeBlock.get());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return voxelShapeGetter.apply(pState.getValue(SHAPE), pState.getValue(SIZE), pState.hasProperty(FACING) ? pState.getValue(FACING) : Direction.SOUTH);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        Direction pipeOutDirection = pipeBlock.get().getPipeDirectionFromExtension(pState).getOpposite(); // extension "facing" property faces toward the pipe
        if (pDirection.getAxis() != pipeOutDirection.getAxis()) // only respond to changes along the length of the pipe
            return pState;

        if (pDirection == pipeOutDirection) { // check for updates further along the pipe
            T shape = pState.getValue(SHAPE); // current extensionShape

            boolean connected = shape.isConnected();
            boolean shouldConnect = pLevel.getBlockState(pPos.relative(pipeOutDirection))
                    .is(this);
            if (!connected && shouldConnect)
                return pState.setValue(SHAPE, shape.getConnected()); // set shape to the connected variant
            if (connected && !shouldConnect)
                return pState.setValue(SHAPE, shape.getLongestNonConnected()); // set shape to the not-quite-connected variant
            return pState;
        }

        return !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState()
                : pState.setValue(SIZE, pLevel.getBlockState(pPos.relative(pipeOutDirection.getOpposite()))
                .getValue(SIZE));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!(level instanceof ServerLevel))
            return InteractionResult.SUCCESS;

        if (state.getValue(SHAPE).isSingle())
            return IWrenchable.super.onSneakWrenched(state, context); // any shift click will remove the block, so defer to regular shift-wrench logic

        Direction pipeOutFacing = pipeBlock.get().getPipeDirectionFromExtension(state).getOpposite();
        double clickedPosition = pipeBlock.get().getExtensionClickPosition(pos, context.getClickLocation(), pipeOutFacing);

        T wrenchedShape = state.getValue(SHAPE).getExtensionShapeForClickPosition(clickedPosition);
        if (wrenchedShape == null) // if the player clicked such that the extension should be removed
            return IWrenchable.super.onSneakWrenched(state, context);

        level.setBlock(pos, state.setValue(SHAPE, wrenchedShape), 3);
        IWrenchable.playRemoveSound(level, pos);

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos findRoot = findRoot(level, context.getClickedPos(), state);
        BlockState blockState = level.getBlockState(findRoot);
        if (blockState.getBlock() instanceof GenericPipeBlock pipe)
            return pipe.onWrenched(blockState, relocateContext(context, findRoot));
        return IWrenchable.super.onWrenched(state, context);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pOldState.getBlock() != this || pOldState.getValue(SHAPE) != pState.getValue(SHAPE))
            GenericPipeBlock.queuePitchUpdate(pLevel, findRoot(pLevel, pPos, pState));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pNewState.getBlock() != this || pNewState.getValue(SHAPE) != pState.getValue(SHAPE))
            GenericPipeBlock.queuePitchUpdate(pLevel, findRoot(pLevel, pPos, pState));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(pipeBlock);
    }
}
