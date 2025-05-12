package com.finchy.pipeorgans.content.pipes.posaune;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.PedalPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class PosauneBlock extends PedalPipeBlock {

    public PosauneBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.POSAUNE;
        extensionBlock = AllBlocks.POSAUNE_EXTENSION;
        blockEntity = AllBlockEntities.POSAUNE_BLOCK_ENTITY;
        shape = GenericWhistleProperties.WhistleShape.SLIM;
    }
}
