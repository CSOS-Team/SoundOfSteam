package com.finchy.pipeorgans.util.midi;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class PitchMapping {

    protected static Map<Integer, Item> pitchMapping;

    public PitchMapping() {
        pitchMapping = new HashMap<>();
        setMappings();
    }

    protected abstract void setMappings();

    protected void set(int pitch, Item item) {
        pitchMapping.put(pitch, item);
    }

    public ItemStack getStack(int pitch) {
        return new ItemStack(pitchMapping.get(pitch));
    }

}


