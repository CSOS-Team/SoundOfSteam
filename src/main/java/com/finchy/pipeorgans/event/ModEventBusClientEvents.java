package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.pipes.diapason.DiapasonRenderer;
import com.finchy.pipeorgans.block.pipes.gamba.GambaRenderer;
import com.finchy.pipeorgans.block.pipes.gedeckt.GedecktRenderer;
import com.finchy.pipeorgans.block.pipes.nasard.NasardRenderer;
import com.finchy.pipeorgans.block.pipes.piccolo.PiccoloRenderer;
import com.finchy.pipeorgans.block.pipes.posaune.PosauneRenderer;
import com.finchy.pipeorgans.block.pipes.subbass.SubbassRenderer;
import com.finchy.pipeorgans.block.pipes.trompette.TrompetteRenderer;
import com.finchy.pipeorgans.init.AllBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

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
    }
}
