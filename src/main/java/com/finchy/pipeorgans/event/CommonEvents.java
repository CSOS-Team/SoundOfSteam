package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
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

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        AllCommands.register(event.getDispatcher());
    }

}
