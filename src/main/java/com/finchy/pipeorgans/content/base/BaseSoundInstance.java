package com.finchy.pipeorgans.content.base;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class BaseSoundInstance extends AbstractTickableSoundInstance {

    private boolean active;
    private int keepAlive;

    public BaseSoundInstance(BlockPos worldPosition, SoundEvent soundEvent) {
        super(soundEvent,
                SoundSource.RECORDS,
                SoundInstance.createUnseededRandom());
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
    //Mmmmmmm... It sounds like steam
}