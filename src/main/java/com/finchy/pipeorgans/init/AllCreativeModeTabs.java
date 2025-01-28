package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PipeOrgans.MOD_ID);

    public static final RegistryObject<CreativeModeTab> PIPE_ORGANS_GREAT = CREATIVE_MODE_TABS.register("pipe_organs_great",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AllBlocks.DIAPASON.get()))
                    .title(Component.translatable("creativetab.pipe_organs_great"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(AllBlocks.PICCOLO.get());
                        output.accept(AllBlocks.DIAPASON.get());
                        output.accept(AllBlocks.TROMPETTE.get());

                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> PIPE_ORGANS_SWELL = CREATIVE_MODE_TABS.register("pipe_organs_swell",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AllBlocks.GEDECKT.get()))
                    .title(Component.translatable("creativetab.pipe_organs_swell"))
                    .withTabsBefore(PIPE_ORGANS_GREAT.getKey())
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(AllBlocks.GEDECKT.get());
                        output.accept(AllBlocks.GAMBA.get());
                        output.accept(com.simibubi.create.AllBlocks.STEAM_WHISTLE.get());

                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> PIPE_ORGANS_PEDAL = CREATIVE_MODE_TABS.register("pipe_organs_pedal",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AllBlocks.SUBBASS.get()))
                    .title(Component.translatable("creativetab.pipe_organs_pedal"))
                    .withTabsBefore(PIPE_ORGANS_SWELL.getKey())
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(AllBlocks.SUBBASS.get());;

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
