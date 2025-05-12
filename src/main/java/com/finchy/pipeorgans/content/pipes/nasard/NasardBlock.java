package com.finchy.pipeorgans.content.pipes.nasard;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class NasardBlock extends GenericPipeBlock {
    public NasardBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.NASARD;
        extensionBlock = AllBlocks.NASARD_EXTENSION;
        blockEntity = AllBlockEntities.NASARD_BLOCK_ENTITY;
        shape = GenericWhistleProperties.WhistleShape.SLIM;
    }
}
