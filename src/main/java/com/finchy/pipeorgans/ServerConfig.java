package com.finchy.pipeorgans;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
@Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue MIDI_FILE_SIZE_LIMIT = BUILDER
            .comment("The maximum allowed file size of uploaded MIDI files. [in KiloBytes]")
            .defineInRange("midi.midiFileSizeLimit", 256, 64, 1024);

    private static final ForgeConfigSpec.IntValue MAX_MIDI_FILES = BUILDER
            .comment("The amount of MIDI files a player can upload until previous ones are overwritten.")
            .defineInRange("midi.maxMidiFiles", 16, 1, 128);

    private static final ForgeConfigSpec.IntValue MAX_MIDI_PACKET_SIZE = BUILDER
            .comment("The maximum packet size uploaded MIDI files are split into. [in Bytes]")
            .defineInRange("midi.maxMidiPacketSize", 1024, 256, 32767);

    private static final ForgeConfigSpec.IntValue MIDI_IDLE_TIMEOUT = BUILDER
            .comment("Amount of game ticks without new packets arriving until an active MIDI upload process is discarded.")
            .defineInRange("midi.idiIdleTimeout", 600, 100, 1200);

    private static final ForgeConfigSpec.BooleanValue CLIPBOARD_ASSISTED_PLACEMENT_ENABLED = BUILDER
            .comment("Enable clipboard-assisted placement mechanic on the server. Players can still have it disabled on the client side.")
            .worldRestart()
            .define("clipboardAssistedPlacementEnabled", true);
/*
    private static final ForgeConfigSpec.IntValue TRACKER_BAR_IMPACT = BUILDER
            .comment("Stress impact multiplier for the Tracker Bar")
            .defineInRange("kinetics.trackerBar.trackerBarImpact", 4, 0, 4096);

 */

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static long midiFileSizeLimit;
    public static int maxMidiFiles;
    public static long maxMidiPacketSize;
    public static int midiIdleTimeout;
    public static boolean clipboardAssistedPlacementEnabled;
    //public static int trackerBarImpact;


    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            midiFileSizeLimit = MIDI_FILE_SIZE_LIMIT.get();
            maxMidiFiles = MAX_MIDI_FILES.get();
            maxMidiPacketSize = MAX_MIDI_PACKET_SIZE.get();
            midiIdleTimeout = MIDI_IDLE_TIMEOUT.get();
            clipboardAssistedPlacementEnabled = CLIPBOARD_ASSISTED_PLACEMENT_ENABLED.get();
           // trackerBarImpact = TRACKER_BAR_IMPACT.get();
        }
    }
    @SubscribeEvent
    public static void onReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            midiFileSizeLimit = MIDI_FILE_SIZE_LIMIT.get();
            maxMidiFiles = MAX_MIDI_FILES.get();
            maxMidiPacketSize = MAX_MIDI_PACKET_SIZE.get();
            midiIdleTimeout = MIDI_IDLE_TIMEOUT.get();
            clipboardAssistedPlacementEnabled = CLIPBOARD_ASSISTED_PLACEMENT_ENABLED.get();
          //  trackerBarImpact = TRACKER_BAR_IMPACT.get();
        }
    }
}
