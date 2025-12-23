package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllPipeOrgansPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(AllBlocks.TRACKER_BAR, "tracker_bar", TrackerBarPonder::musicRollPlayback);

        HELPER.forComponents(AllBlocks.WINDCHEST_MASTER)
                .addStoryBoard("windchest_controller", PipePlaybackPonder::windchestController)
                .addStoryBoard("boiler_pipe_playback", PipePlaybackPonder::boilerPipePlaying);

        HELPER.forComponents(AllBlocks.PIPE_BLOCKS)
                .addStoryBoard("pipe_adjusting", PipePlaybackPonder::pipeAdjusting);
    }
}
