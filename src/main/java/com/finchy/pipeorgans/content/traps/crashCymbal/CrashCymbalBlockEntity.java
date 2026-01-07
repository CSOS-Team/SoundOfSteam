package com.finchy.pipeorgans.content.traps.crashCymbal;

import com.finchy.pipeorgans.init.AllSoundEvents;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.ref.WeakReference;
import java.util.List;


public class CrashCymbalBlockEntity extends SmartBlockEntity {

    public WeakReference<FluidTankBlockEntity> source;

    // track previous powered state
    private boolean wasPowered = false;

    public CrashCymbalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        source = new WeakReference<>(null);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    protected boolean isPowered() {
        return getBlockState().getOptionalValue(CrashCymbalBlock.POWERED)
                .orElse(false);
    }


    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        boolean powered = isPowered();

        // play sound only on rising edge
        if (powered && !wasPowered) {
            playCrashSound();
        }

        wasPowered = powered;
    }

    private void playCrashSound() {
        level.playSound(
                null,                                   // player: null = all nearby
                worldPosition,                          // block position
                AllSoundEvents.CRASH_CYMBAL.get(),      // get the SoundEvent
                SoundSource.BLOCKS,                      // category
                1.0f,                                   // volume
                1.0f                                    // pitch
        );
    }
}
