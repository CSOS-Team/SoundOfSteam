package com.finchy.pipeorgans;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
@EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_MUTATION_SOUNDING_PITCH = BUILDER
            .comment("Whether to display the sounding pitch on mutation pipes while wearing goggles.")
            .define("displayMutationSoundingPitch", true);


    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean displayMutationSoundingPitch;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        displayMutationSoundingPitch = DISPLAY_MUTATION_SOUNDING_PITCH.get();
    }
}
