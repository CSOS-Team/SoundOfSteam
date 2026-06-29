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

public class HautboisFlowerTrigger extends SimpleCriterionTrigger<HautboisFlowerTrigger.Instance> {

    public static final ResourceLocation ID = PipeOrgans.asResource("hautbois_flower");

    @Override
    protected HautboisFlowerTrigger.Instance createInstance(JsonObject json,
                                                           ContextAwarePredicate player,
                                                           DeserializationContext context) {
        return new HautboisFlowerTrigger.Instance(player);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, HautboisFlowerTrigger.Instance::test);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(ID, player);
        }

        public boolean test() {
            return true;
        }
    }

    public static CriterionTriggerInstance instance() {
        return new HautboisFlowerTrigger.Instance(ContextAwarePredicate.ANY);
    }
}
