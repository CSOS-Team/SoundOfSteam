package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllParticleTypes;
import com.finchy.pipeorgans.midi.client.ClientMidiLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = PipeOrgans.MOD_ID, dist = Dist.CLIENT)
public class PipeOrgansClient {

    public PipeOrgansClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static final ClientMidiLoader MIDI_SENDER = new ClientMidiLoader();

    public static void onCtorClient(IEventBus modEventBus) {
        AllPartialModels.init();
        modEventBus.addListener(PipeOrgansClient::clientInit);
        modEventBus.addListener(AllParticleTypes::registerFactories);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
    }

}
