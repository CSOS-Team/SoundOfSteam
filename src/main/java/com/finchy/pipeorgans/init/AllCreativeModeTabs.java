package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries. DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PipeOrgans.MOD_ID);

    public static final RegistryObject<CreativeModeTab> PIPE_ORGANS = CREATIVE_MODE_TABS.register("pipe_organs",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AllBlocks.DIAPASON.get()))
                    .title(Component.translatable("pipeorgans.creativetab.pipes"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(AllBlocks.PICCOLO.get());
                        output.accept(AllBlocks.DIAPASON.get());
                        output.accept(AllBlocks.PRESTANT.get());
                        output.accept(AllBlocks.TROMPETTE.get());
                        output.accept(AllBlocks.CHAMADE.get());
                        output.accept(AllBlocks.ENGLISH_HORN.get());
                        output.accept(AllBlocks.GEDECKT.get());
                        output.accept(AllBlocks.ROHRFLOTE.get());
                        output.accept(AllBlocks.HOHLFLUTE.get());
                        output.accept(AllBlocks.GAMBA.get());
                        output.accept(AllBlocks.NASARD.get());
                        output.accept(AllBlocks.TIERCE.get());
                        output.accept(AllBlocks.SUBBASS.get());
                        output.accept(AllBlocks.OPEN_WOOD.get());
                        output.accept(AllBlocks.BASSOON.get());
                        output.accept(AllBlocks.POSAUNE.get());
                        output.accept(AllBlocks.VOX_HUMANA.get());
                        output.accept(AllBlocks.VIOLA.get());
                        output.accept(AllBlocks.VOX_CELESTE.get());
                        output.accept(com.simibubi.create.AllBlocks.STEAM_WHISTLE.get());
                        output.accept(AllBlocks.HAUNTED_WHISTLE.get());
                        output.accept(AllBlocks.BASE.get());
                        output.accept(AllBlocks.WINDCHEST_MASTER.get());
                        output.accept(AllBlocks.WINDCHEST.get());
                        output.accept(AllBlocks.TREMULANT.get());
                        output.accept(AllBlocks.NOTE_LINK.get());
                        output.accept(AllItems.BRASS_BOOT.get());
                        output.accept(AllItems.COPPER_BOOT.get());
                        output.accept(AllItems.IRON_BOOT.get());
                        output.accept(AllItems.DARK_OAK_BOOT.get());
                        output.accept(AllItems.BRASSBOUND_BOOT.get());
                        output.accept(AllItems.BRASS_REED.get());
                        output.accept(AllItems.TUNING_WIRE.get());
                        output.accept(AllBlocks.KEYBOARD_RELAY.get());
                        output.accept(AllBlocks.TRACKER_BAR.get());
                        output.accept(AllBlocks.ROLL_PUNCHER.get());
                        output.accept(AllItems.MUSIC_ROLL.get());
                        output.accept(AllBlocks.CRASH_CYMBAL.get());
                        output.accept(AllBlocks.SNARE_DRUM.get());
                        output.accept(AllBlocks.BASS_DRUM.get());
                        output.accept(AllBlocks.TAP_CYMBAL.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
