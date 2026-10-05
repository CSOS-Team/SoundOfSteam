package com.finchy.pipeorgans.advancement;

import com.finchy.pipeorgans.init.AllTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class WaterPipeTrigger extends SimpleCriterionTrigger<WaterPipeTrigger.Instance> {

    public void trigger(ServerPlayer player) {
        this.trigger(player, Instance::matches);
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {

        public static Criterion<Instance> instance(ContextAwarePredicate player) {
            return AllTriggers.WATER_PIPE_TRIGGER.get().createCriterion(new Instance(Optional.of(player)));
        }

        public static Criterion<Instance> instance() {
            return AllTriggers.WATER_PIPE_TRIGGER.get().createCriterion(new Instance(Optional.empty()));
        }

        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player)
                ).apply(instance, Instance::new)
        );

        public boolean matches() {
            return true;
        }
    }

}
