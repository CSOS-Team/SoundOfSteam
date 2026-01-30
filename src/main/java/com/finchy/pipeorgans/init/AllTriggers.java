package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.advancement.PipeGogglesTrigger;
import com.finchy.pipeorgans.advancement.SteamBaseTrigger;
import com.finchy.pipeorgans.advancement.WaterPipeTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class AllTriggers {

    public static final PipeGogglesTrigger PIPE_GOGGLES =
            new PipeGogglesTrigger();
    public static final SteamBaseTrigger STEAM_BASE =
            new SteamBaseTrigger();
    public static final WaterPipeTrigger WATER_PIPE =
            new WaterPipeTrigger();

    public static void register() {
        PipeOrgans.LOGGER.info("Registering PipeGogglesTrigger");
        CriteriaTriggers.register(PIPE_GOGGLES);
        CriteriaTriggers.register(STEAM_BASE);
        CriteriaTriggers.register(WATER_PIPE);
    }

}

