package com.finchy.pipeorgans.content.traps.zimblestern;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction.Axis;



public class ZimblesternBlock extends KineticBlock implements IBE<ZimblesternBlockEntity>, IWrenchable {

    public ZimblesternBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.ZIMBLESTERN.get(getRotationAxis(state));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public Class<ZimblesternBlockEntity> getBlockEntityClass() {
        return ZimblesternBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ZimblesternBlockEntity> getBlockEntityType() {
        return AllBlockEntities.ZIMBLESTERN_BLOCK_ENTITY.get();
    }
}
