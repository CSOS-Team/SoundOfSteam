package com.finchy.pipeorgans;

import com.finchy.pipeorgans.data.PipeOrgansDatagen;
import com.finchy.pipeorgans.data.advancement.AllAdvancements;
import com.finchy.pipeorgans.init.*;
import com.finchy.pipeorgans.midi.server.ServerMidiLoader;
import com.finchy.pipeorgans.network.AllPackets;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PipeOrgans.MOD_ID)
public class PipeOrgans {

    public static final String MOD_ID = "pipeorgans";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public static final ServerMidiLoader MIDI_RECEIVER = new ServerMidiLoader();

    public PipeOrgans(IEventBus modEventBus, ModContainer container)
    {
        REGISTRATE.registerEventListeners(modEventBus);

        AllCreativeModeTabs.register(modEventBus);

        AllBlocks.register();
        AllBlockEntities.register();
        AllDisplaySources.register();
        AllItems.register();
        AllSoundEvents.register(modEventBus);
        AllSpriteShifts.register();
        AllTriggers.register(modEventBus);
        AllParticleTypes.register(modEventBus);
        AllMenuTypes.register();
        AllPackets.register();
        AllDataComponents.register(modEventBus);
        
        modEventBus.addListener(PipeOrgans::init);
        modEventBus.addListener(EventPriority.HIGHEST, PipeOrgansDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, PipeOrgansDatagen::gatherData);
        
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        
    }
    
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(AllAdvancements::register);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
