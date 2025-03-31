package com.finchy.pipeorgans.content.pipes.diapason;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class DiapasonBlock extends GenericPipeBlock {
    public DiapasonBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.DIAPASON;
        this.extensionBlock = AllBlocks.DIAPASON_EXTENSION;
        this.blockEntity = AllBlockEntities.DIAPASON_BLOCK_ENTITY;
    }
}
