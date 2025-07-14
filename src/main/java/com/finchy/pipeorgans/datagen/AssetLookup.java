package com.finchy.pipeorgans.datagen;

import com.finchy.pipeorgans.PipeOrgans;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Function;

public class AssetLookup {

    public static ModelFile partialStandardModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String... suffix) { // equivalent to create's partialBaseModel()
        String string = "/"+ctx.getName();
        for (String suf : suffix)
            if (!suf.isEmpty())
                string += "_" + suf;
        final String location = "block/"+ctx.getName()+string;
        return prov.models().getExistingFile(prov.modLoc(location));
    }

    public static ModelFile partialExtensionModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String... suffix) {
        String pipeName;
        if (ctx.getName().endsWith("_extension"))
            pipeName = ctx.getName().substring(0, ctx.getName().length() - "_extension".length());
        else {
            PipeOrgans.LOGGER.error("Tried to generate extension blockstates on a block that is not an extension or is incorrectly named: {}", ctx.getName());
            return null;
        }
        String string = "/extension/"+pipeName;
        for (String suf : suffix)
            if (!suf.isEmpty())
                string += "_" + suf;
        final String location = "block/"+pipeName+string;
        return prov.models().getExistingFile(prov.modLoc(location));
    }

    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> existingItemModel() {
        return (c, p) -> p.getExistingFile(p.modLoc("item/" + c.getName()));
    }

    public static Function<BlockState, ModelFile> forPowered(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov) {
        return state -> state.getValue(BlockStateProperties.POWERED) ?
                partialStandardModel(ctx, prov, "powered")
                : partialStandardModel(ctx, prov);
    }

}