package com.finchy.pipeorgans.content.coupler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

// Routes note-link signals from division A to division B
public class Coupler {

    public static final int MAX_NAME_LENGTH = 24;

    public String name = "";
    // Source division filter
    public ItemStack divisionA = ItemStack.EMPTY;
    // Destination division filter
    public ItemStack divisionB = ItemStack.EMPTY;
    public boolean pressed = false;

    public Coupler() {
    }

    public Coupler(String name, ItemStack divisionA, ItemStack divisionB) {
        this.name = name;
        this.divisionA = divisionA;
        this.divisionB = divisionB;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", name);
        tag.put("DivisionA", divisionA.save(new CompoundTag()));
        tag.put("DivisionB", divisionB.save(new CompoundTag()));
        tag.putBoolean("Pressed", pressed);
        return tag;
    }

    public static Coupler fromNbt(CompoundTag tag) {
        Coupler coupler = new Coupler();
        coupler.name = tag.getString("Name");
        coupler.divisionA = ItemStack.of(tag.getCompound("DivisionA"));
        coupler.divisionB = ItemStack.of(tag.getCompound("DivisionB"));
        coupler.pressed = tag.getBoolean("Pressed");
        return coupler;
    }
}
