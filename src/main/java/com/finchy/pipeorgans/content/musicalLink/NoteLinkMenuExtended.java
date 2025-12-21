package com.finchy.pipeorgans.content.musicalLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllMenuTypes;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NoteLinkMenuExtended extends GhostItemMenu<NoteLinkBlockEntity> {
    public static final int GHOST_INVENTORY_START = 36;

    public static final int KEY_SLOT = 0;
    public static final int KEY_SLOT_rX = 58;
    public static final int KEY_SLOT_rY = 24;

    public static final int KEY_FREQ_SLOT = 1;
    public static final int KEY_FREQ_SLOT_rX = 204; // change!
    public static final int KEY_FREQ_SLOT_rY = 35; // change!
    public static final int PITCH_FREQ_SLOT = 2;
    public static final int PITCH_FREQ_SLOT_rX = 204; // change!
    public static final int PITCH_FREQ_SLOT_rY = 57; // change!

    public static final int PLAYER_INV_X = 24;
    public static final int PLAYER_INV_Y = NoteLinkScreen.GUI_HEIGHT + 22;

    protected ItemStack key = ItemStack.EMPTY;
    protected PipePitch pitch = PipePitch.DEFAULT;

    public NoteLinkMenuExtended(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public NoteLinkMenuExtended(MenuType<?> type, int id, Inventory inv, NoteLinkBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

//    public static NoteLinkMenuExtended create(int id, Inventory inv, NoteLinkBlockEntity be) {
//        return new NoteLinkMenuExtended(AllMenuTypes.NOTE_LINK_MENU.get(), id, inv, be);
//    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(3){
            @Override
            protected void onContentsChanged(int slot) {
                if (clientSide())
                    return;
                debugLog("onContentsChanged", String.format("Ghost inventory slot %d changed", slot));
                super.onContentsChanged(slot);
                if (slot == 0)
                    setKey(this.getStackInSlot(0));

            }


        };
    }

    public void setKey(ItemStack keyStack) {
        this.key = keyStack;
        this.key.setCount(1);
        ghostInventory.setStackInSlot(KEY_FREQ_SLOT, key);
    }


    public void setPitch(PipePitch pitch) {
        this.pitch = pitch;
        ItemStack pitchStack = pitch.getMappedItem();
        ghostInventory.setStackInSlot(PITCH_FREQ_SLOT, pitchStack);
    }

    public void setOctave(int octaveOrdinal) {
        setPitch(new PipePitch(this.pitch.pitchClass(), PipePitch.Octave.values()[octaveOrdinal]));
    }

    public void setPitchClass(int pitchClassOrdinal) {
        setPitch(new PipePitch(PipePitch.PitchClass.values()[pitchClassOrdinal], this.pitch.octave()));
    }

    @Override
    public boolean canDragTo(Slot slotIn) {
        if (clientSide())
            return false;
        debugLog("canDragTo", String.format("Checking if can drag to slot index: %d", slotIn.index));
        return slotIn.index < (GHOST_INVENTORY_START + KEY_FREQ_SLOT) && super.canDragTo(slotIn);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        if (clientSide())
            return false;
        debugLog("canTakeItemForPickAll", String.format("Checking if can take item %s from slot index: %d", stack, slotIn.index));
        if (slotIn.index == (GHOST_INVENTORY_START + KEY_FREQ_SLOT) || slotIn.index == (GHOST_INVENTORY_START + PITCH_FREQ_SLOT))
            return false;
        return super.canTakeItemForPickAll(stack, slotIn);
    }

    @Override
    public void clearContents() {
        contentHolder.reset();
        super.clearContents();
        debugLog("clearContents", "Cleared contents and reset content holder");
    }

    @Override
    protected void initAndReadInventory(NoteLinkBlockEntity contentHolder) {
        super.initAndReadInventory(contentHolder);
        if (clientSide()) return;
        ghostInventory.setStackInSlot(KEY_SLOT, contentHolder.getKey().copy());
        setKey(contentHolder.getKey().copy());
        setPitch(contentHolder.getPitch().copy());
        debugLog("initAndReadInventory", "Initialized ghost inventory from content holder");
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        if (clientSide())
            return ItemStack.EMPTY;
        debugLog("quickMoveStack", String.format("Attempting to quick move stack from slot index: %d", index));
        if (index == (GHOST_INVENTORY_START + KEY_FREQ_SLOT) || index == (GHOST_INVENTORY_START + PITCH_FREQ_SLOT))
            return ItemStack.EMPTY;
        if (index == (GHOST_INVENTORY_START + KEY_SLOT)) {
            ghostInventory.setStackInSlot((GHOST_INVENTORY_START + KEY_FREQ_SLOT), ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }
        if (index < 36) {
            ItemStack stackToInsert = playerInventory.getItem(index);
            ItemStack copy = stackToInsert.copy();
            copy.setCount(1);
            ghostInventory.setStackInSlot(KEY_SLOT, copy);
            debugLog("quickMoveStack", String.format("Moved stack %s to ghost inventory key slot", copy));
            return stackToInsert;
        }
        return ItemStack.EMPTY;
    }

//    @Override
//    protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
//        debugLog("moveItemStackTo", String.format("Attempting to move stack %s from %d to %d (reverse: %b)", pStack, pStartIndex, pEndIndex, pReverseDirection));
//        if (pStartIndex >= GHOST_INVENTORY_START + KEY_FREQ_SLOT || pEndIndex >= GHOST_INVENTORY_START + KEY_FREQ_SLOT)
//            return false;
//        return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
//    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected NoteLinkBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof NoteLinkBlockEntity nlb) {
            nlb.readClient(extraData.readNbt());
            return nlb;
        }
        return null;
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(PLAYER_INV_X, PLAYER_INV_Y);
        addSlot(new SlotItemHandler(ghostInventory, KEY_SLOT, KEY_SLOT_rX, KEY_SLOT_rY));
        addSlot(displayOnlySlot(KEY_FREQ_SLOT, KEY_FREQ_SLOT_rX, KEY_FREQ_SLOT_rY));
        addSlot(displayOnlySlot(PITCH_FREQ_SLOT, PITCH_FREQ_SLOT_rX, PITCH_FREQ_SLOT_rY));
    }

    protected SlotItemHandler displayOnlySlot(int index, int x, int y) {
        return new SlotItemHandler(ghostInventory, index, x, y) {
            @Override
            public boolean mayPickup(Player playerIn) {
                return false;
            }
        };
    }

    @Override
    protected void saveData(NoteLinkBlockEntity contentHolder) {
        contentHolder.setKey(key);
        contentHolder.setPitch(pitch);
    }

    private void debugLog(String source, String message) {
        PipeOrgans.LOGGER.debug("NLMDBG [{}] {}", source, message);
        StringBuilder state = new StringBuilder();
        state.append("\tID: ").append(this.containerId).append(",\n");
        state.append("\tPlayer: ").append(playerInventory.player.getDisplayName().getString()).append(",\n");
        state.append("\tDomain: ").append(clientSide() ? "Client" : "Server").append(",\n");
        state.append("\tContent Holder Pos: ").append(contentHolder.getBlockPos()).append(",\n");
        state.append("\tKey: ").append(key).append(",\n");
        state.append("\tPitch: ").append(pitch).append(",\n");
        state.append("\tGhost Inventory: \n");
        for (int i = 0; i < ghostInventory.getSlots(); i++) {
            state.append("\t\tSlot ").append(i).append(": ").append(ghostInventory.getStackInSlot(i)).append(",\n");
        }
        PipeOrgans.LOGGER.debug("NLMDBG State:\n{}", state.toString());
    }

    private boolean clientSide() {
        return player.level().isClientSide;
    }
}
