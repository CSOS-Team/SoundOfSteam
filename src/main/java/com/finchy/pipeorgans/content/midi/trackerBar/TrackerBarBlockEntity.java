package com.finchy.pipeorgans.content.midi.trackerBar;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity implements MenuProvider {

    private boolean buttonsEnabled = false;
    public boolean sendUpdate = false;

    private List<Queue<MidiEvent>> currentSequence = null;

    private boolean playing = false;
    private int tickPosition = 0;
    private int ppq;
    private int tickStep = 1;
    public double bpm;
    public int endTick = 1;
    public List<String> channelInstruments;

    private String currentMidi = "";
    private String currentMidiOwner = "";

    public TrackerBarInventory inventory;

    public class TrackerBarInventory extends ItemStackHandler {
        public TrackerBarInventory() {
            super(1);
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
        channelInstruments = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            channelInstruments.add(MidiUtils.GeneralMidiInstrument.EMPTY.name);
        }
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
        if (playing) {
            tickSequencer();
            notifyUpdate();
        }
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
    }

    // SEQUENCER

    public void loadSequence(String file, String owner) throws MidiLoadException {
        Sequence sequence = MidiFileParser.getSequenceFromFile(file, owner);

        currentSequence = MidiFileParser.parseMidiEvents(sequence);
        ppq = sequence.getResolution();
        MidiFileParser.initialParse(sequence, this::setChannelInstrument, this::setTempo);
        currentMidi = file;
        currentMidiOwner = owner;
    }

    public void unloadSequence() {
        currentSequence = null;
        currentMidi = "";
        currentMidiOwner = "";
        for (int i=0; i<16; i++) {
            channelInstruments.set(i, MidiUtils.GeneralMidiInstrument.EMPTY.name);
        }
        stopSequencer();
        link.stopAllNotes();
    }

    public boolean isSequenceLoaded() {
        return currentSequence != null && currentMidi != null && currentMidiOwner != null;
    }

    public void setTempo(byte[] data) {
        int microsPerQuarterNote = ((data[0] & 0xFF) << 16) | // combine into a single int
                ((data[1] & 0xFF) << 8) |
                (data[2] & 0xFF);
        bpm = 60_000_000f / microsPerQuarterNote;
        float midiTPS = (1000000f/microsPerQuarterNote) * ppq;
        tickStep = Math.round(midiTPS/20);
    }

    public void setChannelInstrument(int channel, int program) {
        channelInstruments.set(channel, MidiUtils.GeneralMidiInstrument.fromProgram(program).name);
    }

    public void tickSequencer() {
        for (Queue<MidiEvent> track : currentSequence) {
            while (track.peek() != null && track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();

                if (msg instanceof MetaMessage mm) { // if it's a MetaMessage

                    if (MidiUtils.isTempoChange(mm)) { // if it's a tempo change MetaMessage
                        setTempo(mm.getData());

                    } else if (MidiUtils.isTrackEnd(mm)) { // if it's a file end MetaMessage
                        stopSequencer(); // ngl... i have no idea why there are notes remaining. but this fixes it. soo....
                        break;
                    }

                } else if (msg instanceof ShortMessage sm) {
                    if (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm)) {
                        handleNote(sm);
                    } else if (MidiUtils.isProgramChange(sm)) {
                        channelInstruments.set(sm.getChannel(), MidiUtils.GeneralMidiInstrument.fromProgram(sm.getData1()).name);
                    }
                    // not worrying about control changes or anything else for now
                } /* else if (msg instanceof SysexMessage sx) {
                    // we'll just ignore sysex messages
                }
                */

            }
        }
        tickPosition += tickStep;
    }

    public void toggleSequencer() {
        playing = !playing;
        if (!playing) {
            link.stopAllNotes();
        }
    }

    public void stopSequencer() {
        playing = false;
        tickPosition = 0;
        tickStep = 1;
        link.stopAllNotes();
    }

    // BLOCK ENTITY STUFF

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void onRollChanged(ItemStack stack) {
        if (stack.isEmpty())
            unloadSequence();
        if (MidiUtils.isMusicRollValid(stack)) {
            try {
                CompoundTag tag = stack.getTag();
                loadSequence(tag.getString("File"), tag.getString("Owner"));
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

    public boolean isPlaying() {
        return playing;
    }

    public void pressTogglePlayButton() {
        if (isSequenceLoaded()) {
            toggleSequencer();
        }
    }

    public void pressStopButton() {
        stopSequencer();
    }

}
