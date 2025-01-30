package com.finchy.pipeorgans.block.trompette;

import com.finchy.pipeorgans.block.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class TrompetteBlock extends GenericPipeBlock {

    public TrompetteBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.TROMPETTE;
        this.extensionBlock = AllBlocks.TROMPETTE_EXTENSION;
        this.blockEntity = AllBlockEntities.TROMPETTE_BLOCK_ENTITY;
    }
}
