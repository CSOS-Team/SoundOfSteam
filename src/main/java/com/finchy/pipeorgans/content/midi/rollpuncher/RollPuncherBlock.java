package com.finchy.pipeorgans.content.midi.rollpuncher;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class RollPuncherBlock extends HorizontalDirectionalBlock implements IBE<RollPuncherBlockEntity> {

    public RollPuncherBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;
        withBlockEntityDo(pLevel, pPos,
                be -> NetworkHooks.openScreen((ServerPlayer) pPlayer, be, be::sendToMenu));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.hasBlockEntity() || pState.getBlock() == pNewState.getBlock())
            return;
        withBlockEntityDo(pLevel, pPos, be -> ItemHelper.dropContents(pLevel, pPos, be.inventory));
        pLevel.removeBlockEntity(pPos);
    }

    @Override
    public Class<RollPuncherBlockEntity> getBlockEntityClass() {
        return RollPuncherBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RollPuncherBlockEntity> getBlockEntityType() {
        return AllBlockEntities.ROLL_PUNCHER_BLOCK_ENTITY.get();
    }
}
