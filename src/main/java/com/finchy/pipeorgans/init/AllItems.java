package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PipeOrgans.MOD_ID);


    public static final RegistryObject<Item> WHOOPS = ITEMS.register("whoops",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    //public static final RegistryEntry<Item> WHOOPS = REGISTRATE.item("whoops", Item::new).register();

}
