package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.finchy.pipeorgans.util.MidiUtils.GeneralMidiInstrument;
import com.finchy.pipeorgans.util.MidiUtils.GeneralMidiDrumkit;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TrackerBarMenu extends MenuBase<TrackerBarBlockEntity> {

    private final ContainerData data;

    public ItemStackHandler ghostInventory;

    // todo: need to add ghost inventory to menu

    public TrackerBarMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        this(type, id, inv, (TrackerBarBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(21));
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
        ghostInventory = contentHolder.midiSourceBehaviour.storedGhostInv;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container == playerInventory;
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(81, 181);
        addSlot(new TrackerBarSlot(contentHolder.inventory, 0, 12, 39, stack -> contentHolder.onRollChanged()));
        int slot = 0;
        for (int row=0; row<2; row++) {
            for (int column=0; column<8; column++) {
                addSlot(new SlotItemHandler(ghostInventory, slot++, column*39 + 26, row*20 + 89));
            }
        }
    }

    @Override
    protected void saveData(TrackerBarBlockEntity contentHolder) {
        contentHolder.midiSourceBehaviour.storedGhostInv = ghostInventory;
    }

    public Component getChannelInstrumentName(int channel) {
        if (channel != 9) {
            GeneralMidiInstrument instrument = GeneralMidiInstrument.fromProgram(data.get(channel));
            if (instrument != GeneralMidiInstrument.EMPTY) return Component.translatable(instrument.key);
        } else {
            GeneralMidiDrumkit drumkit = GeneralMidiDrumkit.fromProgram(data.get(channel));
            if (drumkit != GeneralMidiDrumkit.EMPTY) return Component.translatable(drumkit.key);
        }
        return Component.empty();
    }

    public boolean isPlaying() {
        return data.get(16) == 1;
    }

    public int getTickPosition() {
        return data.get(17);
    }

    public int getEndTick() {
        return data.get(18);
    }

    public float getBPM() {
        return (float) data.get(19)/10;
    }

    public boolean getButtonsEnabled() {
        return data.get(20) == 1;
    }

    // PLAYER INVENTORY IS INDICES 0-35
    // MUSIC ROLL SLOT IS INDEX 36
    // GHOST SLOTS ARE INDICES 37-53

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId <= 36) {
            super.clicked(slotId, dragType, clickTypeIn, player);
            return;
        }
        if (clickTypeIn == ClickType.THROW)
            return;

        ItemStack held = getCarried();
        int slot = slotId - 37;
        if (clickTypeIn == ClickType.CLONE) {
            if (player.isCreative() && held.isEmpty()) {
                ItemStack stackInSlot = ghostInventory.getStackInSlot(slot)
                        .copy();
                stackInSlot.setCount(stackInSlot.getMaxStackSize());
                setCarried(stackInSlot);
                return;
            }
            return;
        }

        ItemStack insert;
        if (held.isEmpty()) {
            insert = ItemStack.EMPTY;
        } else {
            insert = held.copy();
            insert.setCount(1);
        }
        ghostInventory.setStackInSlot(slot, insert);
        getSlot(slotId).setChanged();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        // PLAYER INVENTORY IS INDICES 0-35
        // MUSIC ROLL SLOT IS INDEX 36
        // GHOST SLOTS ARE INDICES 37-53
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (pIndex < 36) { // clicked on player slot
                // shift click from player inv into roll slot
                if (!this.moveItemStackTo(originalStack, 36, 37, false)) { // failed to move into the roll slot
                    // unable to move into roll slot, trying ghost slots
                    for (int i = 0; i < ghostInventory.getSlots(); i++) {
                        ItemStack stack = ghostInventory.getStackInSlot(i);
                        if (stack.isEmpty()) {
                            ItemStack copy = originalStack.copy();
                            copy.setCount(1);
                            ghostInventory.insertItem(i, copy, false);
                            getSlot(i + 36).setChanged();
                            break;
                        }
                    }
                    return ItemStack.EMPTY;
                }
                contentHolder.onRollChanged();

            } else if (pIndex == 36) { // clicked on roll slot
                // shift click from roll slot into player inv
                if (!this.moveItemStackTo(originalStack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
                contentHolder.onRollChanged();

            } else { // clicked on ghost slot
                // empty ghost slot that was clicked
                ghostInventory.extractItem(pIndex - 37, 1, false);
                getSlot(pIndex).setChanged();
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
