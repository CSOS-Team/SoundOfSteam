package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

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
        inputSlot = new TrackerBarSlot(contentHolder.inventory, 0, 12, 62, stack -> contentHolder.onRollChanged(stack));

        addSlot(inputSlot);
        addPlayerSlots(81, 175);

        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return contentHolder.areButtonsEnabled() ? 1 : 0;
            }

            @Override
            public void set(int pValue) {
                contentHolder.setButtonsEnabled(pValue == 1);
            }
        });
    }

    @Override
    protected void saveData(TrackerBarBlockEntity contentHolder) {

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (pIndex == 0) {
                // shift click from be slot into player inv
                if (!this.moveItemStackTo(originalStack, 1, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                contentHolder.onRollChanged(newStack);

            } else {
                // shift click from player inv into be slot
                if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                contentHolder.onRollChanged(newStack);
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }
}
