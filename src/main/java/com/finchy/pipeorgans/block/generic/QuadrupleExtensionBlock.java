package com.finchy.pipeorgans.block.generic;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public class QuadrupleExtensionBlock extends Block implements IWrenchable {

    public static final EnumProperty<Generic.QuadrupleExtensionShape> SHAPE =
            EnumProperty.create("shape", Generic.QuadrupleExtensionShape.class);
    public static final EnumProperty<Generic.WhistleSize> SIZE = QuadruplePipeBlock.SIZE;

    public RegistryObject<? extends QuadruplePipeBlock> baseBlock;

    public QuadrupleExtensionBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, Generic.QuadrupleExtensionShape.SINGLE)
                .setValue(SIZE, Generic.WhistleSize.SMALL));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.getQuadrupleExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(SHAPE, SIZE));
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState below = pLevel.getBlockState(pPos.below());
        return below.is(this) && below.getValue(SHAPE) != Generic.QuadrupleExtensionShape.SINGLE
                || below.getBlock() instanceof QuadruplePipeBlock;
    }


    // todo: FIX THIS
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pFacing.getAxis() != Direction.Axis.Y)
            return pState;

        if (pFacing == Direction.UP) {
            boolean connected = pState.getValue(SHAPE) == Generic.QuadrupleExtensionShape.QUADRUPLE_CONNECTED;
            boolean shouldConnect = pLevel.getBlockState(pCurrentPos.above())
                    .is(this);
            if (!connected && shouldConnect)
                return pState.setValue(SHAPE, Generic.QuadrupleExtensionShape.QUADRUPLE_CONNECTED);
            if (connected && !shouldConnect)
                return pState.setValue(SHAPE, Generic.QuadrupleExtensionShape.QUADRUPLE);
            return pState;
        }

        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState()
                : pState.setValue(SIZE, pLevel.getBlockState(pCurrentPos.below())
                .getValue(SIZE));
    }

    public static BlockPos findRoot(LevelAccessor pLevel, BlockPos pPos) {
        BlockPos currentPos = pPos.below();
        while (true) {
            BlockState blockState = pLevel.getBlockState(currentPos);
            if (blockState.getBlock() instanceof QuadrupleExtensionBlock) {
                currentPos = currentPos.below();
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
        if (pPlayer == null || heldItem.getItem() != this.baseBlock.get().asItem()) {;
            return InteractionResult.PASS;
        }
        BlockPos rootFound = findRoot(pLevel, pPos);
        BlockState blockState = pLevel.getBlockState(rootFound);
        if (blockState.getBlock() instanceof QuadruplePipeBlock pipe)
            return pipe.use(blockState, pLevel, rootFound, pPlayer, pHand,
                    new BlockHitResult(pHit.getLocation(), pHit.getDirection(), rootFound, pHit.isInside()));
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .5f || state.getValue(SHAPE) == Generic.QuadrupleExtensionShape.SINGLE)
            return IWrenchable.super.onSneakWrenched(state, context);
        if (!(world instanceof ServerLevel))
            return InteractionResult.SUCCESS;
        world.setBlock(pos, state.setValue(SHAPE, Generic.QuadrupleExtensionShape.SINGLE), 3);
        playRemoveSound(world, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos findRoot = findRoot(level, context.getClickedPos());
        BlockState blockState = level.getBlockState(findRoot);
        if (blockState.getBlock()instanceof QuadruplePipeBlock pipe)
            return pipe.onWrenched(blockState, relocateContext(context, findRoot));
        return IWrenchable.super.onWrenched(state, context);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pOldState.getBlock() != this || pOldState.getValue(SHAPE) != pState.getValue(SHAPE))
            QuadruplePipeBlock.queuePitchUpdate(pLevel, findRoot(pLevel, pPos));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pOldState.getBlock() != this || pOldState.getValue(SHAPE) != pState.getValue(SHAPE))
            QuadruplePipeBlock.queuePitchUpdate(pLevel, findRoot(pLevel, pPos));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(this.baseBlock.get());
    }
}
