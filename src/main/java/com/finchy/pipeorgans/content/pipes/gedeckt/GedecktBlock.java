package com.finchy.pipeorgans.content.pipes.gedeckt;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class GedecktBlock extends GenericPipeBlock {
    public GedecktBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.GEDECKT;
        extensionBlock = AllBlocks.GEDECKT_EXTENSION;
        blockEntity = AllBlockEntities.GEDECKT_BLOCK_ENTITY;
        shape = GenericWhistleProperties.WhistleShape.SLIM;
    }
}
