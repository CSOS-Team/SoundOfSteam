package com.finchy.pipeorgans.blockentity;

import com.finchy.pipeorgans.init.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GedecktBlockEntity extends BlockEntity {
    public GedecktBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.GEDECKT_BLOCK_ENTITY.get(), pos, blockState);
    }
}
