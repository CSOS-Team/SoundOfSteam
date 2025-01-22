package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.diapason.DiapasonBlock;
import com.finchy.pipeorgans.block.diapason.DiapasonExtensionBlock;
import com.finchy.pipeorgans.block.gamba.GambaBlock;
import com.finchy.pipeorgans.block.gamba.GambaExtensionBlock;
import com.finchy.pipeorgans.block.gedeckt.GedecktBlock;
import com.finchy.pipeorgans.block.gedeckt.GedecktExtensionBlock;
import com.finchy.pipeorgans.block.piccolo.PiccoloBlock;
import com.finchy.pipeorgans.block.piccolo.PiccoloExtensionBlock;
import com.finchy.pipeorgans.block.subbass.SubbassBlock;
import com.finchy.pipeorgans.block.subbass.SubbassExtensionBlock;
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

    public static final RegistryObject<GedecktBlock> GEDECKT = registerBlock("gedeckt",
            () -> new GedecktBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GedecktExtensionBlock> GEDECKT_EXTENSION = registerBlockWithoutItem("gedeckt_extension",
            () -> new GedecktExtensionBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<DiapasonBlock> DIAPASON = registerBlock("diapason",
            () -> new DiapasonBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<DiapasonExtensionBlock> DIAPASON_EXTENSION = registerBlockWithoutItem("diapason_extension",
            () -> new DiapasonExtensionBlock(BlockBehaviour.Properties.copy(com.simibubi.create.AllBlocks.ZINC_BLOCK.get())
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GambaBlock> GAMBA = registerBlock("gamba",
            () -> new GambaBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<GambaExtensionBlock> GAMBA_EXTENSION = registerBlockWithoutItem("gamba_extension",
            () -> new GambaExtensionBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PiccoloBlock> PICCOLO = registerBlock("piccolo",
            () -> new PiccoloBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<PiccoloExtensionBlock> PICCOLO_EXTENSION = registerBlockWithoutItem("piccolo_extension",
            () -> new PiccoloExtensionBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<SubbassBlock> SUBBASS = registerBlock("subbass",
            () -> new SubbassBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<SubbassExtensionBlock> SUBBASS_EXTENSION = registerBlockWithoutItem("subbass_extension",
            () -> new SubbassExtensionBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return AllItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
