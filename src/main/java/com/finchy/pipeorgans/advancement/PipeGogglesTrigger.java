package com.finchy.pipeorgans.advancement;

import com.finchy.pipeorgans.PipeOrgans;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class PipeGogglesTrigger extends SimpleCriterionTrigger<PipeGogglesTrigger.Instance> {

    @Override
    public Codec<PipeGogglesTrigger.Instance> codec() {
        return TameAnimalTrigger.TriggerInstance.CODEC;
    }
    
    public void trigger(ServerPlayer player) {
        this.trigger(player, Instance::test);
    }

    public static final ResourceLocation ID =
            PipeOrgans.asResource("pipe_goggles");

    @Override
    protected Instance createInstance(JsonObject json,
                                      ContextAwarePredicate player,
                                      DeserializationContext context) {
        return new Instance(player);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }\

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public Instance(ContextAwarePredicate player) {
            super(ID, player);
        }

        public boolean test() {
            return true;
        }
    }

    // Datagen access
    public static CriterionTriggerInstance instance() {
        return new Instance(ContextAwarePredicate.ANY);
    }

}
