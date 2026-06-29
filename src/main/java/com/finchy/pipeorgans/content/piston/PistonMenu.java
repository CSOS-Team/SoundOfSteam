package com.finchy.pipeorgans.content.piston;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PistonMenu extends MenuBase<PistonBlockEntity> {

    public static final int MARGIN = 8;
    public static final int TITLE_Y = 6;

    public static final int COLS = 8;
    public static final int ROWS = 2;
    public static final int BUTTON_SIZE = 24;
    public static final int CELL_GAP = 4;
    public static final int CELL_PITCH = BUTTON_SIZE + CELL_GAP; // 28
    public static final int GRID_X = 8;
    public static final int GRID_TOP = 18;

    public static final int ACTIONS_Y = GRID_TOP + ROWS * CELL_PITCH + 6;
    public static final int ACTION_H = 18;

    public PistonMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public PistonMenu(MenuType<?> type, int id, Inventory inv, PistonBlockEntity be) {
        super(type, id, inv, be);
    }

    public static PistonMenu create(int id, Inventory inv, PistonBlockEntity be) {
        return new PistonMenu(AllMenuTypes.PISTON_MENU.get(), id, inv, be);
    }

    public PistonBlockEntity getPistonBE() {
        return contentHolder;
    }

    public static int gridWidth() {
        return COLS * CELL_PITCH - CELL_GAP;
    }

    public static int guiWidth() {
        return GRID_X + gridWidth() + MARGIN;
    }

    public static int guiHeight() {
        return ACTIONS_Y + ACTION_H + MARGIN;
    }

    public static int cellX(int index) {
        return GRID_X + (index % COLS) * CELL_PITCH;
    }

    public static int cellY(int index) {
        return GRID_TOP + (index / COLS) * CELL_PITCH;
    }

    @Override
    protected PistonBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof PistonBlockEntity be) {
            be.readClient(extraData.readNbt());
            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(PistonBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
    }

    @Override
    protected void saveData(PistonBlockEntity contentHolder) {
    }

    @Override
    public net.minecraft.world.item.ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }
}
