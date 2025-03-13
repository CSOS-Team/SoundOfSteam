package com.finchy.pipeorgans.block;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.finchy.pipeorgans.init.AllBlockEntities.WINDCHEST_MASTER_BLOCK_ENTITY;

public class WindchestMasterBlockEntity extends SmartBlockEntity {

    public WindchestMasterBlockEntity(BlockPos pos, BlockState state) {
        super(WINDCHEST_MASTER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
   @Override
    public void lazyTick(){
        WindchestMasterBlock.updateMasterWindy(this.level, this.worldPosition);
   }
}
