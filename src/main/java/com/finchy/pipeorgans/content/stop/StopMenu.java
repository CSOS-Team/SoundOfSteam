package com.finchy.pipeorgans.content.stop;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class StopMenu extends MenuBase<StopBlockEntity> {

    // Shared layout (GUI-relative)
    public static final int MARGIN = 8;
    public static final int TITLE_Y = 6;

    public static final int DIVISION_BG_X = 8;
    public static final int DIVISION_BG_Y = 18;

    public static final int COLS = 6;
    public static final int ROWS = 3;
    public static final int BUTTON_SIZE = 28;
    public static final int CELL_GAP = 4;
    public static final int CELL_PITCH = BUTTON_SIZE + CELL_GAP; // 32
    public static final int GRID_X = 8;
    public static final int GRID_TOP = 44;

    public static final int PLAYER_INV_W = 162;
    public static final int PLAYER_INV_H = 76;
    public static final int INV_GAP = 10;

    private static final int PLAYER_SLOTS = 36;

    // No field initializers for values set during super() construction.
    public ItemStackHandler ghostInventory;

    public StopMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public StopMenu(MenuType<?> type, int id, Inventory inv, StopBlockEntity be) {
        super(type, id, inv, be);
    }

    public static StopMenu create(int id, Inventory inv, StopBlockEntity be) {
        return new StopMenu(AllMenuTypes.STOP_MENU.get(), id, inv, be);
    }

    public StopBlockEntity getStopBE() {
        return contentHolder;
    }

    // Layout helpers

    public static int gridWidth() {
        return COLS * CELL_PITCH - CELL_GAP;
    }

    public static int gridHeight() {
        return ROWS * CELL_PITCH - CELL_GAP;
    }

    public static int contentBottom() {
        return GRID_TOP + gridHeight();
    }

    public static int playerInvY() {
        return contentBottom() + INV_GAP;
    }

    public static int guiWidth() {
        return Math.max(GRID_X + gridWidth() + MARGIN, PLAYER_INV_W + 2 * MARGIN);
    }

    public static int guiHeight() {
        return playerInvY() + PLAYER_INV_H + MARGIN;
    }

    public static int playerInvX() {
        return (guiWidth() - PLAYER_INV_W) / 2;
    }

    public static int cellX(int index) {
        return GRID_X + (index % COLS) * CELL_PITCH;
    }

    public static int cellY(int index) {
        return GRID_TOP + (index / COLS) * CELL_PITCH;
    }

    // MenuBase wiring

    @Override
    protected StopBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof StopBlockEntity be) {
            be.readClient(extraData.readNbt());
            extraData.readBoolean(); // edit mode (unused for main)
            extraData.readVarInt();  // edit index (unused for main)
            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(StopBlockEntity contentHolder) {
        ghostInventory = contentHolder.getDivisionInv();
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(playerInvX(), playerInvY());
        addSlot(new SlotItemHandler(ghostInventory, 0, DIVISION_BG_X + 1, DIVISION_BG_Y + 1));
    }

    @Override
    protected void saveData(StopBlockEntity contentHolder) {
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container == playerInventory;
    }

    private boolean isGhostSlot(int slotId) {
        return slotId >= PLAYER_SLOTS && slotId < PLAYER_SLOTS + 1;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (!isGhostSlot(slotId)) {
            super.clicked(slotId, dragType, clickType, player);
            return;
        }
        if (clickType == ClickType.THROW)
            return;

        ItemStack held = getCarried();
        if (clickType == ClickType.CLONE) {
            if (player.isCreative() && held.isEmpty()) {
                ItemStack copy = ghostInventory.getStackInSlot(0).copy();
                if (!copy.isEmpty()) {
                    copy.setCount(copy.getMaxStackSize());
                    setCarried(copy);
                }
            }
            return;
        }

        ItemStack filter;
        if (held.isEmpty()) {
            filter = ItemStack.EMPTY;
        } else {
            filter = held.copy();
            filter.setCount(1);
        }
        ghostInventory.setStackInSlot(0, filter);
        getSlot(slotId).setChanged();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem())
            return ItemStack.EMPTY;

        if (isGhostSlot(index)) {
            ghostInventory.setStackInSlot(0, ItemStack.EMPTY);
            getSlot(index).setChanged();
            return ItemStack.EMPTY;
        }

        if (ghostInventory.getStackInSlot(0).isEmpty()) {
            ItemStack copy = slot.getItem().copy();
            copy.setCount(1);
            ghostInventory.setStackInSlot(0, copy);
            getSlot(PLAYER_SLOTS).setChanged();
        }
        return ItemStack.EMPTY;
    }
}
