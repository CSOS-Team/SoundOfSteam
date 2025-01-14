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

public class GedecktBlockEntity extends SmartBlockEntity {

    public WeakReference<FluidTankBlockEntity> source;
    public LerpedFloat animation;
    protected int pitch;

    public GedecktBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.GEDECKT_BLOCK_ENTITY.get(), pos, blockState);
        source = new WeakReference<>(null);
        animation = LerpedFloat.angular();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("Pitch", pitch);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        pitch = tag.getInt("Pitch");
        super.read(tag, clientPacket);
    }

    public void updatePitch() {
        BlockPos currentPos = worldPosition.above();
        int newPitch;
        for (newPitch = 0; newPitch <= 24; newPitch += 2) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!(blockState.getBlock() instanceof GedecktExtensionBlock))
                break;
            if (blockState.getValue(GedecktExtensionBlock.SHAPE) == GedecktExtensionBlock.GedecktExtensionShape.SINGLE) {
                newPitch++;
                break;
            }
            currentPos = currentPos.above();
        }
        if (pitch == newPitch)
            return;
        pitch = newPitch;

        notifyUpdate();

        FluidTankBlockEntity tank = getTank();
        if (tank != null && tank.boiler != null)
            tank.boiler.checkPipeOrganAdvancement(tank);
    }

    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = WhistleBlock.getAttachedDirection(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }
}
