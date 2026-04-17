package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlock;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlock;
import com.finchy.pipeorgans.content.midi.rollPuncher.RollPuncherBlock;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlock;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlock;
import com.finchy.pipeorgans.content.noteLink.NoteLinkGenerator;
import com.finchy.pipeorgans.content.pipes.*;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockItem;
import com.finchy.pipeorgans.content.pipes.generic.StopSize;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.content.windchest.WindchestMasterBlock;
import com.finchy.pipeorgans.data.AssetLookup;
import com.finchy.pipeorgans.data.BlockStateGen.*;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;

@SuppressWarnings("SameParameterValue")
public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS);
    }

    // declare blocks here

    public static final BlockEntry<BaseBlock> BASE = REGISTRATE.block("base", BaseBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .tag(AllTags.AllBlockTags.VALID_WHISTLE.tag)
            .transform(pickaxeOnly())
            .blockstate(new BaseGenerator()::generate)
            .item()
            .transform(customItemModel())
            .lang("Pipe Base")
            .register();

    public static final BlockEntry<KeyboardRelayBlock> KEYBOARD_RELAY = REGISTRATE.block("keyboard_relay", KeyboardRelayBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(
                    c.get(),
                    state -> {
                        boolean transmitting = state.getValue(KeyboardRelayBlock.TRANSMITTING);
                        boolean active = state.getValue(KeyboardRelayBlock.ACTIVE);

                        if (transmitting && active)
                            return p.models().getExistingFile(p.modLoc("block/keyboard_relay/keyboard_relay_transmitting"));

                        if (!active)
                            return p.models().getExistingFile(p.modLoc("block/keyboard_relay/keyboard_relay_inactive"));

                        return p.models().getExistingFile(p.modLoc("block/keyboard_relay/keyboard_relay"));
                    },
                    180
            ))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TrackerBarBlock> TRACKER_BAR = REGISTRATE.block("tracker_bar", TrackerBarBlock::new)
            .initialProperties(com.simibubi.create.AllBlocks.BRASS_BLOCK)
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forBooleanProperty(TrackerBarBlock.TRANSMITTING, "transmitting", c, p)))
            .item()
            .transform(customItemModel())
            .transform(displaySource(AllDisplaySources.TRACKER_BAR_FILENAME))
            .transform(displaySource(AllDisplaySources.TRACKER_BAR_BPM))
            .transform(pickaxeOnly())
            .onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 4.0)) // todo: add stress config for this
            .register();

    public static final BlockEntry<WindchestBlock> WINDCHEST = REGISTRATE.block("windchest", WindchestBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .noOcclusion())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .item()
            .transform(customItemModel())
            .transform(axeOnly())
            .register();

    public static final BlockEntry<WindchestMasterBlock> WINDCHEST_MASTER = REGISTRATE.block("windchest_master", WindchestMasterBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .noOcclusion())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .lang("Windchest Controller")
            .item()
            .transform(customItemModel())
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<RollPuncherBlock> ROLL_PUNCHER = REGISTRATE.block("roll_puncher", RollPuncherBlock::new)
            .initialProperties(() -> Blocks.LECTERN)
            .transform(axeOrPickaxe())
            .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), prov.models()
                    .getExistingFile(ctx.getId()), 180))
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.pipeorgans.roll_puncher"))
            .lang("Roll Authoring Table")
            .item()
            .build()
            .register();

    //Pipes go here
    public static List<BlockEntry<? extends GenericPipeBlock>> PIPE_BLOCKS = new ArrayList<>();

    public static final BlockEntry<Diapason.DiapasonBlock> DIAPASON = registerPipeBlock(
            "diapason",
            Diapason.DiapasonBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Diapason.DiapasonExtensionBlock> DIAPASON_EXTENSION = registerExtensionBlock(
            "diapason_extension",
            Diapason.DiapasonExtensionBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<HauntedWhistle.HauntedWhistleBlock> HAUNTED_WHISTLE = registerPipeBlock(
            "haunted_whistle",
            HauntedWhistle.HauntedWhistleBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<HauntedWhistle.HauntedWhistleExtensionBlock> HAUNTED_WHISTLE_EXTENSION = registerExtensionBlock(
            "haunted_whistle_extension",
            HauntedWhistle.HauntedWhistleExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Prestant.PrestantBlock> PRESTANT = registerPipeBlock(
            "prestant",
            Prestant.PrestantBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            StopSize.FOUR,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Prestant.PrestantExtensionBlock> PRESTANT_EXTENSION = registerExtensionBlock(
            "prestant_extension",
            Prestant.PrestantExtensionBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Gamba.GambaBlock> GAMBA = registerPipeBlock(
            "gamba",
            Gamba.GambaBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.FOUR,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Gamba.GambaExtensionBlock> GAMBA_EXTENSION = registerExtensionBlock(
            "gamba_extension",
            Gamba.GambaExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Gedeckt.GedecktBlock> GEDECKT = registerPipeBlock(
            "gedeckt",
            Gedeckt.GedecktBlock::new,
            () -> Blocks.SPRUCE_PLANKS,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_AXE);


    public static final BlockEntry<Gedeckt.GedecktExtensionBlock> GEDECKT_EXTENSION = registerExtensionBlock(
            "gedeckt_extension",
            Gedeckt.GedecktExtensionBlock::new,
            () -> Blocks.SPRUCE_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<Hohlflute.HohlfluteBlock> HOHLFLUTE = registerPipeBlock(
            "hohlflute",
            Hohlflute.HohlfluteBlock::new,
            () -> Blocks.BIRCH_PLANKS,
            StopSize.FOUR,
            BlockTags.MINEABLE_WITH_AXE);


    public static final BlockEntry<Hohlflute.HohlfluteExtensionBlock> HOHLFLUTE_EXTENSION = registerExtensionBlock(
            "hohlflute_extension",
            Hohlflute.HohlfluteExtensionBlock::new,
            () -> Blocks.BIRCH_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<Rohrflote.RohrfloteBlock> ROHRFLOTE = registerPipeBlock(
            "rohrflote",
            Rohrflote.RohrfloteBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Rohrflote.RohrfloteExtensionBlock> ROHRFLOTE_EXTENSION = registerExtensionBlock(
            "rohrflote_extension",
            Rohrflote.RohrfloteExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Nasard.NasardBlock> NASARD = registerPipeBlock(
            "nasard",
            Nasard.NasardBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.TWOANDTWOTHIRDS,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Nasard.NasardExtensionBlock> NASARD_EXTENSION = registerExtensionBlock(
            "nasard_extension",
            Nasard.NasardExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Tierce.TierceBlock> TIERCE = registerPipeBlock(
            "tierce",
            Tierce.TierceBlock::new,
            () -> Blocks.GOLD_BLOCK,
            StopSize.ONEANDTHREEFIFTHS,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Tierce.TierceExtensionBlock> TIERCE_EXTENSION = registerExtensionBlock(
            "tierce_extension",
            Tierce.TierceExtensionBlock::new,
            () -> Blocks.GOLD_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Piccolo.PiccoloBlock> PICCOLO = registerPipeBlock(
            "piccolo",
            Piccolo.PiccoloBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.TWO,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Piccolo.PiccoloExtensionBlock> PICCOLO_EXTENSION = registerExtensionBlock(
            "piccolo_extension",
            Piccolo.PiccoloExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Oktav.OktavBlock> OKTAV = registerPipeBlock(
            "oktav",
            Oktav.OktavBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.TWO,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Oktav.OktavExtensionBlock> OKTAV_EXTENSION = registerExtensionBlock(
            "oktav_extension",
            Oktav.OktavExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Posaune.PosauneBlock> POSAUNE = registerPipeBlock(
            "posaune",
            Posaune.PosauneBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.THIRTYTWO,
            BlockTags.MINEABLE_WITH_AXE);


    public static final BlockEntry<Posaune.PosauneExtensionBlock> POSAUNE_EXTENSION = registerExtensionBlock(
            "posaune_extension",
            Posaune.PosauneExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<Bassoon.BassoonBlock> BASSOON = registerPipeBlock(
            "bassoon",
            Bassoon.BassoonBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.SIXTEEN,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Bassoon.BassoonExtensionBlock> BASSOON_EXTENSION = registerExtensionBlock(
            "bassoon_extension",
            Bassoon.BassoonExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Subbass.SubbassBlock> SUBBASS = registerPipeBlock(
            "subbass",
            Subbass.SubbassBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.SIXTEEN,
            BlockTags.MINEABLE_WITH_AXE);


    public static final BlockEntry<Subbass.SubbassExtensionBlock> SUBBASS_EXTENSION = registerExtensionBlock(
            "subbass_extension",
            Subbass.SubbassExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<Untersatz.UntersatzBlock> UNTERSATZ = registerPipeBlock(
            "untersatz",
            Untersatz.UntersatzBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.THIRTYTWO,
            BlockTags.MINEABLE_WITH_AXE);


    public static final BlockEntry<Untersatz.UntersatzExtensionBlock> UNTERSATZ_EXTENSION = registerExtensionBlock(
            "untersatz_extension",
            Untersatz.UntersatzExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<OpenWood.OpenWoodBlock> OPEN_WOOD = registerPipeBlock(
            "open_wood",
            OpenWood.OpenWoodBlock::new,
            () -> Blocks.MANGROVE_PLANKS,
            StopSize.SIXTEEN,
            BlockTags.MINEABLE_WITH_AXE);


    public static final BlockEntry<OpenWood.OpenWoodExtensionBlock> OPEN_WOOD_EXTENSION = registerExtensionBlock(
            "open_wood_extension",
            OpenWood.OpenWoodExtensionBlock::new,
            () -> Blocks.MANGROVE_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<Trompette.TrompetteBlock> TROMPETTE = registerPipeBlock(
            "trompette",
            Trompette.TrompetteBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Trompette.TrompetteExtensionBlock> TROMPETTE_EXTENSION = registerExtensionBlock(
            "trompette_extension",
            Trompette.TrompetteExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Chamade.ChamadeBlock> CHAMADE = registerPipeBlock(
            "chamade",
            Chamade.ChamadeBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Chamade.ChamadeExtensionBlock> CHAMADE_EXTENSION = registerExtensionBlock(
            "chamade_extension",
            Chamade.ChamadeExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Krummhorn.KrummhornBlock> KRUMMHORN = registerPipeBlock(
            "krummhorn",
            Krummhorn.KrummhornBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Krummhorn.KrummhornExtensionBlock> KRUMMHORN_EXTENSION = registerExtensionBlock(
            "krummhorn_extension",
            Krummhorn.KrummhornExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<EnglishHorn.EnglishHornBlock> ENGLISH_HORN = registerPipeBlock(
            "english_horn",
            EnglishHorn.EnglishHornBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<EnglishHorn.EnglishHornExtensionBlock> ENGLISH_HORN_EXTENSION = registerExtensionBlock(
            "english_horn_extension",
            EnglishHorn.EnglishHornExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<Viola.ViolaBlock> VIOLA = registerPipeBlock(
            "viola",
            Viola.ViolaBlock::new,
            com.simibubi.create.AllBlocks.INDUSTRIAL_IRON_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);


    public static final BlockEntry<Viola.ViolaExtensionBlock> VIOLA_EXTENSION = registerExtensionBlock(
            "viola_extension",
            Viola.ViolaExtensionBlock::new,
            com.simibubi.create.AllBlocks.INDUSTRIAL_IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<VoxCeleste.VoxCelesteBlock> VOX_CELESTE = registerPipeBlock(
            "vox_celeste",
            VoxCeleste.VoxCelesteBlock::new,
            com.simibubi.create.AllBlocks.WEATHERED_IRON_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE,
            "Voix Celeste"); //I didn't misspell it... I SWEAR

    public static final BlockEntry<VoxCeleste.VoxCelesteExtensionBlock> VOX_CELESTE_EXTENSION = registerExtensionBlock(
            "vox_celeste_extension",
            VoxCeleste.VoxCelesteExtensionBlock::new,
            com.simibubi.create.AllBlocks.WEATHERED_IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE,
            "Voix Celeste Extension");

    public static final BlockEntry<VoxHumana.VoxHumanaBlock> VOX_HUMANA = registerPipeBlock(
            "vox_humana",
            VoxHumana.VoxHumanaBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<VoxHumana.VoxHumanaExtensionBlock> VOX_HUMANA_EXTENSION = registerExtensionBlock(
            "vox_humana_extension",
            VoxHumana.VoxHumanaExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    @SuppressWarnings("removal")
    public static final BlockEntry<NoteLinkBlock> NOTE_LINK = REGISTRATE.block("note_link", NoteLinkBlock::new)
            .initialProperties(() -> Blocks.SPRUCE_PLANKS)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).forceSolidOn())
            .transform(axeOrPickaxe())
            .tag(com.simibubi.create.AllTags.AllBlockTags.BRITTLE.tag, com.simibubi.create.AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(new NoteLinkGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)   // Marked as deprecated but Create also uses it with the same version of Registrate, so... idc
            .item()
            .transform(customItemModel("_", "transmitter"))
            .register();


//register block
    //register pipe block
    private static <T extends GenericPipeBlock> BlockEntry<T> registerPipeBlock( // overload for blocks without lang overrides
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier,
            StopSize stopsize, TagKey<Block> toolTag) {

        BlockEntry<T> entry = REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .tag(AllTags.AllBlockTags.VALID_WHISTLE.tag)
                .blockstate(new PipeGenerator()::generate)
                .item((b, p) -> new GenericPipeBlockItem(b, p, stopsize))
                .transform(customItemModel())
                .tag(toolTag)
                .register();

        PIPE_BLOCKS.add(entry);
        return entry;
    }

    private static <T extends GenericPipeBlock> BlockEntry<T> registerPipeBlock( // overload for blocks with lang overrides
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier,
            StopSize stopsize, TagKey<Block> toolTag,
            @NotNull String nameOverride) {

        BlockEntry<T> entry = REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .tag(AllTags.AllBlockTags.VALID_WHISTLE.tag)
                .blockstate(new PipeGenerator()::generate)
                .item((b, p) -> new GenericPipeBlockItem(b, p, stopsize))
                .transform(customItemModel())
                .tag(toolTag)
                .lang(nameOverride)
                .register();

        PIPE_BLOCKS.add(entry);
        return entry;
    }

    // register extension block
    private static <T extends GenericExtensionBlock<?>> BlockEntry<T> registerExtensionBlock( // overload for blocks without lang overrides
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier,
            TagKey<Block> toolTag) {

        return REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .blockstate(new PipeExtensionGenerator()::generate)
                .tag(toolTag)
                .register();
    }

    private static <T extends GenericExtensionBlock<?>> BlockEntry<T> registerExtensionBlock( // overload for blocks with lang overrides
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier,
            TagKey<Block> toolTag,
            @NotNull String nameOverride) {

        return REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .blockstate(new PipeExtensionGenerator()::generate)
                .tag(toolTag)
                .lang(nameOverride)
                .register();
    }



    public static void register() {
    }
}
