package com.finchy.pipeorgans.util.midi;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class PitchMapping {

    protected static Map<Integer, Item> pitchMapping;
    public String id;

    public PitchMapping(String id) {
        pitchMapping = new HashMap<>();
        this.id = id;
        setMappings();
        AllPitchMappings.addMapping(id, this);
    }

    protected abstract void setMappings();

    protected void set(int pitch, Item item) {
        pitchMapping.put(pitch, item);
    }

    public ItemStack getStack(int pitch) {
        return new ItemStack(pitchMapping.get(pitch));
    }

}
