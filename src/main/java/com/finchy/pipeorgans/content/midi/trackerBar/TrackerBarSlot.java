package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TrackerBarSlot extends SlotItemHandler {
    private final Consumer<ItemStack> onStackChanged;

    public TrackerBarSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Consumer<ItemStack> onStackChanged) {
        super(itemHandler, index, xPosition, yPosition);
        this.onStackChanged = onStackChanged;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return AllItems.MUSIC_ROLL.isIn(stack);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        super.set(stack);
        onStackChanged.accept(stack);
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {
        super.onQuickCraft(oldStackIn, newStackIn);
        if (ItemStack.matches(oldStackIn, newStackIn)) {
            onStackChanged.accept(newStackIn);
        }
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        super.onTake(pPlayer, pStack);
        onStackChanged.accept(ItemStack.EMPTY);
    }
}
