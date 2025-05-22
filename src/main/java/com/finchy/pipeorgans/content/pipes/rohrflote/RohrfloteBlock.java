package com.finchy.pipeorgans.content.pipes.rohrflote;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class RohrfloteBlock extends GenericPipeBlock {
    public RohrfloteBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.ROHRFLOTE;
        this.extensionBlock = AllBlocks.ROHRFLOTE_EXTENSION;
        this.blockEntity = AllBlockEntities.ROHRFLOTE_BLOCK_ENTITY;
    }
}
