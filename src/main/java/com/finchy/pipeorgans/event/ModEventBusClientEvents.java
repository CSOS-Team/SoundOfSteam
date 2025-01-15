package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.gedeckt.GedecktRenderer;
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
    }
}
