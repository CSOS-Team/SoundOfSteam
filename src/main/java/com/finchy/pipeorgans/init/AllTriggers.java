package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.advancement.PipeGogglesTrigger;
import com.finchy.pipeorgans.advancement.SteamBaseTrigger;
import com.finchy.pipeorgans.advancement.WaterPipeTrigger;
import com.finchy.pipeorgans.advancement.HautboisFlowerTrigger;
import net.minecraft.advancements.CriteriaTriggers;

/**
 * Define all the custom advancement triggers here
 */
public class AllTriggers {

    public static final PipeGogglesTrigger PIPE_GOGGLES =
            new PipeGogglesTrigger();
    public static final SteamBaseTrigger STEAM_BASE =
            new SteamBaseTrigger();
    public static final WaterPipeTrigger WATER_PIPE =
            new WaterPipeTrigger();
    public static final HautboisFlowerTrigger HAUTBOIS_FLOWER =
            new HautboisFlowerTrigger();

    public static void register() {
        CriteriaTriggers.register(PIPE_GOGGLES);
        CriteriaTriggers.register(STEAM_BASE);
        CriteriaTriggers.register(WATER_PIPE);
        CriteriaTriggers.register(HAUTBOIS_FLOWER);
    }

}
