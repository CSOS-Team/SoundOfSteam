package com.finchy.pipeorgans.data.advancement;

import com.finchy.pipeorgans.advancement.PipeGogglesTrigger;
import com.finchy.pipeorgans.advancement.SteamBaseTrigger;
import com.finchy.pipeorgans.advancement.WaterPipeTrigger;
import com.finchy.pipeorgans.data.advancement.PipeOrgansAdvancement.Builder;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllItems;
import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;


public class AllAdvancements implements DataProvider {

    public static final List<PipeOrgansAdvancement> ENTRIES = new ArrayList<>();

    private final PackOutput output;

    public AllAdvancements(PackOutput output) {
        this.output = output;
    }

    // builder order is nominally:
    // icon, title, description, parent, trigger, tasktype

    public static final PipeOrgansAdvancement ROOT = create("root", b -> b.icon(AllBlocks.TROMPETTE)
            .title("Sound of Steam")
            .description("All things Pipe and Steamy")
            .awardedForFree()
            .silentTask()),

    PIPE_BASE = create("pipe_base", b -> b.icon(AllBlocks.BASE)
            .title("Crafting the Base-ics")
            .description("Craft a Pipe Base")
            .after(ROOT)
            .whenIconCollected()),



    // principal branch

    DIAPASON = create("diapason", b -> b.icon(AllBlocks.DIAPASON)
            .title("The Principal Voice")
            .description("Place a Diapason pipe")
            .after(PIPE_BASE)
            .whenBlockPlaced(AllBlocks.DIAPASON)),

    PRESTANT = create("prestant", b -> b.icon(AllBlocks.PRESTANT)
            .title("Pressed and Prestant")
            .description("Place a Prestant pipe")
            .after(DIAPASON)
            .whenBlockPlaced(AllBlocks.PRESTANT)),

    PICCOLO = create("piccolo", b -> b.icon(AllBlocks.PICCOLO)
            .title("Piercing the Sky")
            .description("Place a Piccolo pipe")
            .after(PRESTANT)
            .whenBlockPlaced(AllBlocks.PICCOLO)),

    OPEN_WOOD = create("open_wood", b -> b.icon(AllBlocks.OPEN_WOOD)
            .title("Big Boom")
            .description("Place an Open Wood pipe")
            .after(PICCOLO)
            .whenBlockPlaced(AllBlocks.OPEN_WOOD)),

    // flute branch

    GEDECKT = create("gedeckt", b -> b.icon(AllBlocks.GEDECKT)
            .title("Capped Harmony")
            .description("Place a Gedeckt pipe")
            .after(PIPE_BASE)
            .whenBlockPlaced(AllBlocks.GEDECKT)),

    ROHRFLOTE = create("rohrflote", b -> b.icon(AllBlocks.ROHRFLOTE)
            .title("The Pipe with a Hat")
            .description("Place a Rohrflote pipe")
            .after(GEDECKT)
            .whenBlockPlaced(AllBlocks.ROHRFLOTE)),

    HOHLFLUTE = create("hohlflute", b -> b.icon(AllBlocks.HOHLFLUTE)
            .title("Hohl in One")
            .description("Place a Hohlflute pipe")
            .after(ROHRFLOTE)
            .whenBlockPlaced(AllBlocks.HOHLFLUTE)),

    SUBBASS = create("subbass", b -> b.icon(AllBlocks.SUBBASS)
            .title("All About that (Sub)bass")
            .description("Place a Subbass pipe")
            .after(HOHLFLUTE)
            .whenBlockPlaced(AllBlocks.ROHRFLOTE)),

    // mutation branch

    NASARD = create("nasard", b -> b.icon(AllBlocks.NASARD)
            .title("The Notes between the Notes")
            .description("Place a Nasard pipe")
            .after(PIPE_BASE)
            .whenBlockPlaced(AllBlocks.NASARD)),

    TIERCE = create("tierce", b -> b.icon(AllBlocks.TIERCE)
            .title("Adds Flavour, Ruins Chords")
            .description("Place a Tierce pipe")
            .after(NASARD)
            .whenBlockPlaced(AllBlocks.TIERCE)),

    // strings branch

    VIOLA = create("viola", b -> b.icon(AllBlocks.VIOLA)
            .title("Bowless Beauty")
            .description("Place a Viola pipe")
            .after(PIPE_BASE)
            .whenBlockPlaced(AllBlocks.VIOLA)),

    VOX_CELESTE = create("vox_celeste", b -> b.icon(AllBlocks.VOX_CELESTE)
            .title("Pleasantly Out of Tune")
            .description("Place a Voix Celeste pipe")
            .after(VIOLA)
            .whenBlockPlaced(AllBlocks.VOX_CELESTE)),

    GAMBA = create("gamba", b -> b.icon(AllBlocks.GAMBA)
            .title("Shrill Strings")
            .description("Place a Gamba pipe")
            .after(VOX_CELESTE)
            .whenBlockPlaced(AllBlocks.GAMBA)),

    // reed branch

    BRASS_REED = create("brass_reed", b -> b.icon(AllItems.BRASS_REED)
            .title("Are you Reed-y?")
            .description("Assemble a Reed Pipe by deploying a Boot, Brass Reed and Tuning Wire onto a Pipe Base")
            .after(PIPE_BASE)
            .whenIconCollected()),

    TROMPETTE = create("trompette", b -> b.icon(AllBlocks.TROMPETTE)
            .title("HONK")
            .description("Place a Trompette pipe")
            .after(BRASS_REED)
            .whenBlockPlaced(AllBlocks.TROMPETTE)),

    CHAMADE = create("chamade", b -> b.icon(AllBlocks.CHAMADE)
            .title("Trompette Pipe, but it Sideways")
            .description("Place a Chamade pipe")
            .after(TROMPETTE)
            .whenBlockPlaced(AllBlocks.CHAMADE)),

    ENGLISH_HORN = create("english_horn", b -> b.icon(AllBlocks.ENGLISH_HORN)
            .title("Actually from Germany")
            .description("Place an English Horn pipe")
            .after(CHAMADE)
            .whenBlockPlaced(AllBlocks.ENGLISH_HORN)),

    VOX_HUMANA = create("vox_humana", b -> b.icon(AllBlocks.VOX_HUMANA)
            .title("A Choir of Goats")
            .description("Place a Vox Humana pipe")
            .after(ENGLISH_HORN)
            .whenBlockPlaced(AllBlocks.VOX_HUMANA)),

    BASSOON = create("bassoon", b -> b.icon(AllBlocks.BASSOON)
            .title("Firewood")
            .description("Place a Bassoon pipe")
            .after(VOX_HUMANA)
            .whenBlockPlaced(AllBlocks.BASSOON)),

    POSAUNE = create("posaune", b -> b.icon(AllBlocks.POSAUNE)
            .title("Rumbling Reeds")
            .description("Place a Posaune pipe")
            .after(BASSOON)
            .whenBlockPlaced(AllBlocks.POSAUNE)),

    //Hidden Advancements
    //Don't ruin the fun and peek










    PIPE_GOGGLES = create("pipe_goggles", b -> b.icon(com.simibubi.create.AllItems.GOGGLES)
            .title("Pipes for Nerds")
            .description("Put goggles on a pipe")
            .trigger(PipeGogglesTrigger.instance())
            .after(ROOT)
            .secretTask()
    ),
    WATER_PIPE = create("water_pipe", b -> b.icon(Items.WATER_BUCKET)
                    .title("The Sound of... Birds?")
                    .description("Waterlog a Piccolo pipe")
                    .trigger(WaterPipeTrigger.instance())
                    .after(PICCOLO)
                    .secretTask()
            ),
    STEAM_BASE = create("steam_base", b -> b.icon(AllBlocks.BASE)
            .title("Steam. Just Steam")
            .description("Let steam escape through a Pipe Base")
            .trigger(SteamBaseTrigger.instance())
            .after(ROOT)
            .secretTask()
            );




    //Why'd you peek :(

    //except you Finchy, you're allowed to peek <3

    private static PipeOrgansAdvancement create(String id, UnaryOperator<Builder> b) {
        return new PipeOrgansAdvancement(id, b);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            ResourceLocation id = advancement.getId();
            if (!set.add(id))
                throw new IllegalStateException("Duplicate advancement " + id);
            Path path = pathProvider.json(id);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
                    .serializeToJson(), path));
        };

        for (PipeOrgansAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Create: Sound of Steam's Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (PipeOrgansAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }

    public static void register() {
    }

}