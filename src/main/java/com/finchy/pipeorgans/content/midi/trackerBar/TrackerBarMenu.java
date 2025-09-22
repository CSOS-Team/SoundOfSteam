package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class TrackerBarMenu extends MenuBase<TrackerBarBlockEntity> {

    private final ContainerData data;

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
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(81, 225);
        addSlot(new TrackerBarSlot(contentHolder.inventory, 0, 12, 62, stack -> contentHolder.onRollChanged()));
        int slot = 0;
        for (int row=0; row<2; row++) {
            for (int column=0; column<8; column++) {
                addSlot(new SlotItemHandler(contentHolder.midiSourceBehaviour.storedGhostInv, slot++, column*39 + 26, row*20 + 133));
            }
        }
    }

    @Override
    protected void saveData(TrackerBarBlockEntity contentHolder) {

    }

    public Component getChannelInstrument(int channel) {
        String translatable = MidiUtils.GeneralMidiInstrument.fromProgram(data.get(channel)).key;
        return Component.literal(channel+1 + ": ").append(Component.translatable(translatable));
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

    public String getLoadedFilename() {
        return contentHolder.getCurrentMidi();
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

            if (pIndex >= 36) {
                // shift click from be slot into player inv
                if (!this.moveItemStackTo(originalStack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                contentHolder.onRollChanged();

            } else {
                // shift click from player inv into be slot
                if (!this.moveItemStackTo(originalStack, 36, 53, false)) {
                    return ItemStack.EMPTY;
                }
                contentHolder.onRollChanged();
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
