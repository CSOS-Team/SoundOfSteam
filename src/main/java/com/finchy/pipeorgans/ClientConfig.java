package com.finchy.pipeorgans;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_MUTATION_SOUNDING_PITCH = BUILDER
            .comment("Whether to display the sounding pitch on mutation pipes while wearing goggles.")
            .define("displayMutationSoundingPitch", true);

    public static final ModConfigSpec.BooleanValue SHOW_OCTAVE_BRACKETS = BUILDER
            .comment("If true, octave values in goggle tooltips are shown in parentheses.")
            .define("showOctaveBrackets", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean displayMutationSoundingPitch;
    public static boolean showOctaveBrackets;

    // Call this whenever you need to ensure runtime values are up-to-date
    public static void syncFromFile() {
        displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
        showOctaveBrackets = SHOW_OCTAVE_BRACKETS.get();
    }
}
