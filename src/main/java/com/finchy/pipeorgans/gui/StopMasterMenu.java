package com.finchy.pipeorgans.gui;

import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockEntity;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StopMasterMenu extends AbstractContainerMenu {

    public final StopMasterBlockEntity blockEntity;
    private final Level level;

    public StopMasterMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public StopMasterMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(AllMenuTypes.STOP_MASTER_MENU.get(), pContainerId);
        blockEntity = (StopMasterBlockEntity) entity;
        level = inv.player.level();

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, AllBlocks.STOP_MASTER.get());
    }
}
