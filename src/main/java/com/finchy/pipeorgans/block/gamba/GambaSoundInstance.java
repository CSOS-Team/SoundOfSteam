package com.finchy.pipeorgans.block.gamba;

import com.finchy.pipeorgans.block.Generic;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GambaSoundInstance extends AbstractTickableSoundInstance {

    private boolean active;
    private int keepAlive;
    private Generic.GambaWhistleSize size;

    public GambaSoundInstance(Generic.GambaWhistleSize size, BlockPos worldPosition) {
        super((size == Generic.GambaWhistleSize.SMALL ? GAMBA_HIGH :
                size == Generic.GambaWhistleSize.MEDIUM ? GAMBA_MEDIUM :
                size == Generic.GambaWhistleSize.LARGE ? GAMBA_LOW : GAMBA_SUPERHIGH).get(),
                SoundSource.RECORDS,
                SoundInstance.createUnseededRandom());
        this.size = size;
        looping = true;
        active = true;
        volume = 0.05f;
        delay = 0;
        keepAlive();
        Vec3 v = Vec3.atCenterOf(worldPosition);
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Generic.GambaWhistleSize getOctave() { return size; }

    public void fadeOut() { this.active = false; }

    public void keepAlive() {
        keepAlive = 2;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void tick() {
        if (active) {
            volume = Math.min(1, volume + .25f);
            keepAlive--;
            if (keepAlive == 0)
                fadeOut();
            return;

        }
        volume = Math.max(0, volume - .25f);
        if (volume == 0)
            stop();
    }

}
