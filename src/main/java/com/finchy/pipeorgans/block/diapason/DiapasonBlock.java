package com.finchy.pipeorgans.block.diapason;

import com.finchy.pipeorgans.block.generic.GenericPipeBlock;
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
