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
import com.finchy.pipeorgans.block.pipes.subbass.SubbassBlock;
import com.finchy.pipeorgans.block.pipes.subbass.SubbassExtensionBlock;
import com.finchy.pipeorgans.block.pipes.trompette.TrompetteBlock;
import com.finchy.pipeorgans.block.pipes.trompette.TrompetteExtensionBlock;
import com.finchy.pipeorgans.item.GenericPipeBlockItem;
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

public class AllBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PipeOrgans.MOD_ID);

    // declare blocks here

    public static final RegistryObject<BaseBlock> BASE = registerBlock("base",
            () -> new BaseBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<WindchestBlock> WINDCHEST = registerBlock("windchest",
            () -> new WindchestBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<WindchestMasterBlock> WINDCHEST_MASTER = registerBlock("windchest_master",
            () -> new WindchestMasterBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));



    public static final RegistryObject<GedecktBlock> GEDECKT = registerPipeBlock("gedeckt", "8",
            () -> new GedecktBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GedecktExtensionBlock> GEDECKT_EXTENSION = registerBlockWithoutItem("gedeckt_extension",
            () -> new GedecktExtensionBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<DiapasonBlock> DIAPASON = registerPipeBlock("diapason", "8",
            () -> new DiapasonBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<DiapasonExtensionBlock> DIAPASON_EXTENSION = registerBlockWithoutItem("diapason_extension",
            () -> new DiapasonExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GambaBlock> GAMBA = registerPipeBlock("gamba", "4",
            () -> new GambaBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GambaExtensionBlock> GAMBA_EXTENSION = registerBlockWithoutItem("gamba_extension",
            () -> new GambaExtensionBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PiccoloBlock> PICCOLO = registerPipeBlock("piccolo", "2",
            () -> new PiccoloBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PiccoloExtensionBlock> PICCOLO_EXTENSION = registerBlockWithoutItem("piccolo_extension",
            () -> new PiccoloExtensionBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<SubbassBlock> SUBBASS = registerPipeBlock("subbass",  "16",
            () -> new SubbassBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<SubbassExtensionBlock> SUBBASS_EXTENSION = registerBlockWithoutItem("subbass_extension",
            () -> new SubbassExtensionBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<TrompetteBlock> TROMPETTE = registerPipeBlock("trompette", "8",
            () -> new TrompetteBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.BRASS_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<TrompetteExtensionBlock> TROMPETTE_EXTENSION = registerBlockWithoutItem("trompette_extension",
            () -> new TrompetteExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.BRASS_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<NasardBlock> NASARD = registerPipeBlock("nasard", "223",
            () -> new NasardBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<NasardExtensionBlock> NASARD_EXTENSION = registerBlockWithoutItem("nasard_extension",
            () -> new NasardExtensionBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()));



    private static <T extends Block> RegistryObject<T> registerPipeBlock(String name, String octave, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerPipeBlockItem(name, toReturn, octave);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }

    private static <T extends Block> void registerPipeBlockItem(String name, RegistryObject<T> block, String octave) {
        AllItems.ITEMS.register(name, () -> new GenericPipeBlockItem(block.get(), new Item.Properties(), octave));
    }



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return AllItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
