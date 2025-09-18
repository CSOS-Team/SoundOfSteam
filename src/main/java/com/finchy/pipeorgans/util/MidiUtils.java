package com.finchy.pipeorgans.util;

import com.finchy.pipeorgans.content.midi.MusicRollItem;
import net.minecraft.world.item.ItemStack;

import javax.sound.midi.*;
import java.util.*;

public abstract class MidiUtils {

    public static boolean isNoteOn(ShortMessage sm) {
        return sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0; // if message is note on AND velocity > 0
    }

    public static boolean isNoteOff(ShortMessage sm) {
        return (sm.getCommand() == ShortMessage.NOTE_OFF) || // if message is note off
                (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() == 0); // OR if message is note on AND velocity == 0
    }

    public static boolean isProgramChange(ShortMessage sm) {
        return sm.getCommand() == ShortMessage.PROGRAM_CHANGE;
    }

    public static boolean isTempoChange(MetaMessage mm) {
        return mm.getType() == 0x51;
    }

    public static boolean isFileEnd(MetaMessage mm) {
        return mm.getType() == 0x2F;
    }

    public static boolean isMusicRollValid(ItemStack stack) {
        return stack.getItem() instanceof MusicRollItem && // just in case they use commands or something
                stack.hasTag() && // if it even has a tag
                stack.getTag().contains("Owner") &&
                stack.getTag().contains("File");
    }

    public enum GeneralMidiInstrument {

        ACOUSTIC_GRAND_PIANO(0, "Acoustic Grand Piano"),
        BRIGHT_ACOUSTIC_PIANO(1, "Bright Acoustic Piano"),
        ELECTRIC_GRAND_PIANO(2, "Electric Grand Piano"),
        HONKY_TONK_PIANO(3, "Honky-tonk Piano"),
        ELECTRIC_PIANO_1(4, "Electric Piano 1"),
        ELECTRIC_PIANO_2(5, "Electric Piano 2"),
        HARPSICHORD(6, "Harpsichord"),
        CLAVINET(7, "Clavinet"),

        CELESTA(8, "Celesta"),
        GLOCKENSPIEL(9, "Glockenspiel"),
        MUSIC_BOX(10, "Music Box"),
        VIBRAPHONE(11, "Vibraphone"),
        MARIMBA(12, "Marimba"),
        XYLOPHONE(13, "Xylophone"),
        TUBULAR_BELLS(14, "Tubular Bells"),
        DULCIMER(15, "Dulcimer"),

        DRAWBAR_ORGAN(16, "Drawbar Organ"),
        PERCUSSIVE_ORGAN(17, "Percussive Organ"),
        ROCK_ORGAN(18, "Rock Organ"),
        CHURCH_ORGAN(19, "Church Organ"),
        REED_ORGAN(20, "Reed Organ"),
        ACCORDION(21, "Accordion"),
        HARMONICA(22, "Harmonica"),
        TANGO_ACCORDION(23, "Tango Accordion"),

        ACOUSTIC_GUITAR_NYLON(24, "Acoustic Guitar (nylon)"),
        ACOUSTIC_GUITAR_STEEL(25, "Acoustic Guitar (steel)"),
        ELECTRIC_GUITAR_JAZZ(26, "Electric Guitar (jazz)"),
        ELECTRIC_GUITAR_CLEAN(27, "Electric Guitar (clean)"),
        ELECTRIC_GUITAR_MUTED(28, "Electric Guitar (muted)"),
        ELECTRIC_GUITAR_OVERDRIVE(29, "Electric Guitar (overdrive)"),
        ELECTRIC_GUITAR_DISTORTION(30, "Electric Guitar (distortion)"),
        ELECTRIC_GUITAR_HARMONICS(31, "Electric Guitar (harmonics)"),

        ACOUSTIC_BASS(32, "Acoustic Bass"),
        ELECTRIC_BASS_FINGER(33, "Electric Bass (finger)"),
        ELECTRIC_BASS_PICK(34, "Electric Bass (pick)"),
        FRETLESS_BASS(35, "Fretless Bass"),
        SLAP_BASS_1(36, "Slap Bass 1"),
        SLAP_BASS_2(37, "Slap Bass 2"),
        SYNTH_BASS_1(38, "Synth Bass 1"),
        SYNTH_BASS_2(39, "Synth Bass 2"),

        VIOLIN(40, "Violin"),
        VIOLA(41, "Viola"),
        CELLO(42, "Cello"),
        CONTRABASS(43, "Contrabass"),
        TREMOLO_STRINGS(44, "Tremolo Strings"),
        PIZZICATO_STRINGS(45, "Pizzicato Strings"),
        ORCHESTRAL_HARP(46, "Orchestral Harp"),
        TIMPANI(47, "Timpani"),

        STRING_ENSEMBLE_1(48, "String Ensemble 1"),
        STRING_ENSEMBLE_2(49, "String Ensemble 2"),
        SYNTH_STRINGS_1(50, "Synth Strings 1"),
        SYNTH_STRINGS_2(51, "Synth Strings 2"),
        CHOIR_AAHS(52, "Choir Aahs"),
        VOICE_OOHS(53, "Voice Oohs"),
        SYNTH_VOICE(54, "Synth Voice"),
        ORCHESTRA_HIT(55, "Orchestra Hit"),

        TRUMPET(56, "Trumpet"),
        TROMBONE(57, "Trombone"),
        TUBA(58, "Tuba"),
        MUTED_TRUMPET(59, "Muted Trumpet"),
        FRENCH_HORN(60, "French Horn"),
        BRASS_SECTION(61, "Brass Section"),
        SYNTH_BRASS_1(62, "Synth Brass 1"),
        SYNTH_BRASS_2(63, "Synth Brass 2"),

        SOPRANO_SAX(64, "Soprano Sax"),
        ALTO_SAX(65, "Alto Sax"),
        TENOR_SAX(66, "Tenor Sax"),
        BARITONE_SAX(67, "Baritone Sax"),
        OBOE(68, "Oboe"),
        ENGLISH_HORN(69, "English Horn"),
        BASSOON(70, "Bassoon"),
        CLARINET(71, "Clarinet"),

        PICCOLO(72, "Piccolo"),
        FLUTE(73, "Flute"),
        RECORDER(74, "Recorder"),
        PAN_FLUTE(75, "Pan Flute"),
        BLOWN_BOTTLE(76, "Blown Bottle"),
        SHAKUHACHI(77, "Shakuhachi"),
        WHISTLE(78, "Whistle"),
        OCARINA(79, "Ocarina"),

        LEAD_1_SQUARE(80, "Lead 1 (square)"),
        LEAD_2_SAWTOOTH(81, "Lead 2 (sawtooth)"),
        LEAD_3_CALLIOPE(82, "Lead 3 (calliope)"),
        LEAD_4_CHIFF(83, "Lead 4 (chiff)"),
        LEAD_5_CHARANG(84, "Lead 5 (charang)"),
        LEAD_6_VOICE(85, "Lead 6 (voice)"),
        LEAD_7_FIFTHS(86, "Lead 7 (fifths)"),
        LEAD_8_BASS_LEAD(87, "Lead 8 (bass + lead)"),

        PAD_1_NEW_AGE(88, "Pad 1 (new age)"),
        PAD_2_WARM(89, "Pad 2 (warm)"),
        PAD_3_POLYSYNTH(90, "Pad 3 (polysynth)"),
        PAD_4_CHOIR(91, "Pad 4 (choir)"),
        PAD_5_BOWED(92, "Pad 5 (bowed)"),
        PAD_6_METALLIC(93, "Pad 6 (metallic)"),
        PAD_7_HALO(94, "Pad 7 (halo)"),
        PAD_8_SWEEP(95, "Pad 8 (sweep)"),

        FX_1_RAIN(96, "FX 1 (rain)"),
        FX_2_SOUNDTRACK(97, "FX 2 (soundtrack)"),
        FX_3_CRYSTAL(98, "FX 3 (crystal)"),
        FX_4_ATMOSPHERE(99, "FX 4 (atmosphere)"),
        FX_5_BRIGHTNESS(100, "FX 5 (brightness)"),
        FX_6_GOBLINS(101, "FX 6 (goblins)"),
        FX_7_ECHOES(102, "FX 7 (echoes)"),
        FX_8_SCI_FI(103, "FX 8 (sci-fi)"),

        SITAR(104, "Sitar"),
        BANJO(105, "Banjo"),
        SHAMISEN(106, "Shamisen"),
        KOTO(107, "Koto"),
        KALIMBA(108, "Kalimba"),
        BAGPIPE(109, "Bagpipe"),
        FIDDLE(110, "Fiddle"),
        SHANAI(111, "Shanai"),

        TINKLE_BELL(112, "Tinkle Bell"),
        AGOGO(113, "Agogo"),
        STEEL_DRUMS(114, "Steel Drums"),
        WOODBLOCK(115, "Woodblock"),
        TAIKO_DRUM(116, "Taiko Drum"),
        MELODIC_TOM(117, "Melodic Tom"),
        SYNTH_DRUM(118, "Synth Drum"),
        REVERSE_CYMBAL(119, "Reverse Cymbal"),

        GUITAR_FRET_NOISE(120, "Guitar Fret Noise"),
        BREATH_NOISE(121, "Breath Noise"),
        SEASHORE(122, "Seashore"),
        BIRD_TWEET(123, "Bird Tweet"),
        TELEPHONE_RING(124, "Telephone Ring"),
        HELICOPTER(125, "Helicopter"),
        APPLAUSE(126, "Applause"),
        GUNSHOT(127, "Gunshot"),

        EMPTY(-1, "empty");

        public final String name;
        public final int program;

        GeneralMidiInstrument(int program, String name) {
            this.program = program;
            this.name = name;
        }

        private static final Map<Integer, GeneralMidiInstrument> BY_PROGRAM = new HashMap<>();
        static {
            for (GeneralMidiInstrument instrument : values()) {
                BY_PROGRAM.put(instrument.program, instrument);
            }
        }

        public static GeneralMidiInstrument fromProgram(int program) {
            return BY_PROGRAM.get(program);
        }

    }

}
