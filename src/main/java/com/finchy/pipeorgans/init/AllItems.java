package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AllItems {

    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS);
    }

    public static final ItemEntry<Item> BRASS_BOOT = REGISTRATE.item("brass_boot", Item::new).register();
    public static final ItemEntry<Item> DARK_OAK_BOOT = REGISTRATE.item("dark_oak_boot", Item::new).register();
    public static final ItemEntry<Item> COPPER_BOOT = REGISTRATE.item("copper_boot", Item::new).register();
    public static final ItemEntry<Item> BRASS_REED = REGISTRATE.item("brass_reed", Item::new).register();
    public static final ItemEntry<Item> TUNING_WIRE = REGISTRATE.item("tuning_wire", Item::new).register();

    public static final ItemEntry<Item> INCOMPLETE_TROMPETTE = REGISTRATE.item("incomplete_trompette", Item::new)
            .properties(p -> p.stacksTo(1)).register();
    public static final ItemEntry<Item> INCOMPLETE_VOX_HUMANA = REGISTRATE.item("incomplete_vox_humana", Item::new)
            .properties(p -> p.stacksTo(1)).register();
    public static final ItemEntry<Item> INCOMPLETE_POSAUNE = REGISTRATE.item("incomplete_posaune", Item::new)
            .properties(p -> p.stacksTo(1)).register();

    public static void register() {

    }
}
