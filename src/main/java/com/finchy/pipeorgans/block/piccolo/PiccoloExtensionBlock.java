package com.finchy.pipeorgans.block.piccolo;

import com.finchy.pipeorgans.block.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.block.generic.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PiccoloExtensionBlock extends QuadrupleExtensionBlock {
    public PiccoloExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.PICCOLO;
    }
}
