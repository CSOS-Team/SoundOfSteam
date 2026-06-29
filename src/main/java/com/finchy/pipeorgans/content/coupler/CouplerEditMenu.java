package com.finchy.pipeorgans.content.coupler;

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

public class CouplerEditMenu extends MenuBase<CouplerBlockEntity> {

    public static final int MARGIN = 8;
    public static final int TITLE_Y = 6;
    public static final int NAME_LABEL_Y = 18;
    public static final int NAME_BOX_Y = 28;
    public static final int FILTER_LABEL_Y = 48;
    public static final int A_BG_X = 8;
    public static final int B_BG_X = 92;
    public static final int FILTER_BG_Y = 58;
    public static final int BUTTONS_Y = 86;

    public static final int FIELD_X = 8;
    public static final int FIELD_W = 160;
    public static final int BOX_H = 14;

    public static final int PLAYER_INV_W = 162;
    public static final int PLAYER_INV_H = 76;
    public static final int INV_GAP = 10;

    private static final int PLAYER_SLOTS = 36;

    private int editIndex;
    public ItemStackHandler ghostInventory; // slot 0 = division A, slot 1 = division B

    public CouplerEditMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public CouplerEditMenu(MenuType<?> type, int id, Inventory inv, CouplerBlockEntity be) {
        super(type, id, inv, be);
    }

    public static CouplerEditMenu create(int id, Inventory inv, CouplerBlockEntity be, int index) {
        be.menuEditIndex = index;
        return new CouplerEditMenu(AllMenuTypes.COUPLER_EDIT_MENU.get(), id, inv, be);
    }

    public CouplerBlockEntity getCouplerBE() {
        return contentHolder;
    }

    public int getEditIndex() {
        return editIndex;
    }

    public ItemStackHandler getGhostInventory() {
        return ghostInventory;
    }

    public static int contentBottom() {
        return BUTTONS_Y + 20;
    }

    public static int playerInvY() {
        return contentBottom() + INV_GAP;
    }

    public static int guiWidth() {
        return Math.max(FIELD_X + FIELD_W + MARGIN, PLAYER_INV_W + 2 * MARGIN);
    }

    public static int guiHeight() {
        return playerInvY() + PLAYER_INV_H + MARGIN;
    }

    public static int playerInvX() {
        return (guiWidth() - PLAYER_INV_W) / 2;
    }

    @Override
    protected CouplerBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof CouplerBlockEntity be) {
            be.readClient(extraData.readNbt());
            extraData.readBoolean();
            be.menuEditIndex = extraData.readVarInt();
            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(CouplerBlockEntity contentHolder) {
        editIndex = contentHolder.menuEditIndex;
        ghostInventory = new ItemStackHandler(2);
        Coupler coupler = contentHolder.getCoupler(editIndex);
        if (coupler != null) {
            if (!coupler.divisionA.isEmpty())
                ghostInventory.setStackInSlot(0, coupler.divisionA.copy());
            if (!coupler.divisionB.isEmpty())
                ghostInventory.setStackInSlot(1, coupler.divisionB.copy());
        }
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(playerInvX(), playerInvY());
        addSlot(new SlotItemHandler(ghostInventory, 0, A_BG_X + 1, FILTER_BG_Y + 1));
        addSlot(new SlotItemHandler(ghostInventory, 1, B_BG_X + 1, FILTER_BG_Y + 1));
    }

    @Override
    protected void saveData(CouplerBlockEntity contentHolder) {
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container == playerInventory;
    }

    private boolean isGhostSlot(int slotId) {
        return slotId >= PLAYER_SLOTS && slotId < PLAYER_SLOTS + 2;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (!isGhostSlot(slotId)) {
            super.clicked(slotId, dragType, clickType, player);
            return;
        }
        if (clickType == ClickType.THROW)
            return;
        int ghostIdx = slotId - PLAYER_SLOTS;
        ItemStack held = getCarried();

        if (clickType == ClickType.CLONE) {
            if (player.isCreative() && held.isEmpty()) {
                ItemStack copy = ghostInventory.getStackInSlot(ghostIdx).copy();
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
        ghostInventory.setStackInSlot(ghostIdx, filter);
        getSlot(slotId).setChanged();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem())
            return ItemStack.EMPTY;

        if (isGhostSlot(index)) {
            ghostInventory.setStackInSlot(index - PLAYER_SLOTS, ItemStack.EMPTY);
            getSlot(index).setChanged();
            return ItemStack.EMPTY;
        }

        for (int g = 0; g < 2; g++) {
            if (ghostInventory.getStackInSlot(g).isEmpty()) {
                ItemStack copy = slot.getItem().copy();
                copy.setCount(1);
                ghostInventory.setStackInSlot(g, copy);
                getSlot(PLAYER_SLOTS + g).setChanged();
                break;
            }
        }
        return ItemStack.EMPTY;
    }
}
