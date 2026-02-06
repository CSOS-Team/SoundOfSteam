package com.finchy.pipeorgans.content.traps.zimblestern;


import com.finchy.pipeorgans.init.AllSoundEvents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ZimblesternBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {


    public ZimblesternBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0)
            stopTwinkleSound();
        else {

            playTwinkleSound();
        }

    }

        /*
        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(SoundScapes.AmbienceGroup.MILLING, worldPosition, pitch);

         */

    @Nullable
    private ZimblesternSoundInstance twinkleSound;
    private void playTwinkleSound() {
        if (twinkleSound == null || twinkleSound.isStopped()) {
            twinkleSound = new ZimblesternSoundInstance(
                    worldPosition, AllSoundEvents.ZIMBLE_TWINKLE.get()
            );
            Minecraft.getInstance().getSoundManager().play(twinkleSound);
        }

        twinkleSound.keepAlive();
    }

    private void stopTwinkleSound() {
        if (twinkleSound != null) {
            twinkleSound.fadeOut();
            twinkleSound = null;
        }
    }

}
