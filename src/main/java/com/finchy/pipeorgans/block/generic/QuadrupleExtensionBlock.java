package com.finchy.pipeorgans.block.generic;

import com.finchy.pipeorgans.block.Generic;

public class QuadrupleExtensionBlock extends GenericExtensionBlock {

    public QuadrupleExtensionBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, Generic.QuadrupleExtensionShape.SINGLE)
                .setValue(SIZE, Generic.WhistleSize.MEDIUM));
    }
}
