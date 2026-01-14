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

public class SingleExtensionBlock extends GenericExtensionBlock<ExtensionShapes.Single> {

    public static final EnumProperty<ExtensionShapes.Single> SHAPE = EnumProperty.create("shape", ExtensionShapes.Single.class);

    public SingleExtensionBlock(Properties pProperties, BlockEntry<? extends GenericPipeBlock> pipeBlock, TriFunction<ExtensionShapes.Single, PipeSize, Direction, VoxelShape> voxelShapeGetter) {
        super(pProperties, SHAPE, pipeBlock, voxelShapeGetter);
    }

    @Override
    protected void registerDefaultStateWithShape() {
        BlockState defaultState = defaultBlockState()
                .setValue(SHAPE, ExtensionShapes.Single.SINGLE)
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
