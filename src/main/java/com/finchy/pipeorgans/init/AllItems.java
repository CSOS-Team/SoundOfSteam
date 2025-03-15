package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class AllItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, PipeOrgans.MOD_ID);

    public static final DeferredHolder<Item, Item> BRASS_BOOT = ITEMS.register("brass_boot",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> DARK_OAK_BOOT = ITEMS.register("dark_oak_boot",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> COPPER_BOOT = ITEMS.register("copper_boot",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
