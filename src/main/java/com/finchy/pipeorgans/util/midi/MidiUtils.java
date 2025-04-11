package com.finchy.pipeorgans.util.midi;

import javax.sound.midi.ShortMessage;

public abstract class MidiUtils {

    public static boolean isNoteOn(ShortMessage sm) {
        return sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0; // if message is note on AND velocity > 0
    }

    public static boolean isNoteOff(ShortMessage sm) {
        return sm.getCommand() == ShortMessage.NOTE_OFF || sm.getData2() == 0; // if message is note off OR velocity == 0
    }

}
