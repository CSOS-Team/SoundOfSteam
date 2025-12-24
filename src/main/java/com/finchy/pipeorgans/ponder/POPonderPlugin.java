package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.PipeOrgans;
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
}
