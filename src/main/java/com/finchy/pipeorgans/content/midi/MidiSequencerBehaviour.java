package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlockEntity;
import com.finchy.pipeorgans.util.MidiLoadException;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.nbt.CompoundTag;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class MidiSequencerBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<MidiSequencerBehaviour> TYPE = new BehaviourType<>();

    private List<Queue<MidiEvent>> currentSequence = null;

    private boolean playing = false;
    private int tickPosition = 0;
    private int ppq;
    private int tickStep = 1;
    private double bpm;
    private int endTick = 1;
    public List<Integer> channelInstruments;
    private static final List<Integer> defaultChannelInstruments = new ArrayList<>(Collections.nCopies(16, -1));

    private String currentMidi = "";
    private String currentMidiOwner = "";

    public MidiSequencerBehaviour(SmartBlockEntity be) {
        super(be);
        channelInstruments = new ArrayList<>(defaultChannelInstruments);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        tag.putString("File", currentMidi);
        tag.putString("Owner", currentMidiOwner);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        String midiRead = tag.getString("File");
        String ownerRead = tag.getString("Owner");
        try {
            if (!midiRead.isEmpty() && !ownerRead.isEmpty()) {
                loadSequence(midiRead, ownerRead);
                setButtonsEnabled(true);
            } else setButtonsEnabled(false);
        } catch (MidiLoadException e) {
            PipeOrgans.LOGGER.error("Exception when loading data from Tracker Bar NBT: {}", e.getMessage());
            setButtonsEnabled(false);
        }
    }

    public void loadSequence(String file, String owner) throws MidiLoadException {
        Sequence sequence = MidiFileParser.getSequenceFromFile(file, owner);
        currentSequence = MidiFileParser.parseMidiEvents(sequence);

        channelInstruments = new ArrayList<>(defaultChannelInstruments);
        ppq = sequence.getResolution();
        MidiFileParser.initialParse(sequence, this::setChannelInstrument, this::setTempo);
        endTick = MidiFileParser.endTick(sequence);
        currentMidi = file;
        currentMidiOwner = owner;
        blockEntity.notifyUpdate();
    }

    public void unloadSequence() {
        currentSequence = null;
        currentMidi = "";
        currentMidiOwner = "";
        channelInstruments = new ArrayList<>(defaultChannelInstruments);
        playing = false;
        tickPosition = 0;
        tickStep = 1;
        ((TrackerBarBlockEntity) blockEntity).stopAllNotes();
        blockEntity.notifyUpdate();
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
        blockEntity.setChanged();
    }

    public void setChannelInstrument(int channel, int program) {
        channelInstruments.set(channel, program);
    }

    public void tickSequencer() {
        for (Queue<MidiEvent> track : currentSequence) {
            while (track.peek() != null && track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();

                if (tickPosition >= endTick) {
                    restartPlayback();
                    return;
                }

                if (msg instanceof MetaMessage mm) { // if it's a MetaMessage

                    if (MidiUtils.isTempoChange(mm)) { // if it's a tempo change MetaMessage
                        setTempo(mm.getData());
                    }

                } else if (msg instanceof ShortMessage sm) {
                    if (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm)) {
                        ((TrackerBarBlockEntity) blockEntity).handleNote(sm);
                    } else if (MidiUtils.isProgramChange(sm)) {
                        channelInstruments.set(sm.getChannel(), sm.getData1());
                    }
                    // not worrying about control changes or anything else for now
                } /* else if (msg instanceof SysexMessage sx) {
                    // we'll just ignore sysex messages
                }
                */

            }
        }
        tickPosition += tickStep;
        blockEntity.setChanged();
    }

    public void toggleSequencer() {
        playing = !playing;
        if (!playing) {
            ((TrackerBarBlockEntity) blockEntity).stopAllNotes();
        }
        blockEntity.setChanged();
    }

    public void restartPlayback() {
        playing = false;
        ((TrackerBarBlockEntity) blockEntity).stopAllNotes();
        tickPosition = 0;
        try {
            loadSequence(currentMidi, currentMidiOwner);
        } catch (MidiLoadException e) {
            setButtonsEnabled(false);
        }
        blockEntity.setChanged();
    }

    private void setButtonsEnabled(boolean enabled) {
        ((TrackerBarBlockEntity) blockEntity).setButtonsEnabled(enabled);
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getTickPosition() {
        return tickPosition;
    }

    public int getEndTick() {
        return endTick;
    }

    public int get10xBPM() {
        return (int) bpm*10;
    }

    public String getCurrentMidi() {
        return currentMidi;
    }

    public double getPlaybackPercentage() {
        return (double) tickPosition / endTick;
    }
}
