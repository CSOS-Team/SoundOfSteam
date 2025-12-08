package com.finchy.pipeorgans;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
@Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue MIDI_FILE_SIZE_LIMIT = BUILDER
            .comment("Modify the max upload size for midi files")
            .defineInRange("midiFileSizeLimit", 256, 64, 1024);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static long midiFileSizeLimit;

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            midiFileSizeLimit = MIDI_FILE_SIZE_LIMIT.get();
        }
    }
    @SubscribeEvent
    public static void onReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            midiFileSizeLimit = MIDI_FILE_SIZE_LIMIT.get();
        }
    }
}
