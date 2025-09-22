package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.init.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class KeyboardRelayMenu extends GhostItemMenu<KeyboardRelayBlockEntity> {

    public KeyboardRelayMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public KeyboardRelayMenu(MenuType<?> type, int id, Inventory inv, KeyboardRelayBlockEntity be) {
        super(type, id, inv, be);
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return contentHolder.midiSourceBehaviour.storedGhostInv;
    }

    @Override
    protected boolean allowRepeats() {
        return true;
    }

    public static KeyboardRelayMenu create(int id, Inventory inv, KeyboardRelayBlockEntity be) {
        return new KeyboardRelayMenu(AllMenuTypes.KEYBOARD_RELAY_MENU.get(), id, inv, be);
    }

    @Override
    protected KeyboardRelayBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof KeyboardRelayBlockEntity kbr) {
            kbr.readClient(extraData.readNbt());
            return kbr;
        }
        return null;
    }

    @Override
    protected void saveData(KeyboardRelayBlockEntity contentHolder) {
        contentHolder.midiSourceBehaviour.storedGhostInv = ghostInventory;
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(12, 161);
        int x = 31;
        int y = 24;
        int slot = 0;

        for (int column=0; column<4; column++) {
            for (int row=0; row<4; row++) {
                addSlot(new SlotItemHandler(ghostInventory, slot++, column*42 + x, row*20 + y));
            }
        }
    }

}
