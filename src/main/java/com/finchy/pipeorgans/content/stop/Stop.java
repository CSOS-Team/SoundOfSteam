package com.finchy.pipeorgans.content.stop;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

// Single stop on stop block
public class Stop {

    public static final int MAX_NAME_LENGTH = 24;
    public static final int MAX_DESCRIPTOR_LENGTH = 48;

    public String name = "";
    public String descriptor = "";
    public ItemStack filter = ItemStack.EMPTY;
    public boolean pressed = false;

    public Stop() {
    }

    public Stop(String name, String descriptor, ItemStack filter) {
        this.name = name;
        this.descriptor = descriptor;
        this.filter = filter;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", name);
        tag.putString("Descriptor", descriptor);
        tag.put("Filter", filter.save(new CompoundTag()));
        tag.putBoolean("Pressed", pressed);
        return tag;
    }

    public static Stop fromNbt(CompoundTag tag) {
        Stop stop = new Stop();
        stop.name = tag.getString("Name");
        stop.descriptor = tag.getString("Descriptor");
        stop.filter = ItemStack.of(tag.getCompound("Filter"));
        stop.pressed = tag.getBoolean("Pressed");
        return stop;
    }
}
