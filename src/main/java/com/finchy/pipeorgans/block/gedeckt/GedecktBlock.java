package com.finchy.pipeorgans.block.gedeckt;

import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import net.minecraft.sounds.SoundEvents;

public class GedecktBlock extends GenericWhistleBlock {

    @Override
    public void setWhistleProperties() {
        this.baseBlock = AllBlocks.GEDECKT;
        this.extensionBlock = AllBlocks.GEDECKT_EXTENSION;
        this.blockEntity = AllBlockEntities.GEDECKT_BLOCK_ENTITY;
        this.growSound = SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.get();
    }

    public GedecktBlock(Properties pProperties) {
        super(pProperties);
    }
}
