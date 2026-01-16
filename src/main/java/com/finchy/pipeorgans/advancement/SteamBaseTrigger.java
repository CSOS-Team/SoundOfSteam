package com.finchy.pipeorgans.advancement;

import com.finchy.pipeorgans.PipeOrgans;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SteamBaseTrigger extends SimpleCriterionTrigger<SteamBaseTrigger.Instance> {

    public static final ResourceLocation ID =
            PipeOrgans.asResource("steam_base");

    @Override
    protected SteamBaseTrigger.Instance createInstance(JsonObject json,
                                                         ContextAwarePredicate player,
                                                         DeserializationContext context) {
        return new SteamBaseTrigger.Instance(player);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, SteamBaseTrigger.Instance::test);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(ID, player);
        }

        public boolean test() {
            return true;
        }
    }

    // Datagen access
    public static CriterionTriggerInstance instance() {
        return new SteamBaseTrigger.Instance(ContextAwarePredicate.ANY);
    }

}
