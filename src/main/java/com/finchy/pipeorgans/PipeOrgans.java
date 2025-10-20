package com.finchy.pipeorgans;

import com.finchy.pipeorgans.data.PipeOrgansDatagen;
import com.finchy.pipeorgans.init.*;
import com.finchy.pipeorgans.midi.Proxy;
import com.finchy.pipeorgans.midi.client.ClientProxy;
import com.finchy.pipeorgans.midi.server.ServerMidiLoader;
import com.finchy.pipeorgans.midi.server.ServerProxy;
import com.finchy.pipeorgans.network.AllPackets;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PipeOrgans.MOD_ID)
public class PipeOrgans {

    static {
        setProxy(DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new));
    }

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "pipeorgans";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public static final ServerMidiLoader MIDI_RECEIVER = new ServerMidiLoader();

    protected static Proxy proxy;
    public static Proxy getProxy() {
        return proxy;
    }
    public static void setProxy(Proxy inProxy) {
        proxy = inProxy;
    }

    public PipeOrgans() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(modEventBus);

        AllCreativeModeTabs.register(modEventBus);

        AllBlocks.register();
        AllBlockEntities.register();
        AllDisplaySources.register();
        AllItems.register();
        AllSoundEvents.register(modEventBus);
        AllSpriteShifts.register();
        AllParticleTypes.register(modEventBus);
        AllMenuTypes.register();
        AllPackets.registerPackets();

        modEventBus.addListener(EventPriority.LOWEST, PipeOrgansDatagen::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PipeOrgansClient.onCtorClient(modEventBus, MinecraftForge.EVENT_BUS));

        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        proxy.init();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            AllPartialModels.init();
        }
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
