package com.finchy.pipeorgans.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class AllSoundTypes {
    public static final SoundType ZIMBLESTERN = new SoundType(
            1.0F, // volume
            1.0F, // pitch
            SoundEvents.ANVIL_BREAK, // break
            AllSoundEvents.ZIMBLESTERN_STEP.get(), // step
            AllSoundEvents.ZIMBLESTERN_PLACE.get(), // place
            SoundEvents.ANVIL_HIT, // hit
            AllSoundEvents.ZIMBLESTERN_PLACE.get()  // fall
    );
}
