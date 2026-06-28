package com.finchy.pipeorgans;

import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.CAPDirection;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue DISPLAY_MUTATION_SOUNDING_PITCH = BUILDER
            .comment("Whether to display the sounding pitch on mutation pipes while wearing goggles.")
            .define("displayMutationSoundingPitch", true);

    public static final ForgeConfigSpec.BooleanValue SHOW_OCTAVE_BRACKETS = BUILDER
            .comment("If true, octave values in goggle tooltips are shown in parentheses.")
            .define("showOctaveBrackets", false);


    //Sound Config
    public static final ForgeConfigSpec.DoubleValue WHISTLE_CHIFF_VOLUME = BUILDER
            .comment("Volume multiplier for whistle chiff sounds")
            .defineInRange("sounds.whistleChiffVolume", 1.0, 0.0, 2.0);

    public static final ForgeConfigSpec.DoubleValue PIPE_ATTENUATION_DISTANCE = BUILDER
            .comment("How far away you can hear pipes")
            .defineInRange("sounds.pipeAttenuationDistance", 64.0, 1.0, 256.0);

    public static final ForgeConfigSpec.DoubleValue PIPE_FADE_SPEED = BUILDER
            .comment("How fast pipe sounds fade out per tick (higher = faster)")
            .defineInRange("sounds.pipeFadeSpeed", 0.25d, 0.01d, 0.5d);

    public static final ForgeConfigSpec.IntValue MAX_SOUND_SOURCES = BUILDER
            .comment("Max simultaneous sound channels. Raises Minecraft's 255 cap so large organs don't cut out.", "Requires a restart. Higher values use more RAM/CPU.")
            .defineInRange("sounds.maxSoundSources", 512, 256, 2048);

    //Clipboard Assisted Placement Config (CAP)
    public static final ForgeConfigSpec.BooleanValue CAP_ENABLED = BUILDER
            .comment("Enable the clipboard-assisted placement mechanic.")
            .define("clipboardAssistedPlacement.enabled", true);

    public static final ForgeConfigSpec.EnumValue<CAPDirection> CAP_DEFAULT_DIRECTION = BUILDER
            .comment("The default direction for the  clipboard-assisted placement data mutation when placing a block.")
            .defineEnum("clipboardAssistedPlacement.defaultDirection", CAPDirection.FORWARD);

    public static final ForgeConfigSpec.BooleanValue CAP_COPY_MODE = BUILDER
            .comment("If true, clipboard-assisted placement will copy the block's mode (where applicable, e.g. Receiver/Transmitter for Note Links) when placing.")
            .define("clipboardAssistedPlacement.copyMode", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean displayMutationSoundingPitch;
    public static boolean showOctaveBrackets;
    public static double whistleChiffVolume;
    public static double pipeAttenuationDistance;
    public static double pipeFadeSpeed;
    public static int maxSoundSources;
    public static boolean capEnabled;
    public static CAPDirection capDefaultDirection;
    public static boolean capCopyMode;

    // Call this whenever you need to ensure runtime values are up-to-date
    public static void syncFromFile() {
        displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
        showOctaveBrackets = SHOW_OCTAVE_BRACKETS.get();
        whistleChiffVolume = WHISTLE_CHIFF_VOLUME.get();
        pipeAttenuationDistance = PIPE_ATTENUATION_DISTANCE.get();
        pipeFadeSpeed = PIPE_FADE_SPEED.get();
        maxSoundSources = MAX_SOUND_SOURCES.get();
        capEnabled = CAP_ENABLED.get();
        capDefaultDirection = CAP_DEFAULT_DIRECTION.get();
        capCopyMode = CAP_COPY_MODE.get();
    }
}
