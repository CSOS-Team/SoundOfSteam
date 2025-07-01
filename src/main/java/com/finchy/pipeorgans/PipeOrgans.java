package com.finchy.pipeorgans;

import com.finchy.pipeorgans.init.*;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.util.RegistrateDistExecutor;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PipeOrgans.MOD_ID)
public class PipeOrgans
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "pipeorgans";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public PipeOrgans(IEventBus modEventBus, ModContainer container)
    {
        modEventBus.addListener(this::commonSetup);
        AllCreativeModeTabs.register(modEventBus);
        AllBlockEntities.register(modEventBus);
        AllBlocks.register(modEventBus);
        AllItems.register(modEventBus);
        AllSoundEvents.register(modEventBus);

        RegistrateDistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PipeOrgansClient.onCtorClient(modEventBus, NeoForge.EVENT_BUS));

        NeoForge.EVENT_BUS.register(this);

        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
