package com.finchy.pipeorgans.content.pipes.rohrflote;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;


public class RohrfloteSoundInstance extends AbstractTickableSoundInstance {

    private boolean active;
    private int keepAlive;
    private GenericWhistleProperties.WhistleSize size;

    public RohrfloteSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super((size == GenericWhistleProperties.WhistleSize.TINY ? ROHRFLOTE_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? ROHRFLOTE_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? ROHRFLOTE_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? ROHRFLOTE_LOW : ROHRFLOTE_DEEP).get(),
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

    public GenericWhistleProperties.WhistleSize getOctave() { return size; }

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
