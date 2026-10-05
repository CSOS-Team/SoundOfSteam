package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.init.AllSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Supplier;

/**
 * Define the different sounds that can be played when you lengthen a pipe
 */
public enum PipeMaterial {
    BASIC(SoundEvents.NOTE_BLOCK_XYLOPHONE.value()), // The generic sound that Create's Steam Whistles use
    WOOD(AllSoundEvents.GROW_WOODEN_PIPE.get()), // Is wood
    METAL(AllSoundEvents.GROW_METAL_PIPE.get()), // Is metal
    HAUNTED(AllSoundEvents.GROW_HAUNTED_PIPE.get()); // Is spooky

    private final SoundEvent growSound;

    PipeMaterial(SoundEvent growSound) {
        this.growSound = growSound;
    }

    public SoundEvent getGrowSound() {
        return growSound;
    }
}
