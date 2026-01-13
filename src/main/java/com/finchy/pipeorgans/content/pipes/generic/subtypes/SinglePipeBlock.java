package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.function.TriFunction;

public abstract class SinglePipeBlock extends GenericPipeBlock {
    public SinglePipeBlock(Properties pProperties, PipeDirection pipeDirection, PipeMaterial pipeMaterial, BlockEntry<? extends GenericExtensionBlock<?>> extensionBlock, BlockEntityEntry<? extends GenericPipeBlockEntity> blockEntityType, TriFunction<PipeSize, Boolean, Direction, VoxelShape> voxelShapeGetter) {
        super(pProperties, pipeDirection, pipeMaterial, extensionBlock, blockEntityType, voxelShapeGetter);
    }

    @Override
    public int extensionsPerBlock() {
        return 1;
    }
}
