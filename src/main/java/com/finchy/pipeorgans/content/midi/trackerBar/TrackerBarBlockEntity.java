package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import com.finchy.pipeorgans.util.MidiLoadException;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

import javax.sound.midi.*;
import java.util.List;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity implements MenuProvider {

    private boolean buttonsEnabled = false;
    protected final MidiSequencer sequencer;

    public TrackerBarInventory inventory;

    public class TrackerBarInventory extends ItemStackHandler {
        public TrackerBarInventory() {
            super(2);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    }

    public TrackerBarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        inventory = new TrackerBarInventory();
        sequencer = new MidiSequencer(this::handleMidiMessage, link::stopAllNotes);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Tracker Bar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return TrackerBarMenu.create(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void tick() {
        super.tick();
        if (sequencer.isPlaying()) {
            sequencer.tick();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void onRollChanged(ItemStack stack) {
        if (stack.isEmpty())
            sequencer.unloadSequence();
        if (MidiUtils.isMusicRollValid(stack)) {
            try {
                CompoundTag tag = stack.getTag();
                sequencer.loadSequence(tag.getString("File"), tag.getString("Owner"));
                buttonsEnabled = true;
            } catch (MidiLoadException e) {
                buttonsEnabled = false;
            }
        } else {
            buttonsEnabled = false;
        }
    }

    @Override
    public void handleMidiMessage(MidiMessage mm) {
        if (mm instanceof ShortMessage sm && (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm))) {
            handleNote(sm);
        }
    }

    public boolean areButtonsEnabled() {
        return buttonsEnabled;
    }

    public void setButtonsEnabled(boolean value) {
        buttonsEnabled = value;
    }

    public void pressTogglePlayButton() {
        if (sequencer.isSequenceLoaded()) {
            sequencer.toggle();
        }
    }

    public void pressStopButton() {
        sequencer.reset();
        PipeOrgans.LOGGER.info("STOPPED PLAYING");
    }

}
