package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import net.createmod.ponder.api.registration.IndexExclusionHelper;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class POPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return PipeOrgans.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        AllPipeOrgansPonderScenes.register(helper);
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {
        helper.excludeBlockVariants(GenericPipeBlock.class, AllBlocks.DIAPASON.get());
    }
}
