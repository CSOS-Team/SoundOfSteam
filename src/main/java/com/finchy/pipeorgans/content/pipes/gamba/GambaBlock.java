package com.finchy.pipeorgans.content.pipes.gamba;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class GambaBlock extends GenericPipeBlock {
    public GambaBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.GAMBA;
        extensionBlock = AllBlocks.GAMBA_EXTENSION;
        blockEntity = AllBlockEntities.GAMBA_BLOCK_ENTITY;
    }
}
