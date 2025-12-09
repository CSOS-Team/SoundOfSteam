package com.finchy.pipeorgans.util;

import com.finchy.pipeorgans.content.midi.MusicRollItem;
import com.finchy.pipeorgans.init.AllDataComponents;
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

    public static boolean isTrackEnd(MetaMessage mm) {
        return mm.getType() == 0x2F;
    }

    public static boolean isMusicRollValid(ItemStack stack) {
        return stack.getItem() instanceof MusicRollItem && // just in case they use commands or something
                stack.has(AllDataComponents.MIDI_FILE) &&
                stack.has(AllDataComponents.MIDI_OWNER);
    }

    public enum GeneralMidiInstrument {

        ACOUSTIC_GRAND_PIANO(0, "instrument.pipeorgans.acoustic_grand_piano"),
        BRIGHT_ACOUSTIC_PIANO(1, "instrument.pipeorgans.bright_acoustic_piano"),
        ELECTRIC_GRAND_PIANO(2, "instrument.pipeorgans.electric_grand_piano"),
        HONKY_TONK_PIANO(3, "instrument.pipeorgans.honky_tonk_piano"),
        ELECTRIC_PIANO_1(4, "instrument.pipeorgans.electric_piano_1"),
        ELECTRIC_PIANO_2(5, "instrument.pipeorgans.electric_piano_2"),
        HARPSICHORD(6, "instrument.pipeorgans.harpsichord"),
        CLAVINET(7, "instrument.pipeorgans.clavinet"),

        CELESTA(8, "instrument.pipeorgans.celesta"),
        GLOCKENSPIEL(9, "instrument.pipeorgans.glockenspiel"),
        MUSIC_BOX(10, "instrument.pipeorgans.music_box"),
        VIBRAPHONE(11, "instrument.pipeorgans.vibraphone"),
        MARIMBA(12, "instrument.pipeorgans.marimba"),
        XYLOPHONE(13, "instrument.pipeorgans.xylophone"),
        TUBULAR_BELLS(14, "instrument.pipeorgans.tubular_bells"),
        DULCIMER(15, "instrument.pipeorgans.dulcimer"),

        DRAWBAR_ORGAN(16, "instrument.pipeorgans.drawbar_organ"),
        PERCUSSIVE_ORGAN(17, "instrument.pipeorgans.percussive_organ"),
        ROCK_ORGAN(18, "instrument.pipeorgans.rock_organ"),
        CHURCH_ORGAN(19, "instrument.pipeorgans.church_organ"),
        REED_ORGAN(20, "instrument.pipeorgans.reed_organ"),
        ACCORDION(21, "instrument.pipeorgans.accordion"),
        HARMONICA(22, "instrument.pipeorgans.harmonica"),
        TANGO_ACCORDION(23, "instrument.pipeorgans.tango_accordion"),

        ACOUSTIC_GUITAR_NYLON(24, "instrument.pipeorgans.acoustic_guitar_nylon"),
        ACOUSTIC_GUITAR_STEEL(25, "instrument.pipeorgans.acoustic_guitar_steel"),
        ELECTRIC_GUITAR_JAZZ(26, "instrument.pipeorgans.electric_guitar_jazz"),
        ELECTRIC_GUITAR_CLEAN(27, "instrument.pipeorgans.electric_guitar_clean"),
        ELECTRIC_GUITAR_MUTED(28, "instrument.pipeorgans.electric_guitar_muted"),
        ELECTRIC_GUITAR_OVERDRIVE(29, "instrument.pipeorgans.electric_guitar_overdrive"),
        ELECTRIC_GUITAR_DISTORTION(30, "instrument.pipeorgans.electric_guitar_distortion"),
        ELECTRIC_GUITAR_HARMONICS(31, "instrument.pipeorgans.electric_guitar_harmonics"),

        ACOUSTIC_BASS(32, "instrument.pipeorgans.acoustic_bass"),
        ELECTRIC_BASS_FINGER(33, "instrument.pipeorgans.electric_bass_finger"),
        ELECTRIC_BASS_PICK(34, "instrument.pipeorgans.electric_bass_pick"),
        FRETLESS_BASS(35, "instrument.pipeorgans.fretless_bass"),
        SLAP_BASS_1(36, "instrument.pipeorgans.slap_bass_1"),
        SLAP_BASS_2(37, "instrument.pipeorgans.slap_bass_2"),
        SYNTH_BASS_1(38, "instrument.pipeorgans.synth_bass_1"),
        SYNTH_BASS_2(39, "instrument.pipeorgans.synth_bass_2"),

        VIOLIN(40, "instrument.pipeorgans.violin"),
        VIOLA(41, "instrument.pipeorgans.viola"),
        CELLO(42, "instrument.pipeorgans.cello"),
        CONTRABASS(43, "instrument.pipeorgans.contrabass"),
        TREMOLO_STRINGS(44, "instrument.pipeorgans.tremolo_strings"),
        PIZZICATO_STRINGS(45, "instrument.pipeorgans.pizzicato_strings"),
        ORCHESTRAL_HARP(46, "instrument.pipeorgans.orchestral_harp"),
        TIMPANI(47, "instrument.pipeorgans.timpani"),

        STRING_ENSEMBLE_1(48, "instrument.pipeorgans.string_ensemble_1"),
        STRING_ENSEMBLE_2(49, "instrument.pipeorgans.string_ensemble_2"),
        SYNTH_STRINGS_1(50, "instrument.pipeorgans.synth_strings_1"),
        SYNTH_STRINGS_2(51, "instrument.pipeorgans.synth_strings_2"),
        CHOIR_AAHS(52, "instrument.pipeorgans.choir_aahs"),
        VOICE_OOHS(53, "instrument.pipeorgans.voice_oohs"),
        SYNTH_VOICE(54, "instrument.pipeorgans.synth_voice"),
        ORCHESTRA_HIT(55, "instrument.pipeorgans.orchestra_hit"),

        TRUMPET(56, "instrument.pipeorgans.trumpet"),
        TROMBONE(57, "instrument.pipeorgans.trombone"),
        TUBA(58, "instrument.pipeorgans.tuba"),
        MUTED_TRUMPET(59, "instrument.pipeorgans.muted_trumpet"),
        FRENCH_HORN(60, "instrument.pipeorgans.french_horn"),
        BRASS_SECTION(61, "instrument.pipeorgans.brass_section"),
        SYNTH_BRASS_1(62, "instrument.pipeorgans.synth_brass_1"),
        SYNTH_BRASS_2(63, "instrument.pipeorgans.synth_brass_2"),

        SOPRANO_SAX(64, "instrument.pipeorgans.soprano_sax"),
        ALTO_SAX(65, "instrument.pipeorgans.alto_sax"),
        TENOR_SAX(66, "instrument.pipeorgans.tenor_sax"),
        BARITONE_SAX(67, "instrument.pipeorgans.baritone_sax"),
        OBOE(68, "instrument.pipeorgans.oboe"),
        ENGLISH_HORN(69, "instrument.pipeorgans.english_horn"),
        BASSOON(70, "instrument.pipeorgans.bassoon"),
        CLARINET(71, "instrument.pipeorgans.clarinet"),

        PICCOLO(72, "instrument.pipeorgans.piccolo"),
        FLUTE(73, "instrument.pipeorgans.flute"),
        RECORDER(74, "instrument.pipeorgans.recorder"),
        PAN_FLUTE(75, "instrument.pipeorgans.pan_flute"),
        BLOWN_BOTTLE(76, "instrument.pipeorgans.blown_bottle"),
        SHAKUHACHI(77, "instrument.pipeorgans.shakuhachi"),
        WHISTLE(78, "instrument.pipeorgans.whistle"),
        OCARINA(79, "instrument.pipeorgans.ocarina"),

        LEAD_1_SQUARE(80, "instrument.pipeorgans.lead_1_square"),
        LEAD_2_SAWTOOTH(81, "instrument.pipeorgans.lead_2_sawtooth"),
        LEAD_3_CALLIOPE(82, "instrument.pipeorgans.lead_3_calliope"),
        LEAD_4_CHIFF(83, "instrument.pipeorgans.lead_4_chiff"),
        LEAD_5_CHARANG(84, "instrument.pipeorgans.lead_5_charang"),
        LEAD_6_VOICE(85, "instrument.pipeorgans.lead_6_voice"),
        LEAD_7_FIFTHS(86, "instrument.pipeorgans.lead_7_fifths"),
        LEAD_8_BASS_LEAD(87, "instrument.pipeorgans.lead_8_bass_lead"),

        PAD_1_NEW_AGE(88, "instrument.pipeorgans.pad_1_new_age"),
        PAD_2_WARM(89, "instrument.pipeorgans.pad_2_warm"),
        PAD_3_POLYSYNTH(90, "instrument.pipeorgans.pad_3_polysynth"),
        PAD_4_CHOIR(91, "instrument.pipeorgans.pad_4_choir"),
        PAD_5_BOWED(92, "instrument.pipeorgans.pad_5_bowed"),
        PAD_6_METALLIC(93, "instrument.pipeorgans.pad_6_metallic"),
        PAD_7_HALO(94, "instrument.pipeorgans.pad_7_halo"),
        PAD_8_SWEEP(95, "instrument.pipeorgans.pad_8_sweep"),

        FX_1_RAIN(96, "instrument.pipeorgans.fx_1_rain"),
        FX_2_SOUNDTRACK(97, "instrument.pipeorgans.fx_2_soundtrack"),
        FX_3_CRYSTAL(98, "instrument.pipeorgans.fx_3_crystal"),
        FX_4_ATMOSPHERE(99, "instrument.pipeorgans.fx_4_atmosphere"),
        FX_5_BRIGHTNESS(100, "instrument.pipeorgans.fx_5_brightness"),
        FX_6_GOBLINS(101, "instrument.pipeorgans.fx_6_goblins"),
        FX_7_ECHOES(102, "instrument.pipeorgans.fx_7_echoes"),
        FX_8_SCI_FI(103, "instrument.pipeorgans.fx_8_sci_fi"),

        SITAR(104, "instrument.pipeorgans.sitar"),
        BANJO(105, "instrument.pipeorgans.banjo"),
        SHAMISEN(106, "instrument.pipeorgans.shamisen"),
        KOTO(107, "instrument.pipeorgans.koto"),
        KALIMBA(108, "instrument.pipeorgans.kalimba"),
        BAGPIPE(109, "instrument.pipeorgans.bagpipe"),
        FIDDLE(110, "instrument.pipeorgans.fiddle"),
        SHANAI(111, "instrument.pipeorgans.shanai"),

        TINKLE_BELL(112, "instrument.pipeorgans.tinkle_bell"),
        AGOGO(113, "instrument.pipeorgans.agogo"),
        STEEL_DRUMS(114, "instrument.pipeorgans.steel_drums"),
        WOODBLOCK(115, "instrument.pipeorgans.woodblock"),
        TAIKO_DRUM(116, "instrument.pipeorgans.taiko_drum"),
        MELODIC_TOM(117, "instrument.pipeorgans.melodic_tom"),
        SYNTH_DRUM(118, "instrument.pipeorgans.synth_drum"),
        REVERSE_CYMBAL(119, "instrument.pipeorgans.reverse_cymbal"),

        GUITAR_FRET_NOISE(120, "instrument.pipeorgans.guitar_fret_noise"),
        BREATH_NOISE(121, "instrument.pipeorgans.breath_noise"),
        SEASHORE(122, "instrument.pipeorgans.seashore"),
        BIRD_TWEET(123, "instrument.pipeorgans.bird_tweet"),
        TELEPHONE_RING(124, "instrument.pipeorgans.telephone_ring"),
        HELICOPTER(125, "instrument.pipeorgans.helicopter"),
        APPLAUSE(126, "instrument.pipeorgans.applause"),
        GUNSHOT(127, "instrument.pipeorgans.gunshot"),

        EMPTY(-1, "instrument.pipeorgans.empty");

        public final String key;
        public final int program;

        GeneralMidiInstrument(int program, String key) {
            this.program = program;
            this.key = key;
        }

        private static final Map<Integer, GeneralMidiInstrument> BY_PROGRAM = new HashMap<>();
        static {
            for (GeneralMidiInstrument instrument : values()) {
                BY_PROGRAM.put(instrument.program, instrument);
            }
        }

        public static GeneralMidiInstrument fromProgram(int program) {
            GeneralMidiInstrument fromProgram = BY_PROGRAM.get(program);
            if (fromProgram == null) return EMPTY;
            return fromProgram;
        }

    }

    public enum GeneralMidiDrumkit {
        STANDARD_DRUM_KIT(0, "instrument.pipeorgans.standard_drum_kit"),
        ROOM_DRUM_KIT(8, "instrument.pipeorgans.room_drum_kit"),
        POWER_DRUM_KIT(16, "instrument.pipeorgans.power_drum_kit"),
        ELECTRIC_DRUM_KIT(24, "instrument.pipeorgans.electric_drum_kit"),
        RAP_TR808_DRUMS(25, "instrument.pipeorgans.rap_tr808_drums"),
        JAZZ_DRUM_KIT(32, "instrument.pipeorgans.jazz_drum_kit"),
        BRUSH_KIT(40, "instrument.pipeorgans.brush_kit"),

        EMPTY(-1, "instrument.pipeorgans.empty");

        public final String key;
        public final int program;

        GeneralMidiDrumkit(int program, String key) {
            this.program = program;
            this.key = key;
        }

        private static final Map<Integer, GeneralMidiDrumkit> BY_PROGRAM = new HashMap<>();
        static {
            for (GeneralMidiDrumkit instrument : values()) {
                BY_PROGRAM.put(instrument.program, instrument);
            }
        }

        public static GeneralMidiDrumkit fromProgram(int program) {
            GeneralMidiDrumkit fromProgram = BY_PROGRAM.get(program);
            if (fromProgram == null) return STANDARD_DRUM_KIT;
            return fromProgram;
        }

    }
}
