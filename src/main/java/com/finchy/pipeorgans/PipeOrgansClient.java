package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllParticleTypes;
import com.finchy.pipeorgans.midi.client.ClientMidiLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class  PipeOrgansClient {

    public static final ClientMidiLoader MIDI_SENDER = new ClientMidiLoader();

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(PipeOrgansClient::clientInit);
        modEventBus.addListener(AllParticleTypes::registerFactories);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        AllPartialModels.init();
    }

}




// This is a bucket 🪣