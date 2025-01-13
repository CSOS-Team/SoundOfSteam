package com.finchy.pipeorgans.blockentity;

import com.finchy.pipeorgans.block.GedecktBlock;
import com.finchy.pipeorgans.block.GedecktExtensionBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleExtenderBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.ref.WeakReference;
import java.util.List;

public class GedecktBlockEntity extends BlockEntity {

    public WeakReference<FluidTankBlockEntity> source;
    public LerpedFloat animation;
    protected int pitch;

    public GedecktBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.GEDECKT_BLOCK_ENTITY.get(), pos, blockState);
        source = new WeakReference<>(null);
        animation = LerpedFloat.angular();
    }

}
