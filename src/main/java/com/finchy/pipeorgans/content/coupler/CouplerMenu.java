package com.finchy.pipeorgans.content.coupler;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CouplerMenu extends MenuBase<CouplerBlockEntity> {

    public static final int MARGIN = 8;
    public static final int TITLE_Y = 6;

    public static final int COLS = 6;
    public static final int ROWS = 2;
    public static final int BUTTON_SIZE = 28;
    public static final int CELL_GAP = 4;
    public static final int CELL_PITCH = BUTTON_SIZE + CELL_GAP;
    public static final int GRID_X = 8;
    public static final int GRID_TOP = 20;

    public CouplerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public CouplerMenu(MenuType<?> type, int id, Inventory inv, CouplerBlockEntity be) {
        super(type, id, inv, be);
    }

    public static CouplerMenu create(int id, Inventory inv, CouplerBlockEntity be) {
        return new CouplerMenu(AllMenuTypes.COUPLER_MENU.get(), id, inv, be);
    }

    public CouplerBlockEntity getCouplerBE() {
        return contentHolder;
    }

    public static int gridWidth() {
        return COLS * CELL_PITCH - CELL_GAP;
    }

    public static int gridHeight() {
        return ROWS * CELL_PITCH - CELL_GAP;
    }

    public static int guiWidth() {
        return GRID_X + gridWidth() + MARGIN;
    }

    public static int guiHeight() {
        return GRID_TOP + gridHeight() + MARGIN;
    }

    public static int cellX(int index) {
        return GRID_X + (index % COLS) * CELL_PITCH;
    }

    public static int cellY(int index) {
        return GRID_TOP + (index / COLS) * CELL_PITCH;
    }

    @Override
    protected CouplerBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof CouplerBlockEntity be) {
            be.readClient(extraData.readNbt());
            extraData.readBoolean();
            extraData.readVarInt();
            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(CouplerBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
        // No item slots in the main coupler GUI.
    }

    @Override
    protected void saveData(CouplerBlockEntity contentHolder) {
    }

    @Override
    public net.minecraft.world.item.ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }
}
