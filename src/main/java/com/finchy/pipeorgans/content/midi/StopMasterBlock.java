package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StopMasterBlock extends Block implements IBE<StopMasterBlockEntity>, IWrenchable {

    public StopMasterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<StopMasterBlockEntity> getBlockEntityClass() {
        return StopMasterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StopMasterBlockEntity> getBlockEntityType() {
        return AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get();
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide // serverside only
                && pLevel.getBlockEntity(pPos) instanceof StopMasterBlockEntity be) { // check that be has been placed (and get be)

            if (pStack.hasTag()) { // if blockitem has been linked to midi source
                CompoundTag tag = pStack.getTag();
                if (tag != null) {
                    int[] sourcePos = tag.getIntArray("midi_source_pos");
                    // transfer data to block entity
                    be.linkToSource(
                            pLevel,
                            new BlockPos(sourcePos[0], sourcePos[1], sourcePos[2])
                    );
                }

            } /* else { // if blockitem has not been linked
                // nothing
            } */
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            if (!pLevel.isClientSide) {
                withBlockEntityDo(pLevel, pPos, StopMasterBlockEntity::onBlockRemoved);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
