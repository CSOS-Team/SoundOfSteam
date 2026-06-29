package com.finchy.pipeorgans.content.console;

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

import java.util.ArrayList;
import java.util.List;

public class OrganConsoleMenu extends MenuBase<OrganConsoleBlockEntity> {

    public static final int MARGIN = 8;
    public static final int TITLE_Y = 6;
    public static final int SLOT = 18;

    // Horizontal filter row near the top. Each section has an OUTPUT and an INPUT filter, grouped together.
    public static final int FILTERS_X = 8;       // bg-left of the first filter group
    public static final int FILTER_ROW_Y = 18;   // bg-top of the filter row
    public static final int GROUP_GAP = 6;       // gap between adjacent section groups

    // Stacked keyboards below the filter row
    public static final int KEYBOARDS_TOP = FILTER_ROW_Y + SLOT + 8; // 44
    public static final int ROW_PITCH = 22;      // KEY_H + a tiny gap between manuals
    public static final int KEY_H = 18;
    public static final int KEYBOARD_X = 8;
    public static final int MANUAL_KEY_W = 5;
    public static final int PEDAL_KEY_W = 8;

    public static final int PLAYER_INV_W = 162; // 9 * 18
    public static final int PLAYER_INV_H = 76;  // 3 rows + gap + hotbar
    public static final int INV_GAP = 10;

    private static final int PLAYER_SLOTS = 36;

    private boolean pedalboardMode;
    private int manualCount;
    private int[] ghostSlotIndices;

    public ItemStackHandler ghostInventory;

    public OrganConsoleMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public OrganConsoleMenu(MenuType<?> type, int id, Inventory inv, OrganConsoleBlockEntity be) {
        super(type, id, inv, be);
    }

    public static OrganConsoleMenu create(int id, Inventory inv, OrganConsoleBlockEntity be) {
        return new OrganConsoleMenu(AllMenuTypes.ORGAN_CONSOLE_MENU.get(), id, inv, be);
    }

    public boolean isPedalboardMode() {
        return pedalboardMode;
    }

    public OrganConsoleBlockEntity getConsole() {
        return contentHolder;
    }

    public int getManualCount() {
        return manualCount;
    }

    // Layout helpers
    public static int keyboardRows(boolean pedalboard, int manualCount) {
        return pedalboard ? 1 : Math.max(1, manualCount);
    }

    // Number of filter groups
    public static int emitFilterCount(boolean pedalboard, int manualCount) {
        return keyboardRows(pedalboard, manualCount);
    }

    public static final int GROUP_W = 2 * SLOT + GROUP_GAP;

    public static int groupBgX(int i) {
        return FILTERS_X + i * GROUP_W;
    }

    public static int outputBgX(int i) {
        return groupBgX(i);
    }

    public static int inputBgX(int i) {
        return groupBgX(i) + SLOT;
    }

    public static int sectionForRow(boolean pedalboard, int row) {
        return pedalboard ? OrganConsoleBlockEntity.PEDAL_SECTION : row;
    }

    public static int rowTop(int row) {
        return KEYBOARDS_TOP + row * ROW_PITCH;
    }

    public static int keyWidth(boolean pedalboard) {
        return pedalboard ? PEDAL_KEY_W : MANUAL_KEY_W;
    }

    public static int keyCountFor(boolean pedalboard) {
        return pedalboard ? OrganConsoleBlockEntity.PEDAL_KEY_COUNT : OrganConsoleBlockEntity.MANUAL_KEY_COUNT;
    }

    public static int keyboardWidth(boolean pedalboard) {
        return keyCountFor(pedalboard) * keyWidth(pedalboard);
    }

    public static int contentBottom(boolean pedalboard, int manualCount) {
        return KEYBOARDS_TOP + keyboardRows(pedalboard, manualCount) * ROW_PITCH;
    }

    public static int playerInvY(boolean pedalboard, int manualCount) {
        return contentBottom(pedalboard, manualCount) + INV_GAP;
    }

    public static int filtersWidth(boolean pedalboard, int manualCount) {
        return emitFilterCount(pedalboard, manualCount) * GROUP_W - GROUP_GAP;
    }

    public static int guiWidth(boolean pedalboard, int manualCount) {
        int byKeyboard = KEYBOARD_X + keyboardWidth(pedalboard) + MARGIN;
        int byFilters = FILTERS_X + filtersWidth(pedalboard, manualCount) + MARGIN;
        return Math.max(Math.max(byKeyboard, byFilters), PLAYER_INV_W + 2 * MARGIN);
    }

    public static int guiHeight(boolean pedalboard, int manualCount) {
        return playerInvY(pedalboard, manualCount) + PLAYER_INV_H + MARGIN;
    }

    public static int playerInvX(boolean pedalboard, int manualCount) {
        return (guiWidth(pedalboard, manualCount) - PLAYER_INV_W) / 2;
    }

    // MenuBase wiring
    @Override
    protected OrganConsoleBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof OrganConsoleBlockEntity console) {
            console.readClient(extraData.readNbt());
            console.menuPedalboardMode = extraData.readBoolean();
            console.menuManualCount = extraData.readVarInt();
            return console;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(OrganConsoleBlockEntity contentHolder) {
        ghostInventory = contentHolder.getFilterInventory();
        pedalboardMode = contentHolder.menuPedalboardMode;
        manualCount = contentHolder.menuManualCount;
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(playerInvX(pedalboardMode, manualCount), playerInvY(pedalboardMode, manualCount));

        List<Integer> ghost = new ArrayList<>();
        int groups = emitFilterCount(pedalboardMode, manualCount);
        for (int i = 0; i < groups; i++) {
            int section = sectionForRow(pedalboardMode, i);
            // output filter
            addSlot(new SlotItemHandler(ghostInventory, OrganConsoleBlockEntity.outputSlot(section),
                    outputBgX(i) + 1, FILTER_ROW_Y + 1));
            ghost.add(OrganConsoleBlockEntity.outputSlot(section));
            // input filter
            addSlot(new SlotItemHandler(ghostInventory, OrganConsoleBlockEntity.inputSlot(section),
                    inputBgX(i) + 1, FILTER_ROW_Y + 1));
            ghost.add(OrganConsoleBlockEntity.inputSlot(section));
        }

        ghostSlotIndices = ghost.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    protected void saveData(OrganConsoleBlockEntity contentHolder) {
        // Nothing to persist.
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container == playerInventory;
    }

    // Filter-only ghost slot interaction (mirrors TrackerBarMenu)
    private boolean isGhostSlot(int slotId) {
        return slotId >= PLAYER_SLOTS && (slotId - PLAYER_SLOTS) < ghostSlotIndices.length;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (!isGhostSlot(slotId)) {
            super.clicked(slotId, dragType, clickType, player);
            return;
        }
        if (clickType == ClickType.THROW)
            return;

        int ghostIdx = ghostSlotIndices[slotId - PLAYER_SLOTS];
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
            int ghostIdx = ghostSlotIndices[index - PLAYER_SLOTS];
            ghostInventory.setStackInSlot(ghostIdx, ItemStack.EMPTY);
            getSlot(index).setChanged();
            return ItemStack.EMPTY;
        }

        ItemStack original = slot.getItem();
        for (int ordinal = 0; ordinal < ghostSlotIndices.length; ordinal++) {
            int ghostIdx = ghostSlotIndices[ordinal];
            if (ghostInventory.getStackInSlot(ghostIdx).isEmpty()) {
                ItemStack copy = original.copy();
                copy.setCount(1);
                ghostInventory.setStackInSlot(ghostIdx, copy);
                getSlot(PLAYER_SLOTS + ordinal).setChanged();
                break;
            }
        }
        return ItemStack.EMPTY;
    }
}
