package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.midi.client.ClientMidiLoader;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

public class  PipeOrgansClient {

    public static final ClientMidiLoader MIDI_LOADER = new ClientMidiLoader();

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(PipeOrgansClient::clientInit);
        AllPartialModels.init();
    }

    public static void clientInit(final FMLClientSetupEvent event) {
    }

}
