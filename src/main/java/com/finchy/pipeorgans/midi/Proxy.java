package com.finchy.pipeorgans.midi;

public interface Proxy {
    boolean isInitialised();
    boolean isClient();
    void init();
}
