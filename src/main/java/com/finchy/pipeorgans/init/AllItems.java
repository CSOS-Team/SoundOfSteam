package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PipeOrgans.MOD_ID);

    public static final RegistryObject<Item> BRASS_BOOT = ITEMS.register("brass_boot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DARK_OAK_BOOT = ITEMS.register("dark_oak_boot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_BOOT = ITEMS.register("copper_boot",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
