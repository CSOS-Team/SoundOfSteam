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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity implements MenuProvider {

    private boolean buttonsEnabled = false;

    private List<Queue<MidiEvent>> currentSequence = null;

    private boolean playing = false;
    private int tickPosition = 0;
    private int ppq;
    private int tickStep = 1;
    public double bpm;
    public int endTick = 1;
    public List<MidiUtils.GeneralMidiInstrument> channelInstruments;
    public static final List<MidiUtils.GeneralMidiInstrument> defaultChannelInstruments = new ArrayList<>(Collections.nCopies(16, MidiUtils.GeneralMidiInstrument.EMPTY));

    private String currentMidi = "";
    private String currentMidiOwner = "";

    public TrackerBarInventory inventory;
    protected final ContainerData data;

    public class TrackerBarInventory extends ItemStackHandler {
        private final TrackerBarBlockEntity be;

        public TrackerBarInventory(TrackerBarBlockEntity be) {
            super(1);
            this.be = be;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            be.setChanged();
        }
    }

    public TrackerBarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        inventory = new TrackerBarInventory(this);
        channelInstruments = new ArrayList<>(defaultChannelInstruments);
        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> TrackerBarBlockEntity.this.channelInstruments.get(0).program;
                    case 1 -> TrackerBarBlockEntity.this.channelInstruments.get(1).program;
                    case 2 -> TrackerBarBlockEntity.this.channelInstruments.get(2).program;
                    case 3 -> TrackerBarBlockEntity.this.channelInstruments.get(3).program;
                    case 4 -> TrackerBarBlockEntity.this.channelInstruments.get(4).program;
                    case 5 -> TrackerBarBlockEntity.this.channelInstruments.get(5).program;
                    case 6 -> TrackerBarBlockEntity.this.channelInstruments.get(6).program;
                    case 7 -> TrackerBarBlockEntity.this.channelInstruments.get(7).program;
                    case 8 -> TrackerBarBlockEntity.this.channelInstruments.get(8).program;
                    case 9 -> TrackerBarBlockEntity.this.channelInstruments.get(9).program;
                    case 10 -> TrackerBarBlockEntity.this.channelInstruments.get(10).program;
                    case 11 -> TrackerBarBlockEntity.this.channelInstruments.get(11).program;
                    case 12 -> TrackerBarBlockEntity.this.channelInstruments.get(12).program;
                    case 13 -> TrackerBarBlockEntity.this.channelInstruments.get(13).program;
                    case 14 -> TrackerBarBlockEntity.this.channelInstruments.get(14).program;
                    case 15 -> TrackerBarBlockEntity.this.channelInstruments.get(15).program;

                    case 16 -> TrackerBarBlockEntity.this.playing ? 1 : 0;
                    case 17 -> TrackerBarBlockEntity.this.tickPosition;
                    case 18 -> TrackerBarBlockEntity.this.endTick;
                    case 19 -> (int) (TrackerBarBlockEntity.this.bpm*10);
                    case 20 -> TrackerBarBlockEntity.this.buttonsEnabled ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                // nuh uh
            }

            @Override
            public int getCount() {
                return 21;
            }
        };
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putString("File", currentMidi);
        tag.putString("Owner", currentMidiOwner);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        String midiRead = tag.getString("File");
        String ownerRead = tag.getString("Owner");
        try {
            if (!midiRead.isEmpty() && !ownerRead.isEmpty()) {loadSequence(midiRead, ownerRead);
                buttonsEnabled = true;
            } else buttonsEnabled = false;
        } catch (MidiLoadException e) {
            PipeOrgans.LOGGER.error("Exception when loading data from Tracker Bar NBT: {}", e.getMessage());
            buttonsEnabled = false;
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.tracker_bar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return TrackerBarMenu.create(pContainerId, pPlayerInventory, this, data);
    }

    @Override
    public void tick() {
        super.tick();
        if (playing) {
            tickSequencer();
        }
    }

    // SEQUENCER

    public void loadSequence(String file, String owner) throws MidiLoadException {
        Sequence sequence = MidiFileParser.getSequenceFromFile(file, owner);

        currentSequence = MidiFileParser.parseMidiEvents(sequence);

        ppq = sequence.getResolution();
        MidiFileParser.initialParse(sequence, this::setChannelInstrument, this::setTempo);
        endTick = MidiFileParser.endTick(sequence);
        currentMidi = file;
        currentMidiOwner = owner;
        notifyUpdate();
    }

    public void unloadSequence() {
        currentSequence = null;
        currentMidi = "";
        currentMidiOwner = "";
        channelInstruments = new ArrayList<>(defaultChannelInstruments);
        playing = false;
        tickPosition = 0;
        tickStep = 1;
        link.stopAllNotes();
        notifyUpdate();
    }

    public boolean isSequenceLoaded() {
        return currentSequence != null && !currentMidi.isEmpty() && !currentMidiOwner.isEmpty();
    }

    public void setTempo(byte[] data) {
        int microsPerQuarterNote = ((data[0] & 0xFF) << 16) | // combine into a single int
                ((data[1] & 0xFF) << 8) |
                (data[2] & 0xFF);
        bpm = 60_000_000f / microsPerQuarterNote;
        float midiTPS = (1000000f/microsPerQuarterNote) * ppq;
        tickStep = Math.round(midiTPS/20);
        setChanged();
    }

    public void setChannelInstrument(int channel, int program) {
        channelInstruments.set(channel, MidiUtils.GeneralMidiInstrument.fromProgram(program));
    }

    public void tickSequencer() {
        for (Queue<MidiEvent> track : currentSequence) {
            while (track.peek() != null && track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();

                if (tickPosition >= endTick) {
                    link.stopAllNotes();
                    restartPlayback();
                    return;
                }

                if (msg instanceof MetaMessage mm) { // if it's a MetaMessage

                    if (MidiUtils.isTempoChange(mm)) { // if it's a tempo change MetaMessage
                        setTempo(mm.getData());
                    }

                } else if (msg instanceof ShortMessage sm) {
                    if (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm)) {
                        handleNote(sm);
                    } else if (MidiUtils.isProgramChange(sm)) {
                        channelInstruments.set(sm.getChannel(), MidiUtils.GeneralMidiInstrument.fromProgram(sm.getData1()));
                    }
                    // not worrying about control changes or anything else for now
                } /* else if (msg instanceof SysexMessage sx) {
                    // we'll just ignore sysex messages
                }
                */

            }
        }
        tickPosition += tickStep;
        setChanged();
    }

    public void toggleSequencer() {
        playing = !playing;
        if (!playing) {
            link.stopAllNotes();
        }
        setChanged();
    }

    public void restartPlayback() {
        playing = false;
        link.stopAllNotes();
        tickPosition = 0;
        try {
            loadSequence(currentMidi, currentMidiOwner);
        } catch (MidiLoadException e) {
            buttonsEnabled = false;
        }
        setChanged();
    }

    public void stopSequencer() {
        playing = false;
        tickPosition = 0;
        link.stopAllNotes();
    }

    // BLOCK ENTITY STUFF

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void onRollChanged() {
        ItemStack stack = inventory.getStackInSlot(0);
        if (stack.isEmpty()) {
            unloadSequence();
            buttonsEnabled = false;
        } else if (MidiUtils.isMusicRollValid(stack)) {
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
        setChanged();
    }

    @Override
    public void handleMidiMessage(MidiMessage mm) {
        if (mm instanceof ShortMessage sm && (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm))) {
            handleNote(sm);
        }
    }

    public boolean getButtonsEnabled() {
        return buttonsEnabled;
    }

    public String getCurrentMidi() {
        return currentMidi;
    }

    public void pressTogglePlayButton() {
        if (isSequenceLoaded()) {
            toggleSequencer();
        }
    }

    public void pressStopButton() {
        restartPlayback();
    }

}
