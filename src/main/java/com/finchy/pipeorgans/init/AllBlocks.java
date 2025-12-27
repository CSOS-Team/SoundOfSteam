package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlock;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlock;
import com.finchy.pipeorgans.content.midi.rollPuncher.RollPuncherBlock;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlock;
import com.finchy.pipeorgans.content.pipes.bassoon.BassoonBlock;
import com.finchy.pipeorgans.content.pipes.bassoon.BassoonExtensionBlock;
import com.finchy.pipeorgans.content.pipes.chamade.ChamadeBlock;
import com.finchy.pipeorgans.content.pipes.chamade.ChamadeExtensionBlock;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonBlock;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonExtensionBlock;
import com.finchy.pipeorgans.content.pipes.englishHorn.EnglishHornBlock;
import com.finchy.pipeorgans.content.pipes.englishHorn.EnglishHornExtensionBlock;
import com.finchy.pipeorgans.content.pipes.hauntedWhistle.HauntedWhistleBlock;
import com.finchy.pipeorgans.content.pipes.hauntedWhistle.HauntedWhistleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.openWood.OpenWoodBlock;
import com.finchy.pipeorgans.content.pipes.openWood.OpenWoodExtensionBlock;
import com.finchy.pipeorgans.content.pipes.prestant.PrestantBlock;
import com.finchy.pipeorgans.content.pipes.prestant.PrestantExtensionBlock;
import com.finchy.pipeorgans.content.pipes.gamba.GambaBlock;
import com.finchy.pipeorgans.content.pipes.gamba.GambaExtensionBlock;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktBlock;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktExtensionBlock;
import com.finchy.pipeorgans.content.pipes.hohlflute.HohlfluteBlock;
import com.finchy.pipeorgans.content.pipes.hohlflute.HohlfluteExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockItem;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockItem.StopSize;
import com.finchy.pipeorgans.content.pipes.nasard.NasardBlock;
import com.finchy.pipeorgans.content.pipes.nasard.NasardExtensionBlock;
import com.finchy.pipeorgans.content.pipes.piccolo.PiccoloBlock;
import com.finchy.pipeorgans.content.pipes.piccolo.PiccoloExtensionBlock;
import com.finchy.pipeorgans.content.pipes.posaune.PosauneBlock;
import com.finchy.pipeorgans.content.pipes.posaune.PosauneExtensionBlock;
import com.finchy.pipeorgans.content.pipes.rohrflote.RohrfloteBlock;
import com.finchy.pipeorgans.content.pipes.rohrflote.RohrfloteExtensionBlock;
import com.finchy.pipeorgans.content.pipes.subbass.SubbassBlock;
import com.finchy.pipeorgans.content.pipes.subbass.SubbassExtensionBlock;
import com.finchy.pipeorgans.content.pipes.tierce.TierceBlock;
import com.finchy.pipeorgans.content.pipes.tierce.TierceExtensionBlock;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteBlock;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteExtensionBlock;
import com.finchy.pipeorgans.content.pipes.viola.ViolaBlock;
import com.finchy.pipeorgans.content.pipes.viola.ViolaExtensionBlock;
import com.finchy.pipeorgans.content.pipes.voxCeleste.VoxCelesteBlock;
import com.finchy.pipeorgans.content.pipes.voxCeleste.VoxCelesteExtensionBlock;
import com.finchy.pipeorgans.content.pipes.voxHumana.VoxHumanaBlock;
import com.finchy.pipeorgans.content.pipes.voxHumana.VoxHumanaExtensionBlock;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;

//TODO Roll puncher
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
            .lang("Pipe Base") // override default name generated from id "base"
            .blockstate(new BaseGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<KeyboardRelayBlock> KEYBOARD_RELAY = REGISTRATE.block("keyboard_relay", KeyboardRelayBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forBooleanProperty(KeyboardRelayBlock.TRANSMITTING, "transmitting", c, p)))
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
            .onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 4.0)) // todo: add stress config for this. also probably a minimum speed for the tracker bar
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
            .lang("Windchest Controller")
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .item()
            .transform(customItemModel())
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<RollPuncherBlock> ROLL_PUNCHER = REGISTRATE.block("roll_puncher", RollPuncherBlock::new)
            .initialProperties(() -> Blocks.LECTERN)
            .transform(axeOrPickaxe())
            .lang("Roll Authoring Table")
            .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), prov.models()
                    .getExistingFile(ctx.getId()), 180))
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.pipeorgans.roll_puncher"))
            .item()
            .build()
            .register();



    public static List<BlockEntry<? extends GenericPipeBlock>> PIPE_BLOCKS = new ArrayList<>();

    public static final BlockEntry<DiapasonBlock> DIAPASON = registerPipeBlock(
            "diapason",
            DiapasonBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<DiapasonExtensionBlock> DIAPASON_EXTENSION = registerExtensionBlock(
            "diapason_extension",
            DiapasonExtensionBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<HauntedWhistleBlock> HAUNTED_WHISTLE = registerPipeBlock(
            "haunted_whistle",
            HauntedWhistleBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<HauntedWhistleExtensionBlock> HAUNTED_WHISTLE_EXTENSION = registerExtensionBlock(
            "haunted_whistle_extension",
            HauntedWhistleExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<PrestantBlock> PRESTANT = registerPipeBlock(
            "prestant",
            PrestantBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            StopSize.FOUR,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<PrestantExtensionBlock> PRESTANT_EXTENSION = registerExtensionBlock(
            "prestant_extension",
            PrestantExtensionBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<GambaBlock> GAMBA = registerPipeBlock(
            "gamba",
            GambaBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.FOUR,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<GambaExtensionBlock> GAMBA_EXTENSION = registerExtensionBlock(
            "gamba_extension",
            GambaExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<GedecktBlock> GEDECKT = registerPipeBlock(
            "gedeckt",
            GedecktBlock::new,
            () -> Blocks.SPRUCE_PLANKS,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<GedecktExtensionBlock> GEDECKT_EXTENSION = registerExtensionBlock(
            "gedeckt_extension",
            GedecktExtensionBlock::new,
            () -> Blocks.SPRUCE_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<HohlfluteBlock> HOHLFLUTE = registerPipeBlock(
            "hohlflute",
            HohlfluteBlock::new,
            () -> Blocks.BIRCH_PLANKS,
            StopSize.FOUR,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<HohlfluteExtensionBlock> HOHLFLUTE_EXTENSION = registerExtensionBlock(
            "hohlflute_extension",
            HohlfluteExtensionBlock::new,
            () -> Blocks.BIRCH_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<RohrfloteBlock> ROHRFLOTE = registerPipeBlock(
            "rohrflote",
            RohrfloteBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<RohrfloteExtensionBlock> ROHRFLOTE_EXTENSION = registerExtensionBlock(
            "rohrflote_extension",
            RohrfloteExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<NasardBlock> NASARD = registerPipeBlock(
            "nasard",
            NasardBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.TWOANDTWOTHIRDS,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<NasardExtensionBlock> NASARD_EXTENSION = registerExtensionBlock(
            "nasard_extension",
            NasardExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<TierceBlock> TIERCE = registerPipeBlock(
            "tierce",
            TierceBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.ONEANDTHREEFIFTHS,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<TierceExtensionBlock> TIERCE_EXTENSION = registerExtensionBlock(
            "tierce_extension",
            TierceExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<PiccoloBlock> PICCOLO = registerPipeBlock(
            "piccolo",
            PiccoloBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.TWO,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<PiccoloExtensionBlock> PICCOLO_EXTENSION = registerExtensionBlock(
            "piccolo_extension",
            PiccoloExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<PosauneBlock> POSAUNE = registerPipeBlock(
            "posaune",
            PosauneBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.THIRTYTWO,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<PosauneExtensionBlock> POSAUNE_EXTENSION = registerExtensionBlock(
            "posaune_extension",
            PosauneExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<BassoonBlock> BASSOON = registerPipeBlock(
            "bassoon",
            BassoonBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.SIXTEEN,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<BassoonExtensionBlock> BASSOON_EXTENSION = registerExtensionBlock(
            "bassoon_extension",
            BassoonExtensionBlock::new,
            () -> Blocks.IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<SubbassBlock> SUBBASS = registerPipeBlock(
            "subbass",
            SubbassBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.SIXTEEN,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<SubbassExtensionBlock> SUBBASS_EXTENSION = registerExtensionBlock(
            "subbass_extension",
            SubbassExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<OpenWoodBlock> OPEN_WOOD = registerPipeBlock(
            "open_wood",
            OpenWoodBlock::new,
            () -> Blocks.MANGROVE_PLANKS,
            StopSize.SIXTEEN,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<OpenWoodExtensionBlock> OPEN_WOOD_EXTENSION = registerExtensionBlock(
            "open_wood_extension",
            OpenWoodExtensionBlock::new,
            () -> Blocks.MANGROVE_PLANKS,
            BlockTags.MINEABLE_WITH_AXE);

    public static final BlockEntry<TrompetteBlock> TROMPETTE = registerPipeBlock(
            "trompette",
            TrompetteBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<TrompetteExtensionBlock> TROMPETTE_EXTENSION = registerExtensionBlock(
            "trompette_extension",
            TrompetteExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<ChamadeBlock> CHAMADE = registerPipeBlock(
            "chamade",
            ChamadeBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<ChamadeExtensionBlock> CHAMADE_EXTENSION = registerExtensionBlock(
            "chamade_extension",
            ChamadeExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<EnglishHornBlock> ENGLISH_HORN = registerPipeBlock(
            "english_horn",
            EnglishHornBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<EnglishHornExtensionBlock> ENGLISH_HORN_EXTENSION = registerExtensionBlock(
            "english_horn_extension",
            EnglishHornExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<ViolaBlock> VIOLA = registerPipeBlock(
            "viola",
            ViolaBlock::new,
            com.simibubi.create.AllBlocks.INDUSTRIAL_IRON_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<ViolaExtensionBlock> VIOLA_EXTENSION = registerExtensionBlock(
            "viola_extension",
            ViolaExtensionBlock::new,
            com.simibubi.create.AllBlocks.INDUSTRIAL_IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<VoxCelesteBlock> VOX_CELESTE = registerPipeBlock(
            "vox_celeste",
            VoxCelesteBlock::new,
            com.simibubi.create.AllBlocks.WEATHERED_IRON_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<VoxCelesteExtensionBlock> VOX_CELESTE_EXTENSION = registerExtensionBlock(
            "vox_celeste_extension",
            VoxCelesteExtensionBlock::new,
            com.simibubi.create.AllBlocks.WEATHERED_IRON_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<VoxHumanaBlock> VOX_HUMANA = registerPipeBlock(
            "vox_humana",
            VoxHumanaBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.EIGHT,
            BlockTags.MINEABLE_WITH_PICKAXE);

    public static final BlockEntry<VoxHumanaExtensionBlock> VOX_HUMANA_EXTENSION = registerExtensionBlock(
            "vox_humana_extension",
            VoxHumanaExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK,
            BlockTags.MINEABLE_WITH_PICKAXE);


    private static <T extends GenericPipeBlock> BlockEntry<T> registerPipeBlock(
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier,
            StopSize stopsize, TagKey<Block> toolTag) {
        BlockEntry<T> entry = REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .tag(AllTags.AllBlockTags.VALID_WHISTLE.tag)
                .blockstate(new PipeGenerator()::generate)
                .item((b,p) -> new GenericPipeBlockItem(b, p, stopsize))
                .transform(customItemModel())
                .tag(toolTag)
                .register();
        PIPE_BLOCKS.add(entry);
        return entry;
    }

    private static <T extends GenericExtensionBlock<?>> BlockEntry<T> registerExtensionBlock(
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier, TagKey<Block> toolTag) {
        return REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .blockstate(new PipeExtensionGenerator()::generate)
                .tag(toolTag)
                .register();
    }



    public static void register() {
    }
}
