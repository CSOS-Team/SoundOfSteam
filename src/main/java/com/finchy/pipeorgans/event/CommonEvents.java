package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;
        PipeOrgans.MIDI_RECEIVER.tick();
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event) {
        PipeOrgans.MIDI_RECEIVER.shutdown();
    }

}
