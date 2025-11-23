package com.finchy.pipeorgans.content.midi.rollpuncher;

import com.finchy.pipeorgans.init.AllItems;
import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class RollPuncherMenu extends MenuBase<RollPuncherBlockEntity> {

    private Slot inputSlot;
    private Slot outputSlot;

    public RollPuncherMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public RollPuncherMenu(MenuType<?> type, int id, Inventory inv, RollPuncherBlockEntity be) {
        super(type, id, inv, be);
    }

    public static RollPuncherMenu create (int id, Inventory inv, RollPuncherBlockEntity be) {
        return new RollPuncherMenu(AllMenuTypes.ROLL_PUNCHER_MENU.get(), id, inv, be);
    }

    public boolean canWrite() {
        return inputSlot.hasItem() && !outputSlot.hasItem();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot clickedSlot = getSlot(pIndex);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();
        if (pIndex < 2)
            moveItemStackTo(stack, 2, slots.size(), true);
        else
            moveItemStackTo(stack, 0, 1, false);

        return ItemStack.EMPTY;
    }

    @Override
    protected RollPuncherBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof RollPuncherBlockEntity rollPuncher) {
            rollPuncher.readClient(extraData.readNbt());
            return rollPuncher;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(RollPuncherBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
        inputSlot = new SlotItemHandler(contentHolder.inventory, 0, 21, 59) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.PAPER.asItem()) || AllItems.MUSIC_ROLL.isIn(stack);
            }
        };

        outputSlot = new SlotItemHandler(contentHolder.inventory, 1, 166, 59) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };

        addSlot(inputSlot);
        addSlot(outputSlot);

        // player Slots
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(player.getInventory(), col + row * 9 + 9, 38 + col * 18, 107 + row * 18));
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(player.getInventory(), hotbarSlot, 38 + hotbarSlot * 18, 165));
        }
    }

    @Override
    protected void saveData(RollPuncherBlockEntity contentHolder) {
    }
}
