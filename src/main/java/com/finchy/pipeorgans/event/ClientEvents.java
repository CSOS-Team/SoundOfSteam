package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.PipeOrgansClient;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterRenderer;
import com.finchy.pipeorgans.gui.ClientsideGUIWrapper;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.util.Keybinding;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBusClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            AllPartialModels.init();
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {

            event.registerBlockEntityRenderer(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), StopMasterRenderer::new);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Keybinding.MIDI_CONFIG_KEY);
        }
    }

    @Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (Keybinding.MIDI_CONFIG_KEY.consumeClick()) {
                ClientsideGUIWrapper.openMidiConfigGUI(Minecraft.getInstance().level);
                PipeOrgansClient.MIDI_SENDER.refresh();
                PipeOrgansClient.MIDI_SENDER.startNewUpload("test.mid");
            }
        }

        @SubscribeEvent
        public static void onTick(TickEvent.ClientTickEvent event) {
            if (!isGameActive())
                return;
            PipeOrgansClient.MIDI_SENDER.tick();
        }
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
