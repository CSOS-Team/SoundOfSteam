package com.finchy.pipeorgans.util;

import com.finchy.pipeorgans.midi.PitchMapping;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

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

        public Component getComponentName() {
            return Component.translatableWithFallback("pipeorgans.octave." + getNormalizedName(), String.valueOf(getNumber()));
        }

        public Octave next() {
            int nextOrdinal = this.ordinal() + 1;
            if (nextOrdinal >= Octave.values().length) {
                return null;
            }
            return Octave.values()[nextOrdinal];
        }

        public static Octave fromNumber(int number) {
            return Octave.values()[number + 1]; // +1 because octave -1 is at index 0
        }

        public static Octave fromNormalizedName(String normalizedName) {
            return Octave.valueOf("OCTAVE_" + normalizedName);
        }

        public static List<Component> getAllComponents() {
            return Stream.of(values()).map(Octave::getComponentName).toList();
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

        public String getName() {
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

        public Component getComponentName() {
            return Component.translatableWithFallback("pipeorgans.pitch_class." + getNormalizedName(), getName());
        }

        public static PitchClass fromName(String name) {
            for (PitchClass pc : PitchClass.values()) {
                if (pc.getName().equals(name)) {
                    return pc;
                }
            }
            return null;
        }

        public static PitchClass fromNormalizedName(String normalizedName) {
            for (PitchClass pc : PitchClass.values()) {
                if (pc.getNormalizedName().equals(normalizedName)) {
                    return pc;
                }
            }
            return null;
        }

        public static List<Component> getAllComponents() {
            return Stream.of(values()).map(PitchClass::getComponentName).toList();
        }
    }

    public String getName() {
        return pitchClass().getName() + octave().getNumber();
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

    public static PipePitch fromNormalizedName(String normalizedName) {
        String pcs = normalizedName.substring(0, normalizedName.lastIndexOf('_')).toLowerCase();
        String octs = normalizedName.substring(pcs.length() + 1).toLowerCase();
        return new PipePitch(PitchClass.fromNormalizedName(pcs), Octave.fromNormalizedName(octs));
    }

    public ItemStack getMappedItem() {
        if (!isValid()) {
            return ItemStack.EMPTY;
        }
        int index = this.octave().ordinal() * PitchClass.values().length + this.pitchClass().ordinal();
        return PitchMapping.getStack(index);
    }

    public RedstoneLinkNetworkHandler.Frequency getMappedFrequency() {
        if (!isValid()) {
            return RedstoneLinkNetworkHandler.Frequency.EMPTY;
        }
        return RedstoneLinkNetworkHandler.Frequency.of(getMappedItem());
    }

    public static final PipePitch INVALID = new PipePitch(PitchClass.C, Octave.OCTAVE_n1); // not used for the pipes, so can represent an invalid pitch

    public boolean isValid() {
        return !(this.pitchClass() == PitchClass.C && this.octave() == Octave.OCTAVE_n1);
    }

    public PipePitch copy() {
        return new PipePitch(this.pitchClass(), this.octave());
    }
}
