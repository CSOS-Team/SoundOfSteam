package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TrackerBarMenu extends MenuBase<TrackerBarBlockEntity> {

    private final ContainerData data;

    // todo: need to add ghost inventory to menu (FUUUUUUCKK)

    public TrackerBarMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        this(type, id, inv, (TrackerBarBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public TrackerBarMenu(MenuType<?> type, int id, Inventory inv, TrackerBarBlockEntity be, ContainerData data) {
        super(type, id, inv, be);
        this.data = data;
        addDataSlots(data);
    }

    public static TrackerBarMenu create(int id, Inventory inv, TrackerBarBlockEntity be, ContainerData data) {
        return new TrackerBarMenu(AllMenuTypes.TRACKER_BAR_MENU.get(), id, inv, be, data);
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
        addSlot(new TrackerBarSlot(contentHolder.inventory, 0, 12, 62, stack -> contentHolder.onRollChanged(stack)));
        addPlayerSlots(81, 225);
    }

    @Override
    protected void saveData(TrackerBarBlockEntity contentHolder) {

    }

    public boolean isPlaying() {
        return data.get(0) == 1;
    }

    public int getTickPosition() {
        return data.get(1);
    }

    public float getBPM() {
        return (float) data.get(2) /10;
    }

    public boolean getButtonsEnabled() {
        return data.get(3) == 1;
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
