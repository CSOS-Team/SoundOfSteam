package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.util.MidiLoadException;
import com.finchy.pipeorgans.util.MidiUtils;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class MidiSequencer {

    private List<Queue<MidiEvent>> currentSequence = null;

    private boolean playing = false;
    private int tickPosition = 0;
    private int ppq;
    private int tickStep = 1;
    public double bpm;
    public List<String> channelInstruments;

    private String currentMidi = "";
    private String currentMidiOwner = "";

    private final Consumer<MidiMessage> msgHandler;
    private final Runnable noteStop;

    public MidiSequencer(Consumer<MidiMessage> msgHandler, Runnable noteStop) {
        channelInstruments = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            channelInstruments.add(MidiUtils.GeneralMidiInstrument.EMPTY.name);
        }
        this.msgHandler = msgHandler;
        this.noteStop = noteStop;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void loadSequence(String midi, String owner) throws MidiLoadException {
        Sequence sequence = MidiFileParser.getSequenceFromFile(midi, owner);

        currentSequence = MidiFileParser.parseMidiEvents(sequence);
        ppq = sequence.getResolution();
        MidiFileParser.initialParse(sequence, this::setChannelInstrument, this::setTempo);
        currentMidi = midi;
        currentMidiOwner = owner;
    }

    public void unloadSequence() {
        currentSequence = null;
        currentMidi = "";
        currentMidiOwner = "";
        for (int i=0; i<16; i++) {
            channelInstruments.set(i, MidiUtils.GeneralMidiInstrument.EMPTY.name);
        }
        reset();
        noteStop.run();
    }

    public boolean isSequenceLoaded() {
        return currentSequence != null && currentMidi != null && currentMidiOwner != null;
    }

    public void setChannelInstrument(int channel, int program) {
        channelInstruments.set(channel, MidiUtils.GeneralMidiInstrument.fromProgram(program).name);
    }

    private void setTempo(byte[] data) {
        int microsPerQ = ((data[0] & 0xFF) << 16) | // combine into a single int
                ((data[1] & 0xFF) << 8) |
                (data[2] & 0xFF);
        float midiTPS = (1000000f / microsPerQ) * ppq;
        tickStep = Math.round(midiTPS/20);
        bpm = 60000000.0 / microsPerQ;
    }

    public void tick() {
        for (Queue<MidiEvent> track : currentSequence) {
            while (track.peek() != null && track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();

                if (msg instanceof MetaMessage mm) { // if it's a MetaMessage

                    if (MidiUtils.isTempoChange(mm)) { // if it's a tempo change MetaMessage
                        setTempo(mm.getData());

                    } else if (MidiUtils.isFileEnd(mm)) { // if it's a file end MetaMessage
                        reset();
                        noteStop.run(); // ngl... i have no idea why there are notes remaining. but this fixes it. soo....
                        break;
                    }

                } else if (msg instanceof ShortMessage sm) {
                    if (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm)) {
                        msgHandler.accept(sm);
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

    public String getCurrentMidi() {
        return currentMidi;
    }

    public String getCurrentMidiOwner() {
        return currentMidiOwner;
    }

    public void start() {
        reset();
        playing = true;
        PipeOrgans.LOGGER.info("STARTED PLAYING");
    }

    public void toggle() {
        playing = !playing;
        if (!playing) {
            noteStop.run();
        }
    }

    public void reset() {
        playing = false;
        tickPosition = 0;
        tickStep = 1;
        noteStop.run();
    }
}
