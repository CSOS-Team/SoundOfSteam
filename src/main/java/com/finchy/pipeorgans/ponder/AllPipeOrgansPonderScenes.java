package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.ponder.scenes.NoteLinkScenes;
import com.finchy.pipeorgans.ponder.scenes.PipeScenes;
import com.finchy.pipeorgans.ponder.scenes.TrackerBarScenes;
import com.finchy.pipeorgans.ponder.scenes.WindchestScenes;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllPipeOrgansPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(AllBlocks.TRACKER_BAR, "tracker_bar", TrackerBarScenes::musicRollPlayback);

        HELPER.forComponents(AllBlocks.WINDCHEST_MASTER)
                .addStoryBoard("windchests", WindchestScenes::windchests);

        HELPER.forComponents(AllBlocks.WINDCHEST)
                .addStoryBoard("windchests", WindchestScenes::windchests);

        HELPER.forComponents(AllBlocks.PIPE_BLOCKS)
                .addStoryBoard("pipe_adjusting", PipeScenes::pipeAdjusting)
                .addStoryBoard("pipe_swapping", PipeScenes::pipeSwapping);

        HELPER.forComponents(AllBlocks.NOTE_LINK)
                .addStoryBoard("note_link_basics", NoteLinkScenes::noteLinkBasics)
                .addStoryBoard("note_link_cap", NoteLinkScenes::noteLinkCAP);
    }
}
