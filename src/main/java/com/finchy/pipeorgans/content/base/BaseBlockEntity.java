package com.finchy.pipeorgans.content.base;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.init.AllSoundEvents;
import com.finchy.pipeorgans.init.AllTriggers;
import com.finchy.pipeorgans.util.RangedPowerAdvancement;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;

public class BaseBlockEntity extends SmartBlockEntity {

    public WeakReference<FluidTankBlockEntity> source;
    private boolean wasPoweredLastTick = false;
    private boolean advancementTriggered = false;

    public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
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
        if (level == null)
            return;

        FluidTankBlockEntity tank = getTank();
        BlockState state = getBlockState();
        BlockPos attachedPos = worldPosition.relative(BaseBlock.getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);
        boolean isActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            isActive = windchest.isMasterActive(
                    level,
                    attachedState.getValue(GenericPipeBlock.FACING),
                    attachedPos
            );
        }

        boolean powered =
                ((tank != null && tank.boiler.isActive()
                        && (tank.boiler.passiveHeat || tank.boiler.activeHeat > 0))
                        || isActive)
                        && isPowered();

        // Advancement logic
        if (!level.isClientSide) {
            if (powered && !wasPoweredLastTick) {
                RangedPowerAdvancement.trigger(level, worldPosition, 16,
                        AllTriggers.STEAM_BASE::trigger
                );
            }
            wasPoweredLastTick = powered;
            return; // skip client-only particle/sound logic
        }

        // Particles and Sound
        if (powered) {
            createSteamJet();
            playSteamSound();
        } else {
            stopSteamSound();
        }

        wasPoweredLastTick = powered;
    }

    @Nullable
    private BaseSoundInstance steamSound;

    public void createSteamJet() {
        Vec3 v = new Vec3(0, 0.8125, 0).add(Vec3.atBottomCenterOf(worldPosition));
        Vec3 m = new Vec3(0, 1, 0);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }

    private void playSteamSound() {
        if (steamSound == null || steamSound.isStopped()) {
            steamSound = new BaseSoundInstance(
                    worldPosition, AllSoundEvents.STEAM_HISS.get()
            );
            Minecraft.getInstance().getSoundManager().play(steamSound);
        }

        steamSound.keepAlive();
    }

    private void stopSteamSound() {
        if (steamSound != null) {
            steamSound.fadeOut();
            steamSound = null;
        }
    }

    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = BaseBlock.getAttachedDirection(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }
}
