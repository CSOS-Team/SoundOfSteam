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
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockItem;
import com.finchy.pipeorgans.content.pipes.generic.PipeExtensionGenerator;
import com.finchy.pipeorgans.content.pipes.generic.PipeGenerator;
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
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS);
    }

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PipeOrgans.MOD_ID);

    // declare blocks here

    public static final RegistryObject<BaseBlock> BASE = registerBlock("base",
            () -> new BaseBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<KeyboardRelayBlock> KEYBOARD_RELAY = registerBlock("keyboard_relay",
            () -> new KeyboardRelayBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<TrackerBarBlock> TRACKER_BAR = registerBlock("tracker_bar",
            () -> new TrackerBarBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<StopMasterBlock> STOP_MASTER = registerBlockWithoutItem("stop_master",
            () -> new StopMasterBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops().noOcclusion()));
    static {AllItems.ITEMS.register("stop_master", () -> new StopMasterBlockItem(STOP_MASTER.get(), new Item.Properties()));} // register blockitem for stop_master

    public static final RegistryObject<WindchestBlock> WINDCHEST = registerBlock("windchest",
            () -> new WindchestBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<WindchestMasterBlock> WINDCHEST_MASTER = registerBlock("windchest_master",
            () -> new WindchestMasterBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops().noOcclusion()));



    public static final BlockEntry<DiapasonBlock> DIAPASON = REGISTRATE.block("diapason", DiapasonBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DiapasonExtensionBlock> DIAPASON_EXTENSION = REGISTRATE.block("diapason_extension", DiapasonExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<GambaBlock> GAMBA = REGISTRATE.block("gamba", GambaBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<GambaExtensionBlock> GAMBA_EXTENSION = REGISTRATE.block("gamba_extension", GambaExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<GedecktBlock> GEDECKT = REGISTRATE.block("gedeckt", GedecktBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<GedecktExtensionBlock> GEDECKT_EXTENSION = REGISTRATE.block("gedeckt_extension", GedecktExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<NasardBlock> NASARD = REGISTRATE.block("nasard", NasardBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<NasardExtensionBlock> NASARD_EXTENSION = REGISTRATE.block("nasard_extension", NasardExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<PiccoloBlock> PICCOLO = REGISTRATE.block("piccolo", PiccoloBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<PiccoloExtensionBlock> PICCOLO_EXTENSION = REGISTRATE.block("piccolo_extension", PiccoloExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<PosauneBlock> POSAUNE = REGISTRATE.block("posaune", PosauneBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<PosauneExtensionBlock> POSAUNE_EXTENSION = REGISTRATE.block("posaune_extension", PosauneExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<SubbassBlock> SUBBASS = REGISTRATE.block("subbass", SubbassBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SubbassExtensionBlock> SUBBASS_EXTENSION = REGISTRATE.block("subbass_extension", SubbassExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<TrompetteBlock> TROMPETTE = REGISTRATE.block("trompette", TrompetteBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TrompetteExtensionBlock> TROMPETTE_EXTENSION = REGISTRATE.block("trompette_extension", TrompetteExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<ViolaBlock> VIOLA = REGISTRATE.block("viola", ViolaBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ViolaExtensionBlock> VIOLA_EXTENSION = REGISTRATE.block("viola_extension", ViolaExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();

    public static final BlockEntry<VoxHumanaBlock> VOX_HUMANA = REGISTRATE.block("vox_humana", VoxHumanaBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<VoxHumanaExtensionBlock> VOX_HUMANA_EXTENSION = REGISTRATE.block("vox_humana_extension", VoxHumanaExtensionBlock::new)
            .initialProperties(() -> com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
            .transform(pickaxeOnly())
            .blockstate(new PipeExtensionGenerator()::generate)
            .register();


    /*
    public static final RegistryObject<DiapasonBlock> DIAPASON = registerPipeBlock("diapason", "8",
            () -> new DiapasonBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<DiapasonExtensionBlock> DIAPASON_EXTENSION = registerBlockWithoutItem("diapason_extension",
            () -> new DiapasonExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GambaBlock> GAMBA = registerPipeBlock("gamba", "4",
            () -> new GambaBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GambaExtensionBlock> GAMBA_EXTENSION = registerBlockWithoutItem("gamba_extension",
            () -> new GambaExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GedecktBlock> GEDECKT = registerPipeBlock("gedeckt", "8",
            () -> new GedecktBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GedecktExtensionBlock> GEDECKT_EXTENSION = registerBlockWithoutItem("gedeckt_extension",
            () -> new GedecktExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<NasardBlock> NASARD = registerPipeBlock("nasard", "223",
            () -> new NasardBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<NasardExtensionBlock> NASARD_EXTENSION = registerBlockWithoutItem("nasard_extension",
            () -> new NasardExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PiccoloBlock> PICCOLO = registerPipeBlock("piccolo", "2",
            () -> new PiccoloBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PiccoloExtensionBlock> PICCOLO_EXTENSION = registerBlockWithoutItem("piccolo_extension",
            () -> new PiccoloExtensionBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PosauneBlock> POSAUNE = registerPipeBlock("posaune", "32",
            () -> new PosauneBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PosauneExtensionBlock> POSAUNE_EXTENSION = registerBlockWithoutItem("posaune_extension",
            () -> new PosauneExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<SubbassBlock> SUBBASS = registerPipeBlock("subbass",  "16",
            () -> new SubbassBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<SubbassExtensionBlock> SUBBASS_EXTENSION = registerBlockWithoutItem("subbass_extension",
            () -> new SubbassExtensionBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<TrompetteBlock> TROMPETTE = registerPipeBlock("trompette", "8",
            () -> new TrompetteBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<TrompetteExtensionBlock> TROMPETTE_EXTENSION = registerBlockWithoutItem("trompette_extension",
            () -> new TrompetteExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<VoxHumanaBlock> VOX_HUMANA = registerPipeBlock("vox_humana", "8",
            () -> new VoxHumanaBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<VoxHumanaExtensionBlock> VOX_HUMANA_EXTENSION = registerBlockWithoutItem("vox_humana_extension",
            () -> new VoxHumanaExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<ViolaBlock> VIOLA = registerPipeBlock("viola", "8",
            () -> new ViolaBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<ViolaExtensionBlock> VIOLA_EXTENSION = registerBlockWithoutItem("viola_extension",
            () -> new ViolaExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));
     */



    private static <T extends Block> RegistryObject<T> registerPipeBlock(String name, String octave, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerPipeBlockItem(name, toReturn, octave);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerPipeBlockItem(String name, RegistryObject<T> block, String octave) {
        AllItems.ITEMS.register(name, () -> new GenericPipeBlockItem(block.get(), new Item.Properties(), octave));
    }



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        AllItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
