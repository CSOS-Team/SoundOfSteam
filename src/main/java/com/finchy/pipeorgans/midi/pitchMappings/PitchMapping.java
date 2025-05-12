package com.finchy.pipeorgans.midi.pitchMappings;

import net.minecraft.world.item.ItemStack;

public abstract class PitchMapping {

    public abstract String name();

    @Deprecated
    public abstract String description();

    public abstract ItemStack getStack(int pitch);

}
