package com.finchy.pipeorgans;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber
public class ServerConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue MIDI_FILE_SIZE_LIMIT = BUILDER
            .comment("The maximum allowed file size of uploaded MIDI files. [in KiloBytes]")
            .defineInRange("midiFileSizeLimit", 256, 64, 1024);

    private static final ModConfigSpec.IntValue MAX_MIDI_FILES = BUILDER
            .comment("The amount of MIDI files a player can upload until previous ones are overwritten.")
            .defineInRange("maxMidiFiles", 16, 1, 128);

    private static final ModConfigSpec.IntValue MAX_MIDI_PACKET_SIZE = BUILDER
            .comment("The maximum packet size uploaded MIDI files are split into. [in Bytes]")
            .defineInRange("maxMidiPacketSize", 1024, 256, 32767);

    private static final ModConfigSpec.IntValue MIDI_IDLE_TIMEOUT = BUILDER
            .comment("Amount of game ticks without new packets arriving until an active MIDI upload process is discarded.")
            .defineInRange("midiIdleTimeout", 600, 100, 1200);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static long midiFileSizeLimit;
    public static int maxMidiFiles;
    public static long maxMidiPacketSize;
    public static int midiIdleTimeout;

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            midiFileSizeLimit = MIDI_FILE_SIZE_LIMIT.get();
            maxMidiFiles = MAX_MIDI_FILES.get();
            maxMidiPacketSize = MAX_MIDI_PACKET_SIZE.get();
            midiIdleTimeout = MIDI_IDLE_TIMEOUT.get();
        }
    }
    @SubscribeEvent
    public static void onReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            midiFileSizeLimit = MIDI_FILE_SIZE_LIMIT.get();
            maxMidiFiles = MAX_MIDI_FILES.get();
            maxMidiPacketSize = MAX_MIDI_PACKET_SIZE.get();
            midiIdleTimeout = MIDI_IDLE_TIMEOUT.get();
        }
    }
}
