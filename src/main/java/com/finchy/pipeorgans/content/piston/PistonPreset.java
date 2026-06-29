package com.finchy.pipeorgans.content.piston;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

// Saved combo for one piston
public class PistonPreset {

    // Relative position (BlockPos#asLong) -> pressed bitmask
    public final Map<Long, Long> states = new HashMap<>();

    public boolean isEmpty() {
        return states.isEmpty();
    }

    public void clear() {
        states.clear();
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Map.Entry<Long, Long> entry : states.entrySet()) {
            CompoundTag e = new CompoundTag();
            e.putLong("Pos", entry.getKey());
            e.putLong("Mask", entry.getValue());
            list.add(e);
        }
        tag.put("States", list);
        return tag;
    }

    public static PistonPreset fromNbt(CompoundTag tag) {
        PistonPreset preset = new PistonPreset();
        ListTag list = tag.getList("States", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            preset.states.put(e.getLong("Pos"), e.getLong("Mask"));
        }
        return preset;
    }
}
