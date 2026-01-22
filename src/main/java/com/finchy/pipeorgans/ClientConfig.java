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
    public static boolean capEnabled;
    public static CAPDirection capDefaultDirection;
    public static boolean capCopyMode;

    // Call this whenever you need to ensure runtime values are up-to-date
    public static void syncFromFile() {
        displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
        showOctaveBrackets = SHOW_OCTAVE_BRACKETS.get();
        capEnabled = CAP_ENABLED.get();
        capDefaultDirection = CAP_DEFAULT_DIRECTION.get();
        capCopyMode = CAP_COPY_MODE.get();
    }
}
