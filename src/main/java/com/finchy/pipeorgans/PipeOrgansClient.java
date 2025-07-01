package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PipeOrgansClient {

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(PipeOrgansClient::clientInit);
        AllPartialModels.init();
    }

    public static void clientInit(final FMLClientSetupEvent event) {
    }

}
