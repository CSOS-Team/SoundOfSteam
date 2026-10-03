package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;

/**
 * Defines different sizes of stops
 */
public enum StopSize implements StringRepresentable {
    ONEANDTHREEFIFTHS("135", 28),
    TWO("2", 24),
    TWOANDTWOTHIRDS("223", 19),
    FOUR("4", 12),
    EIGHT("8", 0),
    SIXTEEN("16", -12),
    THIRTYTWO("32", -24);

    // for the purposes of calculations, "0 semitones" is an F#4, i.e. pitch 0 on an 8' pipe with medium size

    private final String size;
    private final int mutation; // amount to increase pitch by relative to 8', in semitones. e.g. 2-2/3 is a 12th above 8', or 19 semitones
    StopSize(String size, int mutation) {
        this.size = size;
        this.mutation = mutation;
    }

    public int getMutatedPitch(int bePitch) {
        // subtract mutation from pitch (higher pitch integer = lower note)
        // mod 12 to bring it back within an octave
        int mutatedPitch = (bePitch - mutation) % 12;
        if (mutatedPitch < 0) // if the result is negative, add 12 to make it ACTUALLY a modulo operation, and not just remainder
            mutatedPitch += 12;
        return mutatedPitch;
    }

    public int getMutatedOctave(int pitch, PipeSize pipeSize) {
        int sizeOffset = (2 - pipeSize.ordinal()) * 12; // tiny = 24, small = 12, medium = 0, large = -12, huge = -24
        int pitchOffset = -pitch; // higher pitch value = lower note
        int absolutePitch = pitchOffset + sizeOffset + mutation; // pitch in semitones relative to F#4
        // midi number of F#4 is 66, so add 66 to absolutePitch to get the midi number of that note
        // from the midi number, octave = (midi number / 12) - 1
        return ((absolutePitch + 66) / 12) - 1;
    }

    @Override
    public String getSerializedName() {
        return size;
    }
}
