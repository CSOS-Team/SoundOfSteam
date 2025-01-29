package com.finchy.pipeorgans.block.gamba;

import com.finchy.pipeorgans.block.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class GambaBlock extends GenericPipeBlock {
    public GambaBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.GAMBA;
        this.extensionBlock = AllBlocks.GAMBA_EXTENSION;
        this.blockEntity = AllBlockEntities.GAMBA_BLOCK_ENTITY;
    }
}
