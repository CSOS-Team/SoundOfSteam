package com.finchy.pipeorgans.content.traps.bassDrum;

import com.finchy.pipeorgans.init.AllSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class BassRollSoundInstance extends AbstractTickableSoundInstance {

    private final BlockPos pos;
    private final Level level;

    public BassRollSoundInstance(Level level, BlockPos pos) {
        super(AllSoundEvents.BASS_ROLL.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.level = level;
        this.pos = pos;

        this.looping = true;
        this.delay = 0;
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    @Override
    public void tick() {
        if (!level.isLoaded(pos)) {
            stop();
        }
    }
}
