package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllParticleTypes;
import com.finchy.pipeorgans.midi.client.ClientMidiFileLoader;
import com.finchy.pipeorgans.ponder.POPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = PipeOrgans.MOD_ID, dist = Dist.CLIENT)
public class PipeOrgansClient {
    
    public PipeOrgansClient(IEventBus modEventBus) {
        modEventBus.addListener(PipeOrgansClient::clientInit);
        modEventBus.addListener(AllParticleTypes::registerFactories);
    }

    public static final ClientMidiFileLoader MIDI_SENDER = new ClientMidiFileLoader();

    public static void clientInit(final FMLClientSetupEvent event) {
        AllPartialModels.init();
        PonderIndex.addPlugin(new POPonderPlugin());
    }

}
