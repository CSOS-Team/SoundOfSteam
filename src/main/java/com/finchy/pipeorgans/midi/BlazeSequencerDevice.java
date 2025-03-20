package com.finchy.pipeorgans.midi;

import com.finchy.pipeorgans.PipeOrgans;

import javax.sound.midi.*;
import java.util.Arrays;

public class BlazeSequencerDevice extends MidiSourceDevice {

    private Sequencer sequencer = null;

    public BlazeSequencerDevice(String uuid) {
        super(uuid,
                new Info(
                        "Blaze Sequencer", "Finchy",
                        uuid, "1.0"
                ) {}
        );
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException e) {
            PipeOrgans.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public void setSequence(Sequence sequence) throws InvalidMidiDataException {
        sequencer.setSequence(sequence);
    }

    public Sequence getSequence() {
        return sequencer.getSequence();
    }

    public void start() {
        sequencer.start();
    }

    public void stop() {
        sequencer.stop();
    }

    public boolean isRunning() {
        return sequencer.isRunning();
    }

    public float getTempoInBPM() {
        return sequencer.getTempoInBPM();
    }

    public void setTempoInBPM(float bpm) {
        sequencer.setTempoInBPM(bpm);
    }

    public long getTickLength() {
        return sequencer.getTickLength();
    }

    public long getTickPosition() {
        return sequencer.getTickPosition();
    }

    public void setTickPosition(long tick) {
        sequencer.setTickPosition(tick);
    }

    public long getMicrosecondPosition() {
        return sequencer.getMicrosecondPosition();
    }

    public void setMicrosecondPosition(long microseconds) {
        sequencer.setMicrosecondPosition(microseconds);
    }
}
