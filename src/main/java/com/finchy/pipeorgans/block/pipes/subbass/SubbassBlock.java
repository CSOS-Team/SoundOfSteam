package com.finchy.pipeorgans.block.pipes.subbass;

import com.finchy.pipeorgans.block.pipes.generic.PedalPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class SubbassBlock extends PedalPipeBlock {

    public SubbassBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.SUBBASS;
        this.extensionBlock = AllBlocks.SUBBASS_EXTENSION;
        this.blockEntity = AllBlockEntities.SUBBASS_BLOCK_ENTITY.getDelegate();
    }

}
