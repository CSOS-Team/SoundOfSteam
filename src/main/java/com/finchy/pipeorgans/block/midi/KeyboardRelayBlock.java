package com.finchy.pipeorgans.block.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
        if (pLevel.isClientSide) {
            return InteractionResult.PASS;
        }
        if (pHand.equals(InteractionHand.OFF_HAND)) { // temporary, to prevent function being called twice
            return InteractionResult.PASS;
        }
        if (KeyboardRelayBlockEntity.playerInRange(pPlayer, pLevel, pPos)) { // if player close enough

            if (!KeyboardRelayBlockEntity.playerIsUsing(pPlayer)) { // if player is not currently using a keyboard relay
                withBlockEntityDo(pLevel, pPos, be -> be.tryStartUsing(pPlayer)); // start using relay

            } else { // if player IS using a keyboard relay
                withBlockEntityDo(pLevel, pPos, be -> be.tryStopUsing(pPlayer)); // stop using relay

            }

            PipeOrgans.LOGGER.error("SUCCESS");
            return InteractionResult.SUCCESS;
        }
        PipeOrgans.LOGGER.error("TOO FAR");
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            if (!pLevel.isClientSide) {
                Entity playerEntity = ((ServerLevel) pLevel).getEntity(user);
                if (playerEntity instanceof Player)
                    stopUsing((Player) playerEntity);
                withBlockEntityDo(world, pos, be -> be.tryStopUsing(state));
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }
}
