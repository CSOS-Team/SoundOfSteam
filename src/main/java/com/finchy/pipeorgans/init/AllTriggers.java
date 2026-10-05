package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.advancement.PipeGogglesTrigger;
import com.finchy.pipeorgans.advancement.SteamBaseTrigger;
import com.finchy.pipeorgans.advancement.WaterPipeTrigger;
import com.simibubi.create.foundation.advancement.CriterionTriggerBase;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Define all the custom advancement triggers here
 */
public class AllTriggers {
    
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, PipeOrgans.MOD_ID);
    
    public static final Supplier<PipeGogglesTrigger> PIPE_GOGGLES_TRIGGER =
            TRIGGER_TYPES.register("pipe_goggles", PipeGogglesTrigger::new);

    public static final Supplier<SteamBaseTrigger> STEAM_BASE_TRIGGER =
            TRIGGER_TYPES.register("steam_base", SteamBaseTrigger::new);

    public static final Supplier<WaterPipeTrigger> WATER_PIPE_TRIGGER =
            TRIGGER_TYPES.register("water_pipe", WaterPipeTrigger::new);
    
    public static void register(IEventBus bus) {
        TRIGGER_TYPES.register(bus);
    }

}

