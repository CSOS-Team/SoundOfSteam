package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllParticleTypes;
import com.finchy.pipeorgans.midi.client.ClientMidiLoader;
import com.finchy.pipeorgans.ponder.POPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
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
        PonderIndex.addPlugin(new POPonderPlugin());
    }

}
