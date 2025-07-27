package com.finchy.pipeorgans.util;

public class MidiLoadException extends Exception {
    public MidiLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MidiLoadException(String message) {
        super(message);
    }
}
