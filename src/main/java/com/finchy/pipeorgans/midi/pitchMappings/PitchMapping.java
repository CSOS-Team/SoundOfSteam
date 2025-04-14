package com.finchy.pipeorgans.midi.pitchMappings;

import net.minecraft.world.item.ItemStack;

public abstract class PitchMapping {

    public abstract String id();

    public abstract ItemStack getStack(int pitch);

}
