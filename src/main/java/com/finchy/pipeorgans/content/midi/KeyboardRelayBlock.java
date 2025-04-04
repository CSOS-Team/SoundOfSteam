package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("NullableProblems")
public class KeyboardRelayBlock extends Block implements IBE<KeyboardRelayBlockEntity>, IWrenchable {

    public KeyboardRelayBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<KeyboardRelayBlockEntity> getBlockEntityClass() {
        return KeyboardRelayBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KeyboardRelayBlockEntity> getBlockEntityType() {
        return AllBlockEntities.KEYBOARD_RELAY_BLOCK_ENTITY.get();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) { // serverside only
            return InteractionResult.PASS;
        }

        if (pHand.equals(InteractionHand.OFF_HAND)) { // temporary, to prevent function being called twice
            return InteractionResult.PASS;
        }
        if (pPlayer.getItemInHand(pHand).getItem() instanceof StopMasterBlockItem) { // if player is linking stopmaster
            // surely there's a better way to do it?
            return InteractionResult.PASS;
        }

        if (KeyboardRelayBlockEntity.playerInRange(pPlayer, pLevel, pPos)) { // if player close enough

            if (!KeyboardRelayBlockEntity.playerIsUsing(pPlayer)) { // if player is not currently using a keyboard relay
                withBlockEntityDo(pLevel, pPos, be -> be.tryStartUsing(pPlayer)); // start using relay

            } else { // if player IS using a keyboard relay
                withBlockEntityDo(pLevel, pPos, be -> be.tryStopUsing(pPlayer)); // stop using relay

            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            if (!pLevel.isClientSide) {
                withBlockEntityDo(pLevel, pPos, KeyboardRelayBlockEntity::onBlockRemoved);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
