package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.PipeOrgansClient;
import com.finchy.pipeorgans.gui.ClientsideGUIWrapper;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.util.Keybinding;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class ClientEvents {
    @EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBusClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            AllPartialModels.init();
        }

        /*
        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {

        }
         */

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Keybinding.MIDI_CONFIG_KEY);
        }
    }

    @EventBusSubscriber(modid = PipeOrgans.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (Keybinding.MIDI_CONFIG_KEY.consumeClick()) {
                ClientsideGUIWrapper.openMidiConfigGUI(Minecraft.getInstance().level);
            }
        }

        @SubscribeEvent
        public static void onTick(ClientTickEvent event) {
            if (!isGameActive())
                return;
            PipeOrgansClient.MIDI_SENDER.tick();
        }
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
