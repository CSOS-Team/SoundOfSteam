package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.PipeOrgans;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = PipeOrgans.MOD_ID)
public final class KeyboardRelayEvents {

    private KeyboardRelayEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity()
                .getPersistentData()
                .remove("UsingKBRelayPos");
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        event.getEntity()
                .getPersistentData()
                .remove("UsingKBRelayPos");
    }
}