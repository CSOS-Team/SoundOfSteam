package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = PipeOrgans.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientItemProperties {

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                ItemProperties.register(
                        AllItems.TUNING_WIRE.get(),
                        PipeOrgans.asResource("variant"),
                        (stack, level, entity, seed) -> {
                            if (!stack.hasCustomHoverName())
                                return 0;

                            //worm moment
                            return stack.getHoverName()
                                    .getString()
                                    .toLowerCase()
                                    .contains("worm") ? 1 : 0;
                        }
                )
        );
    }
}

