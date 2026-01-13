package com.finchy.pipeorgans.content.traps.bassDrum;

import com.finchy.pipeorgans.content.pipes.generic.PercussionMode;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.init.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class BassDrumBlockEntity extends SmartBlockEntity {

    private boolean wasPowered = false;

    @Nullable
    private BassRollSoundInstance rollSound;

    public BassDrumBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void onLoad() {
        super.onLoad();
        wasPowered = isEffectivelyPowered();
    }

    // raw redstone state
    protected boolean isRedstonePowered() {
        return getBlockState()
                .getOptionalValue(BassDrumBlock.POWERED)
                .orElse(false);
    }

    // windchest-based powered state
    protected boolean isEffectivelyPowered() {
        if (level == null)
            return false;

        BlockState state = getBlockState();
        BlockPos attachedPos =
                worldPosition.relative(BassDrumBlock.getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);

        boolean windchestActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            windchestActive = windchest.isMasterActive(
                    level,
                    attachedState.getValue(BassDrumBlock.FACING),
                    attachedPos
            );
        }

        return windchestActive && isRedstonePowered();
    }

    private PercussionMode getMode() {
        return getBlockState().getValue(BassDrumBlock.MODE);
    }

    @Override
    public void tick() {
        if (level == null)
            return;

        boolean powered = isEffectivelyPowered();
        PercussionMode mode = getMode();


        //Tap
        if (!level.isClientSide && powered && !wasPowered && mode == PercussionMode.TAP) {
            level.playSound(
                    null,
                    worldPosition,
                    AllSoundEvents.BASS_TAP.get(),
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
            );
        }

        //Roll
        if (level.isClientSide) {
            if (powered && mode == PercussionMode.ROLL) {
                startRoll();
            } else {
                stopRoll();
            }
        }

        wasPowered = powered;
    }

    //Roll Control

    private void startRoll() {
        if (rollSound != null)
            return;

        rollSound = new BassRollSoundInstance(level, worldPosition);
        Minecraft.getInstance().getSoundManager().play(rollSound);
    }

    private void stopRoll() {
        if (rollSound == null)
            return;

        Minecraft.getInstance()
                .getSoundManager()
                .stop(rollSound);

        // Play fade-out sound when roll stops
        Minecraft.getInstance().getSoundManager().play(
                new net.minecraft.client.resources.sounds.SimpleSoundInstance(
                        AllSoundEvents.BASS_FADE.get(),
                        SoundSource.BLOCKS,
                        1.0f,
                        1.0f,
                        level.random,
                        worldPosition
                )
        );

        rollSound = null;
    }


    private void playBassFade() {
        Minecraft.getInstance().getSoundManager().play(
                net.minecraft.client.resources.sounds.SimpleSoundInstance.forLocalAmbience(
                        AllSoundEvents.BASS_FADE.get(),
                        1.0f,
                        1.0f
                )
        );
    }



    @Override
    public void remove() {
        super.remove();
        stopRoll();
    }
}
