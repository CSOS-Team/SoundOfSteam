package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.ConcurrentHashMap;

public class ClipboardAssistedPlacementData extends ConcurrentHashMap<String, BlockPos> {
    public static final String CLIPBOARD_DATA_KEY = "AssistedPlacement";

    protected static String getBlockID(BlockEntity be) {
        return ForgeRegistries.BLOCKS.getKey(be.getBlockState().getBlock()).toString();
    }

    protected static String getLevelKey(Level level) {
        return level.dimension().location().toString();
    }

    public BlockPos getPosition(BlockEntity be) {
        return this.get(getBlockID(be));
    }

    public void setPosition(BlockEntity be, BlockPos pos) {
        this.put(getBlockID(be), pos);
    }

    public void removePosition(BlockEntity be) {
        this.remove(getBlockID(be));
    }

    public static ClipboardAssistedPlacementData fromClipboardItem(ItemStack clipboard, Level level) {
        CompoundTag tag = clipboard.getOrCreateTag()
                .getCompound(CLIPBOARD_DATA_KEY)
                .getCompound(getLevelKey(level));
        ClipboardAssistedPlacementData data = new ClipboardAssistedPlacementData();
        for (String key : tag.getAllKeys()) {
            CompoundTag posTag = tag.getCompound(key);
            BlockPos pos = BlockPos.of(posTag.getLong("Pos"));
            data.put(key, pos);
        }
        return data;
    }

    public void writeToClipboardItem(ItemStack clipboard, Level level) {
        CompoundTag levelTag = new CompoundTag();
        for (String key : this.keySet()) {
            BlockPos pos = this.get(key);
            CompoundTag entryTag = new CompoundTag();
            entryTag.putLong("Pos", pos.asLong());
            levelTag.put(key, entryTag);
        }
        CompoundTag rootTag = clipboard.getOrCreateTag();
        CompoundTag clipboardDataTag = rootTag.getCompound(CLIPBOARD_DATA_KEY);
        clipboardDataTag.put(getLevelKey(level), levelTag);
        rootTag.put(CLIPBOARD_DATA_KEY, clipboardDataTag);
    }
}
