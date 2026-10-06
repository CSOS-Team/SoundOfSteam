package com.finchy.pipeorgans;

import com.finchy.pipeorgans.content.noteLink.CAPDirection;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs

@EventBusSubscriber
public class ClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_MUTATION_SOUNDING_PITCH = BUILDER
            .comment("Whether to display the sounding pitch on mutation pipes while wearing goggles.")
            .define("displayMutationSoundingPitch", true);

    public static final ModConfigSpec.BooleanValue SHOW_OCTAVE_BRACKETS = BUILDER
            .comment("If true, octave values in goggle tooltips are shown in parentheses.")
            .define("showOctaveBrackets", false);


    //Sound Config
    public static final ModConfigSpec.DoubleValue WHISTLE_CHIFF_VOLUME = BUILDER
            .comment("Volume multiplier for whistle chiff sounds")
            .defineInRange("sounds.whistleChiffVolume", 1.0, 0.0, 2.0);

    public static final ModConfigSpec.DoubleValue PIPE_ATTENUATION_DISTANCE = BUILDER
            .comment("How far away you can hear pipes")
            .defineInRange("sounds.pipeAttenuationDistance", 64.0, 1.0, 256.0);

    public static final ModConfigSpec.DoubleValue PIPE_FADE_SPEED = BUILDER
            .comment("How fast pipe sounds fade out per tick (higher = faster)")
            .defineInRange("sounds.pipeFadeSpeed", 0.25d, 0.01d, 0.5d);

    public static final ModConfigSpec.DoubleValue PIPE_VOLUME = BUILDER
            .comment("Maximum volume per pipe sound source (0.0-1.0).",
                    "Lower this if you get crackling when many pipes play simultaneously.",
                    "At 1.0 (default), 10+ simultaneous pipes can sum past 0 dBFS and clip.")
            .defineInRange("sounds.pipeVolume", 0.5d, 0.01d, 1.0d);

    public static final ModConfigSpec.IntValue MAX_SOUND_SOURCES = BUILDER
            .comment("Max simultaneous sound channels.", "Higher values use more RAM/CPU. Restart to apply.")
            .defineInRange("sounds.maxSoundSources", 512, 256, 2048);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean displayMutationSoundingPitch;
    public static boolean showOctaveBrackets;
    public static double whistleChiffVolume;
    public static double pipeAttenuationDistance;
    public static double pipeFadeSpeed;
    public static double pipeVolume;
    public static int maxSoundSources;

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
            showOctaveBrackets = SHOW_OCTAVE_BRACKETS.get();
            whistleChiffVolume = WHISTLE_CHIFF_VOLUME.get();
            pipeAttenuationDistance = PIPE_ATTENUATION_DISTANCE.get();
            pipeFadeSpeed = PIPE_FADE_SPEED.get();
            pipeVolume = PIPE_VOLUME.get();
            maxSoundSources = MAX_SOUND_SOURCES.get();
        }
    }

    @SubscribeEvent
    public static void onReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
            showOctaveBrackets = SHOW_OCTAVE_BRACKETS.get();
            whistleChiffVolume = WHISTLE_CHIFF_VOLUME.get();
            pipeAttenuationDistance = PIPE_ATTENUATION_DISTANCE.get();
            pipeFadeSpeed = PIPE_FADE_SPEED.get();
            pipeVolume = PIPE_VOLUME.get();
            maxSoundSources = MAX_SOUND_SOURCES.get();
        }
    }

    // Call this whenever you need to ensure runtime values are up-to-date
    public static void syncFromFile() {
        displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
        showOctaveBrackets = SHOW_OCTAVE_BRACKETS.get();
        whistleChiffVolume = WHISTLE_CHIFF_VOLUME.get();
        pipeAttenuationDistance = PIPE_ATTENUATION_DISTANCE.get();
        pipeFadeSpeed = PIPE_FADE_SPEED.get();
        pipeVolume = PIPE_VOLUME.get();
        maxSoundSources = MAX_SOUND_SOURCES.get();
    }
}
