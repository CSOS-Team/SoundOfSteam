package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StopMasterMenu extends MenuBase<StopMasterBlockEntity> {

    public StopMasterMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public StopMasterMenu(MenuType<?> type, int id, Inventory inv, StopMasterBlockEntity be) {
        super(type, id, inv, be);
    }

    public static StopMasterMenu create(int id, Inventory inv, StopMasterBlockEntity be) {
        return new StopMasterMenu(AllMenuTypes.STOP_MASTER_MENU.get(), id, inv, be);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    protected StopMasterBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof StopMasterBlockEntity stopMaster) {
            return stopMaster;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(StopMasterBlockEntity contentHolder) {}

    @Override
    protected void addSlots() {
        addPlayerSlots(-1000, 0);
    }

    @Override
    protected void saveData(StopMasterBlockEntity contentHolder) {}
}
