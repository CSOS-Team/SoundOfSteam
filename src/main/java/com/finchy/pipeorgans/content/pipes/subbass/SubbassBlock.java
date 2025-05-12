package com.finchy.pipeorgans.content.pipes.subbass;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.PedalPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class SubbassBlock extends PedalPipeBlock {

    public SubbassBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.SUBBASS;
        extensionBlock = AllBlocks.SUBBASS_EXTENSION;
        blockEntity = AllBlockEntities.SUBBASS_BLOCK_ENTITY;
    }

}
