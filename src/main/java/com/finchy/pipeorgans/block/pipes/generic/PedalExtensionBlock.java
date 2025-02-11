package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.block.Generic;

public class PedalExtensionBlock extends GenericExtensionBlock {

    public PedalExtensionBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, Generic.QuadrupleExtensionShape.QUAD)
                .setValue(SIZE, Generic.WhistleSize.LARGE));
    }
}
