package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlock;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlock;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlock;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockItem;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlock;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonBlock;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonExtensionBlock;
import com.finchy.pipeorgans.content.pipes.gamba.GambaBlock;
import com.finchy.pipeorgans.content.pipes.gamba.GambaExtensionBlock;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktBlock;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktExtensionBlock;
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
import com.finchy.pipeorgans.content.pipes.subbass.SubbassBlock;
import com.finchy.pipeorgans.content.pipes.subbass.SubbassExtensionBlock;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteBlock;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteExtensionBlock;
import com.finchy.pipeorgans.content.pipes.viola.ViolaBlock;
import com.finchy.pipeorgans.content.pipes.viola.ViolaExtensionBlock;
import com.finchy.pipeorgans.content.pipes.voxHumana.VoxHumanaBlock;
import com.finchy.pipeorgans.content.pipes.voxHumana.VoxHumanaExtensionBlock;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.content.windchest.WindchestMasterBlock;
import com.finchy.pipeorgans.data.AssetLookup;
import com.finchy.pipeorgans.data.BlockStateGen.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS);
    }

    // declare blocks here

    public static final BlockEntry<BaseBlock> BASE = REGISTRATE.block("base", BaseBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .tag(AllTags.AllBlockTags.VALID_WHISTLE.tag)
            .transform(pickaxeOnly())
            .lang("Pipe Base") // override default name generated from id "base"
            .blockstate(new BaseGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<KeyboardRelayBlock> KEYBOARD_RELAY = REGISTRATE.block("keyboard_relay", KeyboardRelayBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TrackerBarBlock> TRACKER_BAR = REGISTRATE.block("tracker_bar", TrackerBarBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .simpleItem()
            .register();

    public static final BlockEntry<WindchestBlock> WINDCHEST = REGISTRATE.block("windchest", WindchestBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .noOcclusion())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .item()
            .transform(customItemModel())
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
            .register();

    public static final BlockEntry<StopMasterBlock> STOP_MASTER = REGISTRATE.block("stop_master", StopMasterBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
            )
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .item(StopMasterBlockItem::new)
            .transform(customItemModel())
            .register();



    public static final BlockEntry<DiapasonBlock> DIAPASON = registerPipeBlock(
            "diapason",
            DiapasonBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK,
            StopSize.EIGHT);

    public static final BlockEntry<DiapasonExtensionBlock> DIAPASON_EXTENSION = registerExtensionBlock(
            "diapason_extension",
            DiapasonExtensionBlock::new,
            com.simibubi.create.AllBlocks.ZINC_BLOCK);

    public static final BlockEntry<GambaBlock> GAMBA = registerPipeBlock(
            "gamba",
            GambaBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.FOUR);

    public static final BlockEntry<GambaExtensionBlock> GAMBA_EXTENSION = registerExtensionBlock(
            "gamba_extension",
            GambaExtensionBlock::new,
            () -> Blocks.IRON_BLOCK);

    public static final BlockEntry<GedecktBlock> GEDECKT = registerPipeBlock(
            "gedeckt",
            GedecktBlock::new,
            () -> Blocks.SPRUCE_PLANKS,
            StopSize.EIGHT);

    public static final BlockEntry<GedecktExtensionBlock> GEDECKT_EXTENSION = registerExtensionBlock(
            "gedeckt_extension",
            GedecktExtensionBlock::new,
            () -> Blocks.SPRUCE_PLANKS);

    public static final BlockEntry<NasardBlock> NASARD = registerPipeBlock(
            "nasard",
            NasardBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.TWOANDTWOTHIRDS);

    public static final BlockEntry<NasardExtensionBlock> NASARD_EXTENSION = registerExtensionBlock(
            "nasard_extension",
            NasardExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK);

    public static final BlockEntry<PiccoloBlock> PICCOLO = registerPipeBlock(
            "piccolo",
            PiccoloBlock::new,
            () -> Blocks.IRON_BLOCK,
            StopSize.TWO);

    public static final BlockEntry<PiccoloExtensionBlock> PICCOLO_EXTENSION = registerExtensionBlock(
            "piccolo_extension",
            PiccoloExtensionBlock::new,
            () -> Blocks.IRON_BLOCK);

    public static final BlockEntry<PosauneBlock> POSAUNE = registerPipeBlock(
            "posaune",
            PosauneBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.THIRTYTWO);

    public static final BlockEntry<PosauneExtensionBlock> POSAUNE_EXTENSION = registerExtensionBlock(
            "posaune_extension",
            PosauneExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS);

    public static final BlockEntry<SubbassBlock> SUBBASS = registerPipeBlock(
            "subbass",
            SubbassBlock::new,
            () -> Blocks.DARK_OAK_PLANKS,
            StopSize.SIXTEEN);

    public static final BlockEntry<SubbassExtensionBlock> SUBBASS_EXTENSION = registerExtensionBlock(
            "subbass_extension",
            SubbassExtensionBlock::new,
            () -> Blocks.DARK_OAK_PLANKS);

    public static final BlockEntry<TrompetteBlock> TROMPETTE = registerPipeBlock(
            "trompette",
            TrompetteBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK,
            StopSize.EIGHT);

    public static final BlockEntry<TrompetteExtensionBlock> TROMPETTE_EXTENSION = registerExtensionBlock(
            "trompette_extension",
            TrompetteExtensionBlock::new,
            com.simibubi.create.AllBlocks.BRASS_BLOCK);

    public static final BlockEntry<ViolaBlock> VIOLA = registerPipeBlock(
            "viola",
            ViolaBlock::new,
            com.simibubi.create.AllBlocks.WEATHERED_IRON_BLOCK,
            StopSize.EIGHT);

    public static final BlockEntry<ViolaExtensionBlock> VIOLA_EXTENSION = registerExtensionBlock(
            "viola_extension",
            ViolaExtensionBlock::new,
            com.simibubi.create.AllBlocks.WEATHERED_IRON_BLOCK);

    public static final BlockEntry<VoxHumanaBlock> VOX_HUMANA = registerPipeBlock(
            "vox_humana",
            VoxHumanaBlock::new,
            () -> Blocks.COPPER_BLOCK,
            StopSize.EIGHT);

    public static final BlockEntry<VoxHumanaExtensionBlock> VOX_HUMANA_EXTENSION = registerExtensionBlock(
            "vox_humana_extension",
            VoxHumanaExtensionBlock::new,
            () -> Blocks.COPPER_BLOCK);

    private static <T extends GenericPipeBlock> BlockEntry<T> registerPipeBlock(
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier,
            StopSize stopsize) {
        return REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .properties(p -> p.requiresCorrectToolForDrops())
                .tag(AllTags.AllBlockTags.VALID_WHISTLE.tag)
                .blockstate(new PipeGenerator()::generate)
                .item((b,p) -> new GenericPipeBlockItem(b, p, stopsize))
                .transform(customItemModel())
                .register();
    }

    private static <T extends GenericExtensionBlock> BlockEntry<T> registerExtensionBlock(
            String name, NonNullFunction<BlockBehaviour.Properties, T> factory,
            NonNullSupplier<? extends Block> initialPropertiesCopier) {
        return REGISTRATE.block(name, factory)
                .initialProperties(initialPropertiesCopier)
                .blockstate(new PipeExtensionGenerator()::generate)
                .register();
    }


    public static void register() {
    }
}
