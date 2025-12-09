package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.midi.Proxy;

public class ClientProxy implements Proxy {

    private MidiDataManager MIDI_DATA;

    private boolean initialised = false;

    @Override
    public void init() {
        MIDI_DATA = new MidiDataManager();
        initialised = true;
    }

    public MidiDataManager getMidiData() {
        return MIDI_DATA;
    }

    @Override
    public boolean isInitialised() {
        return initialised;
    }

    @Override
    public boolean isClient() {
        return true;
    }
}
