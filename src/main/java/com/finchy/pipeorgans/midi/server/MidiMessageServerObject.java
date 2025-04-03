package com.finchy.pipeorgans.midi.server;

public class MidiMessageServerObject {

    public int channel;
    public int note;
    public int velocity;

    public MidiMessageServerObject(int channel, int note, int velocity) {
        this.channel = channel;
        this.note = note;
        this.velocity = velocity;
    }
}
