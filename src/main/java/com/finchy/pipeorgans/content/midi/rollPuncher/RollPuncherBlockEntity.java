package com.finchy.pipeorgans.content.midi.rollPuncher;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RollPuncherBlockEntity extends SmartBlockEntity implements MenuProvider, IInteractionChecker {

    public RollPuncherInventory inventory;
    public boolean isUploading;
    public String uploadingMidi;
    public float uploadingProgress;
    public boolean sendUpdate;

    public class RollPuncherInventory extends ItemStackHandler {
        public RollPuncherInventory() {
            super(2);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    }

    public RollPuncherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new RollPuncherInventory();
        uploadingMidi = null;
        uploadingProgress = 0;
    }

    public void sendToMenu(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(getBlockPos());
        buffer.writeNbt(getUpdateTag());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        super.read(tag, clientPacket);

        if (!clientPacket) return;
        if (tag.contains("Uploading")) {
            isUploading = true;
            uploadingMidi = tag.getString("Midi");
            uploadingProgress = tag.getFloat("Progress");
        } else {
            isUploading = false;
            uploadingMidi = null;
            uploadingProgress = 0;
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.put("Inventory", inventory.serializeNBT());
        super.write(tag, clientPacket);

        if (clientPacket && isUploading) {
            tag.putBoolean("Uploading", true);
            tag.putString("Midi", uploadingMidi);
            tag.putFloat("Progress", uploadingProgress);
        }
    }

    @Override
    public void tick() {
        if (sendUpdate) {
            sendUpdate = false;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 6);
        }
    }

    public void startUpload(String midi) {
        isUploading = true;
        uploadingProgress = 0;
        uploadingMidi = midi;
        sendUpdate = true;
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void finishUpload() {
        isUploading = false;
        uploadingProgress = 0;
        uploadingMidi = null;
        sendUpdate = true;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return RollPuncherMenu.create(pContainerId, pPlayerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.roll_puncher.title");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

}
