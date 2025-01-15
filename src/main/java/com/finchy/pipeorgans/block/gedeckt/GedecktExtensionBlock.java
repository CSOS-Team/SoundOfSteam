package com.finchy.pipeorgans.block.gedeckt;

import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class GedecktExtensionBlock extends GenericWhistleExtensionBlock {

    @Override
    public void setWhistleProperties() {
        this.baseBlock = AllBlocks.GEDECKT;
        this.extensionBlock = AllBlocks.GEDECKT_EXTENSION;
        this.blockEntity = AllBlockEntities.GEDECKT_BLOCK_ENTITY;
    }

    public GedecktExtensionBlock(Properties pProperties) {
        super(pProperties);
    }
}
