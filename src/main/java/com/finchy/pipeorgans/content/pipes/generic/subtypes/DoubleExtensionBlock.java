package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.function.TriFunction;

public class DoubleExtensionBlock extends GenericExtensionBlock<ExtensionShapes.Double> {

    public static final EnumProperty<ExtensionShapes.Double> SHAPE = EnumProperty.create("shape", ExtensionShapes.Double.class);

    public DoubleExtensionBlock(Properties pProperties, BlockEntry<? extends GenericPipeBlock> pipeBlock, TriFunction<ExtensionShapes.Double, PipeSize, Direction, VoxelShape> voxelShapeGetter) {
        super(pProperties, SHAPE, pipeBlock, voxelShapeGetter);
    }

    @Override
    protected void registerDefaultStateWithShape() {
        BlockState defaultState = defaultBlockState()
                .setValue(SHAPE, ExtensionShapes.Double.SINGLE)
                .setValue(SIZE, PipeSize.MEDIUM);
        if (isDirectional())
            defaultState.setValue(FACING, Direction.NORTH);
        registerDefaultState(defaultState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE, SIZE);
        if (isDirectional())
            pBuilder.add(FACING);
        super.createBlockStateDefinition(pBuilder);
    }
}
