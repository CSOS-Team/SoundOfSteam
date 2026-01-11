package com.finchy.pipeorgans.content.pipes.generic.subtypes2;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.function.TriFunction;

public class HorizontalPipeBlock extends GenericPipeBlock {

    public HorizontalPipeBlock(Properties pProperties, ExtensionMode extensionMode,
                               PipeMaterial pipeMaterial,
                               BlockEntry<? extends GenericExtensionBlock<? extends ExtensionShapes.IExtensionShape<?>>> extensionBlock,
                               BlockEntityEntry<? extends GenericPipeBlockEntity> blockEntityType,
                               TriFunction<PipeSize, Boolean, Direction, VoxelShape> voxelShapeGetter) {
        super(pProperties, extensionMode, pipeMaterial, extensionBlock, blockEntityType, voxelShapeGetter);
    }

    @Override
    public Direction getExtensionDirection(BlockState pipeState) {
        return pipeState.getValue(GenericPipeBlock.FACING).getOpposite();
    }

    @Override
    public Direction getPipeDirectionFromExtension(BlockState extensionState) {
        return extensionState.getValue(GenericPipeBlock.FACING).getOpposite();
    }
}
