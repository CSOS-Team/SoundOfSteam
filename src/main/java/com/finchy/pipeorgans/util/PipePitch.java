package com.finchy.pipeorgans.util;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

public record PipePitch(PitchClass pitchClass, Octave octave) {

    public enum Octave {
        OCTAVE_n1(-1),
        OCTAVE_0(0),
        OCTAVE_1(1),
        OCTAVE_2(2),
        OCTAVE_3(3),
        OCTAVE_4(4),
        OCTAVE_5(5),
        OCTAVE_6(6),
        OCTAVE_7(7),
        OCTAVE_8(8)
        ;
        private final int number;

        Octave(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public String getNormalizedName() {
            if (number == -1) {
                return "n1";
            }
            return Integer.toString(number);
        }

        public Octave next() {
            int nextOrdinal = this.ordinal() + 1;
            if (nextOrdinal >= Octave.values().length) {
                return null;
            }
            return Octave.values()[nextOrdinal];
        }
    }

    public enum PitchClass {
        C("C"),
        C_SHARP("C#"),
        D("D"),
        D_SHARP("D#"),
        E("E"),
        F("F"),
        F_SHARP("F#"),
        G("G"),
        G_SHARP("G#"),
        A("A"),
        A_SHARP("A#"),
        B("B")
        ;
        private final String noteName;

        PitchClass(String noteName) {
            this.noteName = noteName;
        }

        public String getNoteName() {
            return noteName;
        }

        public String getNormalizedName() {
            return noteName.replace("#", "_sharp").toLowerCase();
        }

        public PitchClass next() {
            int nextOrdinal = this.ordinal() + 1;
            if (nextOrdinal >= PitchClass.values().length) {
                return PitchClass.C;
            }
            return PitchClass.values()[nextOrdinal];
        }
    }

    public String getName() {
        return pitchClass().getNoteName() + octave().getNumber();
    }

    public String getNormalizedName() {
        return pitchClass().getNormalizedName() + "_" + octave().getNormalizedName();
    }

    public static Iterable<PipePitch> allPipePitches() {
        return () -> new Iterator<>() {
            private Octave currentOctave = Octave.OCTAVE_n1;
            private PitchClass currentPitchClass = PitchClass.F_SHARP;

            @Override
            public boolean hasNext() {
                return currentOctave != Octave.OCTAVE_8 || currentPitchClass != PitchClass.G; // Allow the F#8 pitch to be included
            }

            @Override
            public PipePitch next() {
                PipePitch pitch = new PipePitch(currentPitchClass, currentOctave);
                if (currentPitchClass == PitchClass.B) {
                    currentPitchClass = PitchClass.C;
                    currentOctave = currentOctave.next();
                } else {
                    currentPitchClass = currentPitchClass.next();
                }
                return pitch;
            }
        };
    }

    public static int totalPitchCount() {
        return PitchClass.values().length * (Octave.values().length - 1) + 1; // minus 1 because octave -1 has only F# to B and 8 has only C to F#, plus one for F#-1 or F#8, depending on how you count it
    }
}
