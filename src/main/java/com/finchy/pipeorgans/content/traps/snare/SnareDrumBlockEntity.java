package com.finchy.pipeorgans.content.traps.snare;

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

public class SnareDrumBlockEntity extends SmartBlockEntity {

    private boolean wasPowered = false;

    @Nullable
    private SnareRollSoundInstance rollSound;

    public SnareDrumBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
                .getOptionalValue(SnareDrumBlock.POWERED)
                .orElse(false);
    }

    // windchest-based powered state
    protected boolean isEffectivelyPowered() {
        if (level == null)
            return false;

        BlockState state = getBlockState();
        BlockPos attachedPos =
                worldPosition.relative(SnareDrumBlock.getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);

        boolean windchestActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            windchestActive = windchest.isMasterActive(
                    level,
                    attachedState.getValue(SnareDrumBlock.FACING),
                    attachedPos
            );
        }

        return windchestActive && isRedstonePowered();
    }

    private PercussionMode getMode() {
        return getBlockState().getValue(SnareDrumBlock.MODE);
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
                    AllSoundEvents.SNARE_TAP.get(),
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

        rollSound = new SnareRollSoundInstance(level, worldPosition);
        Minecraft.getInstance().getSoundManager().play(rollSound);
    }

    private void stopRoll() {
        if (rollSound == null)
            return;

        Minecraft.getInstance()
                .getSoundManager()
                .stop(rollSound);

        rollSound = null;
    }


    @Override
    public void remove() {
        super.remove();
        stopRoll();
    }
}
