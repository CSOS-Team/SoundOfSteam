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
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PipeOrgans.MOD_ID)
public class PipeOrgans {

    static {
        if (FMLEnvironment.dist == Dist.CLIENT) setProxy(new ClientProxy());
        else setProxy(new ServerProxy());
    }

    public static final String MOD_ID = "pipeorgans";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public static final ServerMidiLoader MIDI_RECEIVER = new ServerMidiLoader();

    protected static Proxy proxy;
    public static Proxy getProxy() {
        return proxy;
    }
    public static void setProxy(Proxy inProxy) {
        proxy = inProxy;
    }

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
        AllParticleTypes.register(modEventBus);
        AllMenuTypes.register();
        AllPackets.register();

        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        modEventBus.addListener(EventPriority.LOWEST, PipeOrgansDatagen::gatherData);

    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
