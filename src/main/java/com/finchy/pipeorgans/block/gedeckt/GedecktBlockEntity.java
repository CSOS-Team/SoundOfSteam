package com.finchy.pipeorgans.block.gedeckt;

import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GedecktBlockEntity extends GenericWhistleBlockEntity {

    @Override
    public void setWhistleProperties() {
        this.baseBlock = AllBlocks.GEDECKT;
        this.extensionBlock = AllBlocks.GEDECKT_EXTENSION;
        this.blockEntity = AllBlockEntities.GEDECKT_BLOCK_ENTITY;
    }

    public GedecktBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }
}
