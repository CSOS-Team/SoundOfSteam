package com.finchy.pipeorgans.content.traps.tapCymbal;

import com.finchy.pipeorgans.init.AllSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class CymbalTapSoundInstance extends AbstractTickableSoundInstance {

    private final BlockPos pos;
    private final Level level;

    public CymbalTapSoundInstance(Level level, BlockPos pos) {
        super(AllSoundEvents.CYMBAL_TAP.get(), SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.level = level;
        this.pos = pos;

        this.looping = false;
        this.delay = 0;

        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;

        this.volume = 4.0f;
        this.pitch = 1.0f;
        this.attenuation = Attenuation.LINEAR;
    }

    @Override
    public void tick() {
        if (!level.isLoaded(pos)) {
            stop();
        }
    }
}
