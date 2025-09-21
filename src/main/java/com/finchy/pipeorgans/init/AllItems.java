package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MusicRollItem;
import com.finchy.pipeorgans.data.AssetLookup;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AllItems {

    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS);
    }

    public static final ItemEntry<Item>
            BRASS_BOOT = REGISTRATE.item("brass_boot", Item::new).register(),
            DARK_OAK_BOOT = REGISTRATE.item("dark_oak_boot", Item::new).register(),
            COPPER_BOOT = REGISTRATE.item("copper_boot", Item::new).register(),
            BRASS_REED = REGISTRATE.item("brass_reed", Item::new).register(),
            TUNING_WIRE = REGISTRATE.item("tuning_wire", Item::new).register();

    public static final ItemEntry<MusicRollItem> MUSIC_ROLL = REGISTRATE.item("music_roll", MusicRollItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<SequencedAssemblyItem>
            INCOMPLETE_TROMPETTE = sequencedPipeIngredient("incomplete_trompette"),
            INCOMPLETE_VOX_HUMANA = sequencedPipeIngredient("incomplete_vox_humana"),
            INCOMPLETE_POSAUNE = sequencedPipeIngredient("incomplete_posaune");

    private static ItemEntry<SequencedAssemblyItem> sequencedPipeIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .model(AssetLookup.existingItemModel())
                .register();
    }

    public static void register() {

    }
}
