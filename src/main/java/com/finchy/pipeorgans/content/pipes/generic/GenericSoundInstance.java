package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GenericSoundInstance extends AbstractTickableSoundInstance {

    private boolean active;
    private int keepAlive;
    private float fadeOutVolume = 0.05F;
    private PipeSize size;


    public GenericSoundInstance(PipeSize size, BlockPos worldPosition, SoundEvent soundEvent) {
        super(soundEvent,
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
        this.attenuation = Attenuation.NONE;
    }

    public PipeSize getOctave() {
        return size;
    }

    public void fadeOut() {
        this.active = false;
    }

    public void keepAlive() {
        keepAlive = 2;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void tick() {
        if (Minecraft.getInstance().player == null) {
            stop();
            return;
        }

        //Per player sound distance (client side)
        double maxDistance = ClientConfig.PIPE_ATTENUATION_DISTANCE.get();
        double distSqr = Minecraft.getInstance().player.distanceToSqr(x, y, z);
        double dist = Math.sqrt(distSqr);

        float distanceVolume = dist >= maxDistance
                ? 0f
                : Mth.clamp(1f - (float) (dist / maxDistance), 0f, 1f);

        //Fading logic
        float fadeStep = active ? 0.25f : ClientConfig.PIPE_FADE_SPEED.get().floatValue();

        if (active) {
            // The quick fade in the make sure it's not too abrupt of a start
            fadeOutVolume = Mth.clamp(fadeOutVolume + fadeStep, 0f, 1f);
            keepAlive--;
            if (keepAlive <= 0)
                fadeOut();
        } else {
            fadeOutVolume = Mth.clamp(fadeOutVolume - fadeStep, 0f, 1f);
            if (fadeOutVolume <= 0f) {
                stop();
                return;
            }
        }
        //All this math hurts my brain
        this.volume = fadeOutVolume * distanceVolume;
    }
}