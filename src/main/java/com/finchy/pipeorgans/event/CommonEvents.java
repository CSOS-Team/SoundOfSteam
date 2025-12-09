package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent event) {
        PipeOrgans.MIDI_RECEIVER.tick();
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event) {
        PipeOrgans.MIDI_RECEIVER.shutdown();
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        TrackerBarBlockEntity.registerCapabilities(event);
    }

}
