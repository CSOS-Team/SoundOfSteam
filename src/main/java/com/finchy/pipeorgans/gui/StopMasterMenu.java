package com.finchy.pipeorgans.gui;

import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockEntity;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class StopMasterMenu extends MenuBase<StopMasterBlockEntity> {

    private int channels = 0;

    public StopMasterMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public StopMasterMenu(MenuType<?> type, int id, Inventory inv, StopMasterBlockEntity be) {
        super(type, id, inv, be);
    }

    public boolean getChannel(int channel) {
        int mask = 1 << channel;
        return (channels & mask) != 0;
    }

    public void toggleChannel(int channel) {
        if (channel < 0) return;
        int mask = 1 << channel;
        channels = channels ^ mask;
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
        if (blockEntity instanceof StopMasterBlockEntity be) {
            be.readClient(extraData.readNbt());

            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(StopMasterBlockEntity contentHolder) {}

    @Override
    protected void addSlots() {}

    @Override
    protected void saveData(StopMasterBlockEntity contentHolder) {
        contentHolder.setChannels(channels);
    }
}
