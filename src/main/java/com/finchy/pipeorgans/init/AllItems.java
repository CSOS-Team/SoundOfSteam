package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MusicRollItem;
import com.finchy.pipeorgans.data.AssetLookup;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class AllItems {

    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.PIPE_ORGANS);
    }

    public static final ItemEntry<Item>
            BRASS_BOOT = REGISTRATE
            .item("brass_boot", Item::new)
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.brass_boot"))
            .register(),
            DARK_OAK_BOOT = REGISTRATE
                    .item("dark_oak_boot", Item::new)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.dark_oak_boot"))
                    .register(),
            COPPER_BOOT = REGISTRATE
                    .item("copper_boot", Item::new)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.copper_boot"))
                    .register(),
            REINFORCED_COPPER_BOOT = REGISTRATE
                    .item("reinforced_copper_boot", Item::new)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.reinforced_copper_boot"))
                    .register(),
            BRASSBOUND_BOOT = REGISTRATE
                    .item("brassbound_boot", Item::new)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.brassbound_boot"))
                    .register(),
            IRON_BOOT = REGISTRATE
                    .item("iron_boot", Item::new)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.iron_boot"))
                    .register(),
            GILDED_COPPER_BOOT = REGISTRATE
                    .item("gilded_copper_boot", Item::new)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.pipeorgans.gilded_copper_boot"))
                    .register(),
            BRASS_REED = REGISTRATE
                    .item("brass_reed", Item::new)
                    .register();

    public static final ItemEntry<Item>
            TUNING_WIRE = REGISTRATE.item("tuning_wire", Item::new)
            .model((ctx, prov) -> prov.generated(ctx::getEntry)
                    .override()
                    .predicate(PipeOrgans.asResource("variant"), 1)
                    .model(prov.getExistingFile(
                            prov.modLoc("item/worm")
                    ))
                    .end()
            )
            .register();
            //Trust me... that was essential ^

    public static final ItemEntry<MusicRollItem> MUSIC_ROLL = REGISTRATE.item("music_roll", MusicRollItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    //Items for reed pipes part way through sequenced assembly
    public static final ItemEntry<SequencedAssemblyItem>
            INCOMPLETE_TROMPETTE = sequencedPipeIngredient("incomplete_trompette"),
            INCOMPLETE_VOX_HUMANA = sequencedPipeIngredient("incomplete_vox_humana"),
            INCOMPLETE_POSAUNE = sequencedPipeIngredient("incomplete_posaune"),
            INCOMPLETE_ENGLISH_HORN = sequencedPipeIngredient("incomplete_english_horn"),
            INCOMPLETE_KRUMMHORN = sequencedPipeIngredient("incomplete_krummhorn"),
            INCOMPLETE_BASSOON = sequencedPipeIngredient("incomplete_bassoon"),
            INCOMPLETE_HAUTBOIS = sequencedPipeIngredient("incomplete_hautbois");

    private static ItemEntry<SequencedAssemblyItem> sequencedPipeIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .model(AssetLookup.existingItemModel())
                .register();
    }
//Anonym3000 does(n̶'̶t̶) like my comments
    public static void register() {

    }
}
