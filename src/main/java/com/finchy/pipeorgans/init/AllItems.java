package com.finchy.pipeorgans.init;

import com.simibubi.create.Create;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.Item;

import static com.finchy.pipeorgans.PipeOrgans.REGISTRATE;



public class AllItems {

    static {
        Create.REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS_BLOCKS);
    }

    public static final RegistryEntry<Item> WHOOPS = REGISTRATE.item("whoops", Item::new).register();




    public static void register() {} // load the class
}
