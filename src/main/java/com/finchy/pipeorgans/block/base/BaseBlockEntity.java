package com.finchy.pipeorgans.block.base;

import com.finchy.pipeorgans.block.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;
import java.util.List;

public class BaseBlockEntity extends SmartBlockEntity {

    public WeakReference<FluidTankBlockEntity> source;

    public BaseBlockEntity(BlockPos pos, BlockState state) {
         super(AllBlockEntities.BASE_BLOCK_ENTITY.get(), pos, state);
        source = new WeakReference<>(null);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    protected boolean isPowered() {
        return getBlockState().getOptionalValue(GenericPipeBlock.POWERED)
                .orElse(false);
    }

    @Override
    public void tick() {
        super.tick();
        FluidTankBlockEntity tank = getTank();
        if (isPowered() && level.getGameTime() % 8 == 0
                && (tank != null && tank.boiler.isActive() && (tank.boiler.passiveHeat || tank.boiler.activeHeat > 0)
                || isVirtual())) {
            createSteamJet();
        }

    }

    public void createSteamJet() {
        Vec3 v = new Vec3(0, 0.8125, 0).add(Vec3.atBottomCenterOf(worldPosition));
        Vec3 m = new Vec3(0, 1, 0);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }

    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = GenericPipeBlock.getAttachedDirection(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }
}
