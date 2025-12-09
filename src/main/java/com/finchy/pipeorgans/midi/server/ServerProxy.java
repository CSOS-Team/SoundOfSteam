package com.finchy.pipeorgans.midi.server;

import com.finchy.pipeorgans.midi.Proxy;

public class ServerProxy implements Proxy {

    private boolean initialised = false;

    @Override
    public boolean isInitialised() {
        return initialised;
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void init() {
        initialised = true;
    }
}
