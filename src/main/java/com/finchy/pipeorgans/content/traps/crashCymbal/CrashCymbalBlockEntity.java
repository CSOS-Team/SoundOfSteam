package com.finchy.pipeorgans.content.traps.crashCymbal;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.init.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CrashCymbalBlockEntity extends SmartBlockEntity {

    private boolean wasPowered = false;

    public CrashCymbalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void onLoad() {
        super.onLoad();
        wasPowered = isEffectivelyPowered();
    }

    //raw redstone state
    protected boolean isRedstonePowered() {
        return getBlockState()
                .getOptionalValue(CrashCymbalBlock.POWERED)
                .orElse(false);
    }

    // windchest-based powered state (matches pipe logic)
    protected boolean isEffectivelyPowered() {
        if (level == null)
            return false;

        BlockState state = getBlockState();
        BlockPos attachedPos =
                worldPosition.relative(CrashCymbalBlock.getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);

        boolean windchestActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            windchestActive = windchest.isMasterActive(
                    level,
                    attachedState.getValue(CrashCymbalBlock.FACING),
                    attachedPos
            );
        }

        return windchestActive && isRedstonePowered();
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide)
            return;

        boolean powered = isEffectivelyPowered();

        if (powered && !wasPowered) {
            playCrashSound();
        }

        wasPowered = powered;
    }

    private void playCrashSound() {
        level.playSound(
                null,
                worldPosition,
                AllSoundEvents.CRASH_CYMBAL.get(),
                SoundSource.RECORDS,
                1.0f,
                1.0f
        );
    }
}
