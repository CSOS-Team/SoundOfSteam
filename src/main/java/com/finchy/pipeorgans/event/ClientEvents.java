package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterRenderer;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonRenderer;
import com.finchy.pipeorgans.content.pipes.gamba.GambaRenderer;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktRenderer;
import com.finchy.pipeorgans.content.pipes.nasard.NasardRenderer;
import com.finchy.pipeorgans.content.pipes.piccolo.PiccoloRenderer;
import com.finchy.pipeorgans.content.pipes.posaune.PosauneRenderer;
import com.finchy.pipeorgans.content.pipes.subbass.SubbassRenderer;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteRenderer;
import com.finchy.pipeorgans.content.pipes.vox_humana.VoxHumanaRenderer;
import com.finchy.pipeorgans.gui.ClientsideGUIWrapper;
import com.finchy.pipeorgans.gui.StopMasterScreen;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllMenuTypes;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.util.Keybinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBusClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            AllPartialModels.init();
            MenuScreens.register(AllMenuTypes.STOP_MASTER_MENU.get(), StopMasterScreen::new);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(AllBlockEntities.GEDECKT_BLOCK_ENTITY.get(), GedecktRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.DIAPASON_BLOCK_ENTITY.get(), DiapasonRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.GAMBA_BLOCK_ENTITY.get(), GambaRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.PICCOLO_BLOCK_ENTITY.get(), PiccoloRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.SUBBASS_BLOCK_ENTITY.get(), SubbassRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.TROMPETTE_BLOCK_ENTITY.get(), TrompetteRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.NASARD_BLOCK_ENTITY.get(), NasardRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.POSAUNE_BLOCK_ENTITY.get(), PosauneRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntities.VOX_HUMANA_BLOCK_ENTITY.get(), VoxHumanaRenderer::new);

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
            }
        }
    }
}
