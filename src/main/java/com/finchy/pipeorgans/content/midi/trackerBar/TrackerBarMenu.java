package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllItems;
import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class TrackerBarMenu extends MenuBase<TrackerBarBlockEntity> {

    private Slot inputSlot;

    public TrackerBarMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public TrackerBarMenu(MenuType<?> type, int id, Inventory inv, TrackerBarBlockEntity be) {
        super(type, id, inv, be);
    }

    public static TrackerBarMenu create(int id, Inventory inv, TrackerBarBlockEntity be) {
        return new TrackerBarMenu(AllMenuTypes.TRACKER_BAR_MENU.get(), id, inv, be);
    }

    @Override
    protected TrackerBarBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof TrackerBarBlockEntity trackerBar) {
            trackerBar.readClient(extraData.readNbt());
            return trackerBar;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(TrackerBarBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
        inputSlot = new SlotItemHandler(contentHolder.inventory, 0, 12, 62) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return AllItems.BRASS_BOOT.isIn(stack);
            }
        };

        addSlot(inputSlot);
        addPlayerSlots(81, 175);
    }

    @Override
    protected void saveData(TrackerBarBlockEntity contentHolder) {

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot clickedSlot = getSlot(pIndex);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();
        if (pIndex < 1)
            moveItemStackTo(stack, 1, slots.size(), false);
        else
            moveItemStackTo(stack, 0, 1, false);

        return ItemStack.EMPTY;
    }
}
