package com.finchy.pipeorgans.data;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.init.AllBlocks;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.data.Iterate;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PipeModelGenerator implements DataProvider {

    private final PackOutput output;
    private final ExistingFileHelper helper;
    private static final String modid = PipeOrgans.MOD_ID;

    public PipeModelGenerator(PackOutput output, ExistingFileHelper helper) {
        this.output = output;
        this.helper = helper;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (BlockEntry<? extends GenericPipeBlock> pipeEntry : AllBlocks.PIPE_BLOCKS) {
            generateForPipe(pOutput, pipeEntry, futures, (pipeEntry.get().isHorizontal()));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    }

    @Override
    public String getName() {
        return "Pipe Model Generator";
    }

    private void generateForPipe(CachedOutput cache, BlockEntry<? extends GenericPipeBlock> blockEntry, List<CompletableFuture<?>> futures, boolean horizontal) {
        String pipeName = blockEntry.getId().getPath();
        String horizontalBasePrefix = horizontal ? "horizontal_" : "";
        try {
            for (PipeSize size : PipeSize.values()) {
                // load pipe json model
                JsonObject pipeJson = loadModel(pipeName+"/"+pipeName+"_"+size.getSerializedName()+".json"); // e.g. diapason/diapason_medium.json

                for (boolean wall : Iterate.trueAndFalse) {
                    JsonObject copiedPipeJson = pipeJson.deepCopy();
                    String wallString = wall ? "wall" : "floor";

                    String combinedName = pipeName+"/"+pipeName+"_"+size.getSerializedName()+"_"+wallString; // e.g. diapason/diapason_medium_wall
                    // load base json model
                    JsonObject baseJson = loadModel(horizontalBasePrefix + "base_" + size.getSerializedName() + "_" + wallString + ".json"); // e.g. base_medium_floor.json

                    // add base geometry to pipe
                    JsonArray pipeElements = copiedPipeJson.getAsJsonArray("elements");
                    JsonArray baseElements = baseJson.getAsJsonArray("elements");
                    if (pipeElements == null) {
                        pipeElements = new JsonArray();
                        copiedPipeJson.add("elements", pipeElements);
                    }
                    for (JsonElement element : baseElements) {
                        pipeElements.add(element);
                    }

                    // add base textures
                    JsonObject baseTextures = baseJson.getAsJsonObject("textures");
                    if (baseTextures != null) {
                        JsonObject pipeTextures = copiedPipeJson.getAsJsonObject("textures");
                        for (Map.Entry<String, JsonElement> entry : baseTextures.entrySet()) {
                            pipeTextures.add(entry.getKey(), entry.getValue());
                        }
                        copiedPipeJson.add("textures", pipeTextures);
                    }

                    // write combined json to generated/resources
                    writeModel(cache, copiedPipeJson,
                            modid + "/models/block/" + combinedName + ".json", futures
                    ); // e.g. assets/pipeorgans/models/block/diapason/diapason_medium_floor.json

                    // generate powered child model
                    JsonObject poweredJson = new JsonObject();
                    poweredJson.addProperty("parent", PipeOrgans.MOD_ID + ":block/" + combinedName);
                    JsonObject poweredTextures = new JsonObject();
                    poweredTextures.addProperty("1", "create:block/copper_redstone_plate_powered");
                    poweredJson.add("textures", poweredTextures);

                    // write powered json to generated/resources
                    writeModel(cache, poweredJson,
                            modid + "/models/block/" + combinedName + "_powered.json", futures
                    ); // e.g. assets/pipeorgans/models/block/diapason/diapason_medium_floor_powered.json
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonObject loadModel(String name) throws IOException {
        ResourceLocation rl = PipeOrgans.asResource("models/block/" + name);

        if (!helper.exists(rl, PackType.CLIENT_RESOURCES)) {
            throw new FileNotFoundException("Missing model: "+ rl);
        }

        try (InputStream is = helper.getResource(rl, PackType.CLIENT_RESOURCES).open()) {
            return JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
        }
    }

    private void writeModel(CachedOutput cache, JsonElement json, String path, List<CompletableFuture<?>> futures) throws IOException {
        Path outPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(path);
        Files.createDirectories(outPath.getParent());
        futures.add(DataProvider.saveStable(cache, json, outPath));
    }
}
