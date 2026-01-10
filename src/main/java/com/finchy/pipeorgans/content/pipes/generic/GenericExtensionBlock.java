package com.finchy.pipeorgans.content.pipes.generic;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class GenericExtensionBlock<P extends Enum<P> & ExtensionShapes.ExtensionShape & StringRepresentable> extends Block implements IWrenchable {

    public final EnumProperty<P> SHAPE;
    public static final EnumProperty<PipeSize> SIZE = GenericPipeBlock.SIZE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected BlockEntry<? extends GenericPipeBlock> baseBlock;

    public GenericExtensionBlock(Properties pProperties, EnumProperty<P> shapeProperty) {
        super(pProperties);
        this.SHAPE = shapeProperty;
        registerDefaultStateWithSize();
    }

    protected abstract void registerDefaultStateWithSize();

    public BlockPos findRoot(LevelAccessor pLevel, BlockPos pPos, BlockState state) {
        BlockPos currentPos = pPos.below();
        while (true) {
            BlockState blockState = pLevel.getBlockState(currentPos);
            if (blockState.getBlock() instanceof GenericExtensionBlock) {
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

    public boolean isDirectional() {
        return false;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (heldItem.getItem() != this.baseBlock.get().asItem()) {
            return InteractionResult.PASS;
        }
        BlockPos root = findRoot(pLevel, pPos, pState);
        BlockState blockState = pLevel.getBlockState(root);
        if (blockState.getBlock() instanceof GenericPipeBlock pipe)
            return pipe.use(blockState, pLevel, root, pPlayer, pHand,
                    new BlockHitResult(pHit.getLocation(), pHit.getDirection(), root, pHit.isInside()));
        return InteractionResult.PASS;
    }

    @Override
    public abstract InteractionResult onSneakWrenched(BlockState state, UseOnContext context);

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos findRoot = findRoot(level, context.getClickedPos(), state);
        BlockState blockState = level.getBlockState(findRoot);
        if (blockState.getBlock() instanceof GenericPipeBlock pipe)
            return pipe.onWrenched(blockState, relocateContext(context, findRoot));
        return IWrenchable.super.onWrenched(state, context);
    }

    protected InteractionResult callSuperOnSneakWrenched(BlockState state, UseOnContext context) {
        return IWrenchable.super.onSneakWrenched(state, context);
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
        return new ItemStack(this.baseBlock.get());
    }
}
