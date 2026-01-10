package com.finchy.pipeorgans.util;

import com.finchy.pipeorgans.midi.PitchMapping;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
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

        public boolean isLessThan(Octave other) {
            return this.number < other.number;
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
        B("B");
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

        public boolean isLessThanOrEqualTo(PitchClass other) {
            return this.ordinal() <= other.ordinal();
        }

        public boolean isGreaterThanOrEqualTo(PitchClass other) {
            return this.ordinal() >= other.ordinal();
        }

        public int getIndexOffset() {
            return (this.ordinal() + values().length - PipePitch.LOWEST.getMidiPitchNumber()) % values().length;
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

    public enum OctaveGroup { // maps to (most, damn you Nasard) the pipe size ranges for clarity / ease of use
        GROUP_n1_to_0, // values are automatically assigned based on ordinal
        GROUP_0_to_1,
        GROUP_1_to_2,
        GROUP_2_to_3,
        GROUP_3_to_4,
        GROUP_4_to_5,
        GROUP_5_to_6,
        GROUP_6_to_7,
        GROUP_7_to_8,
        GROUP_8_to_9 // not really used, just to make F#8 accessible
        ;

        private final PipePitch start;
        private final PipePitch end;

        OctaveGroup() {
            start = new PipePitch(PitchClass.F_SHARP, Octave.values()[this.ordinal()]);

            if (this.ordinal() + 1 >= Octave.values().length) {  // last group, just for F#8
                end = new PipePitch(PitchClass.F_SHARP, Octave.OCTAVE_8);
            } else {
                end = new PipePitch(PitchClass.F, Octave.values()[this.ordinal() + 1]);
            }
        }

        public Iterable<PipePitch> getPitches() {
            return () -> new Iterator(start, end);
        }

        public PipePitch getStart() {
            return start;
        }

        public PipePitch getEnd() {
            return end;
        }

        public PipePitch getOffset(int offset) {
            if (offset > 11) {
                throw new IllegalArgumentException("Offset must be between 0 and 11");
            }
            int midiNumber = start.getMidiPitchNumber() + offset;
            return PipePitch.fromMidiPitchNumber(midiNumber);
        }

        public Component getLabel() {
            return Component.translatable("pipeorgans.octave_group.label", start.getName(), end.getName());
        }

        public static List<Component> getAllComponents() {
            return Stream.of(values()).map(OctaveGroup::getLabel).toList();
        }

        public static List<Component> getAllFullComponents() { // excludes last group for easier use in UIs
            return Stream.of(values()).limit(values().length - 1) // exclude last group
                    .map(OctaveGroup::getLabel).toList();
        }
    }

    public static class Iterator implements java.util.Iterator<PipePitch> {
        private Octave currentOctave;
        private PitchClass currentPitchClass;
        private final PipePitch endPitch;

        public Iterator() {
            this(LOWEST, HIGHEST);
        }

        public Iterator(PipePitch start, PipePitch end) {
            this.currentOctave = start.octave();
            this.currentPitchClass = start.pitchClass();
            this.endPitch = end;
        }

        @Override
        public boolean hasNext() {
            return currentOctave.isLessThan(endPitch.octave()) ||
                    (currentOctave == endPitch.octave() && currentPitchClass.isLessThanOrEqualTo(endPitch.pitchClass()));
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
    }

    public String getName() {
        return pitchClass().getName() + octave().getNumber();
    }

    public String getNormalizedName() {
        return pitchClass().getNormalizedName() + "_" + octave().getNormalizedName();
    }

    public static Iterable<PipePitch> allPipePitches() {
        return Iterator::new;
    }

    public static int totalPitchCount() {
        return PitchClass.values().length * (Octave.values().length - 1) + 1; // minus 1 because octave -1 has only F# to B and 8 has only C to F#, plus one for F#-1 or F#8, depending on how you count it
    }

    public static PipePitch fromNormalizedName(String normalizedName) {
        int lastUnderscore = normalizedName.lastIndexOf('_');
        if (lastUnderscore == -1) {
            throw new IllegalArgumentException("Invalid normalized name: " + normalizedName);
        }
        String pcs = normalizedName.substring(0, lastUnderscore).toLowerCase();
        String octs = normalizedName.substring(lastUnderscore + 1).toLowerCase();
        return new PipePitch(PitchClass.fromNormalizedName(pcs), Octave.fromNormalizedName(octs));
    }

    public int getMidiPitchNumber() {
        return this.octave().ordinal() * PitchClass.values().length + this.pitchClass().ordinal();
    }

    public int getPitchIndex() { // index relative to the lowest pitch, useful for UIs
        return getMidiPitchNumber() - LOWEST.getMidiPitchNumber();
    }

    public OctaveGroup getOctaveGroup() {
        int groupIndex = this.octave().ordinal() - 1;
        if (this.pitchClass().isGreaterThanOrEqualTo(PitchClass.F_SHARP)) groupIndex++;
        if (groupIndex < 0 || groupIndex >= OctaveGroup.values().length) {
            return null;
        }
        return OctaveGroup.values()[groupIndex];
    }

    public ItemStack getMappedItem() {
        int index = getMidiPitchNumber();
        return PitchMapping.getStack(index);
    }

    public RedstoneLinkNetworkHandler.Frequency getMappedFrequency() {
        return RedstoneLinkNetworkHandler.Frequency.of(getMappedItem());
    }

    public PipePitch next() {
        int midiNumber = getMidiPitchNumber();
        if (midiNumber >= HIGHEST.getMidiPitchNumber()) {
            return null;
        }
        return fromMidiPitchNumber(midiNumber + 1);
    }

    public static final PipePitch LOWEST = new PipePitch(PitchClass.F_SHARP, Octave.OCTAVE_n1);
    public static final PipePitch HIGHEST = new PipePitch(PitchClass.F_SHARP, Octave.OCTAVE_8);
    public static final PipePitch DEFAULT = LOWEST;

    @Nullable
    public static PipePitch fromMidiPitchNumber(int midiPitchNumber) {
        if (midiPitchNumber < LOWEST.getMidiPitchNumber() || midiPitchNumber > HIGHEST.getMidiPitchNumber()) {
            return null;
        }
        int octaveIndex = midiPitchNumber / PitchClass.values().length;
        int pitchClassIndex = midiPitchNumber % PitchClass.values().length;
        return new PipePitch(PitchClass.values()[pitchClassIndex], Octave.values()[octaveIndex]);
    }

    @Nullable
    public static PipePitch fromPitchIndex(int pitchIndex) {
        return fromMidiPitchNumber(pitchIndex + LOWEST.getMidiPitchNumber());
    }

    public static PipePitch fromIndices(int pitchClassIndex, int octaveIndex) {
        return new PipePitch(PitchClass.values()[pitchClassIndex], Octave.values()[octaveIndex]);
    }

    public static PipePitch fromOctaveGroupAndOffset(OctaveGroup group, int offset) {
        return group.getOffset(offset);
    }

    public static PipePitch fromOctaveGroupAndOffset(int groupIndex, int offset) {
        return fromOctaveGroupAndOffset(OctaveGroup.values()[groupIndex], offset);
    }

    public PipePitch copy() {
        return new PipePitch(this.pitchClass(), this.octave());
    }
}
