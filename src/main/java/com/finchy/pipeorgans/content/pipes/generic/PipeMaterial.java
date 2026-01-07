package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.init.AllSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Supplier;

public enum PipeMaterial {
    BASIC(SoundEvents.NOTE_BLOCK_XYLOPHONE),
    WOOD(AllSoundEvents.GROW_WOODEN_PIPE),
    METAL(AllSoundEvents.GROW_METAL_PIPE),
    HAUNTED(AllSoundEvents.GROW_HAUNTED_PIPE);

    private final Supplier<SoundEvent> growSound;

    PipeMaterial(Supplier<SoundEvent> growSound) {
        this.growSound = growSound;
    }

    public SoundEvent getGrowSound() {
        return growSound.get();
    }
}
