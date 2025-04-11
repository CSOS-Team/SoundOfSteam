package com.finchy.pipeorgans.util.midi;

public abstract class FinchyPitchMapping extends PitchMapping {

    @Override
    protected void setMappings() {
        // need to set 6-114 (F#-1 to F#8)
        // this mapping is based on the pipes' octaves
        // i.e. F# is the first colour of an octave, F is the last, and it repeats
        // (as opposed to centring the repetition around C)

        //set();
    }
}
