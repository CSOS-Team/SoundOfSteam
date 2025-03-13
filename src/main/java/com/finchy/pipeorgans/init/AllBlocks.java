package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.WindchestBlock;
import com.finchy.pipeorgans.block.WindchestMasterBlock;
import com.finchy.pipeorgans.block.base.BaseBlock;
import com.finchy.pipeorgans.block.pipes.diapason.DiapasonBlock;
import com.finchy.pipeorgans.block.pipes.diapason.DiapasonExtensionBlock;
import com.finchy.pipeorgans.block.pipes.gamba.GambaBlock;
import com.finchy.pipeorgans.block.pipes.gamba.GambaExtensionBlock;
import com.finchy.pipeorgans.block.pipes.gedeckt.GedecktBlock;
import com.finchy.pipeorgans.block.pipes.gedeckt.GedecktExtensionBlock;
import com.finchy.pipeorgans.block.pipes.nasard.NasardBlock;
import com.finchy.pipeorgans.block.pipes.nasard.NasardExtensionBlock;
import com.finchy.pipeorgans.block.pipes.piccolo.PiccoloBlock;
import com.finchy.pipeorgans.block.pipes.piccolo.PiccoloExtensionBlock;
import com.finchy.pipeorgans.block.pipes.posaune.PosauneBlock;
import com.finchy.pipeorgans.block.pipes.posaune.PosauneExtensionBlock;
import com.finchy.pipeorgans.block.pipes.subbass.SubbassBlock;
import com.finchy.pipeorgans.block.pipes.subbass.SubbassExtensionBlock;
import com.finchy.pipeorgans.block.pipes.trompette.TrompetteBlock;
import com.finchy.pipeorgans.block.pipes.trompette.TrompetteExtensionBlock;
import com.finchy.pipeorgans.block.pipes.vox_humana.VoxHumanaBlock;
import com.finchy.pipeorgans.block.pipes.vox_humana.VoxHumanaExtensionBlock;
import com.finchy.pipeorgans.item.GenericPipeBlockItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.function.Supplier;

public class AllBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, PipeOrgans.MOD_ID);

    // declare blocks here

    public static final DeferredHolder<Block, BaseBlock> BASE = registerBlock("base",
            () -> new BaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, WindchestBlock> WINDCHEST = registerBlock("windchest",
            () -> new WindchestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredHolder<Block, WindchestMasterBlock> WINDCHEST_MASTER = registerBlock("windchest_master",
            () -> new WindchestMasterBlock(BlockBehaviour.Properties.ofFullCopy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops().noOcclusion()));



    public static final DeferredHolder<Block, GedecktBlock> GEDECKT = registerPipeBlock("gedeckt", "8",
            () -> new GedecktBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, GedecktExtensionBlock> GEDECKT_EXTENSION = registerBlockWithoutItem("gedeckt_extension",
            () -> new GedecktExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, DiapasonBlock> DIAPASON = registerPipeBlock("diapason", "8",
            () -> new DiapasonBlock(BlockBehaviour.Properties.ofFullCopy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, DiapasonExtensionBlock> DIAPASON_EXTENSION = registerBlockWithoutItem("diapason_extension",
            () -> new DiapasonExtensionBlock(BlockBehaviour.Properties.ofFullCopy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, GambaBlock> GAMBA = registerPipeBlock("gamba", "4",
            () -> new GambaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, GambaExtensionBlock> GAMBA_EXTENSION = registerBlockWithoutItem("gamba_extension",
            () -> new GambaExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, PiccoloBlock> PICCOLO = registerPipeBlock("piccolo", "2",
            () -> new PiccoloBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, PiccoloExtensionBlock> PICCOLO_EXTENSION = registerBlockWithoutItem("piccolo_extension",
            () -> new PiccoloExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, SubbassBlock> SUBBASS = registerPipeBlock("subbass",  "16",
            () -> new SubbassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, SubbassExtensionBlock> SUBBASS_EXTENSION = registerBlockWithoutItem("subbass_extension",
            () -> new SubbassExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, TrompetteBlock> TROMPETTE = registerPipeBlock("trompette", "8",
            () -> new TrompetteBlock(BlockBehaviour.Properties.ofFullCopy(com.simibubi.create.AllBlocks.BRASS_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, TrompetteExtensionBlock> TROMPETTE_EXTENSION = registerBlockWithoutItem("trompette_extension",
            () -> new TrompetteExtensionBlock(BlockBehaviour.Properties.ofFullCopy(com.simibubi.create.AllBlocks.BRASS_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, NasardBlock> NASARD = registerPipeBlock("nasard", "223",
            () -> new NasardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, NasardExtensionBlock> NASARD_EXTENSION = registerBlockWithoutItem("nasard_extension",
            () -> new NasardExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, PosauneBlock> POSAUNE = registerPipeBlock("posaune", "32",
            () -> new PosauneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, PosauneExtensionBlock> POSAUNE_EXTENSION = registerBlockWithoutItem("posaune_extension",
            () -> new PosauneExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, VoxHumanaBlock> VOX_HUMANA = registerPipeBlock("vox_humana", "8",
            () -> new VoxHumanaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, VoxHumanaExtensionBlock> VOX_HUMANA_EXTENSION = registerBlockWithoutItem("vox_humana_extension",
            () -> new VoxHumanaExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));



    private static <T extends Block> DeferredHolder<Block, T> registerPipeBlock(String name, String octave, Supplier<T> block) {
        DeferredHolder<Block,T> toReturn = BLOCKS.register(name, block);
        registerPipeBlockItem(name, toReturn, octave);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlockWithoutItem(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }

    private static <T extends Block> void registerPipeBlockItem(String name, DeferredHolder<Block, T> block, String octave) {
        AllItems.ITEMS.register(name, () -> new GenericPipeBlockItem(block.get(), new Item.Properties(), octave));
    }



    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Item, BlockItem> registerBlockItem(String name, Supplier<T> block) {
        return AllItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
