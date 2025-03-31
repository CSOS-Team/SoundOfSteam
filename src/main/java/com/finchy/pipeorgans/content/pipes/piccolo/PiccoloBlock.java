package com.finchy.pipeorgans.content.pipes.piccolo;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class PiccoloBlock extends QuadruplePipeBlock {
    public PiccoloBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.PICCOLO;
        this.extensionBlock = AllBlocks.PICCOLO_EXTENSION;
        this.blockEntity = AllBlockEntities.PICCOLO_BLOCK_ENTITY;
        registerDefaultState(defaultBlockState().setValue(SIZE, GenericWhistleProperties.WhistleSize.SMALL));
    }
}
