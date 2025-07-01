package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.AllPartialModels;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = PipeOrgans.MOD_ID, dist = Dist.CLIENT)
public class PipeOrgansClient {

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        AllPartialModels.init();
        modEventBus.addListener(PipeOrgansClient::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
    }

}
