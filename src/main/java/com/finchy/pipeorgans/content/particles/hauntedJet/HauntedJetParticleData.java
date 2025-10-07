package com.finchy.pipeorgans.content.particles.hauntedJet;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.finchy.pipeorgans.init.AllParticleTypes;
import com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticle;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HauntedJetParticleData implements ParticleOptions, ICustomParticleDataWithSprite<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> {

    public static final Codec<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> CODEC = RecordCodecBuilder.create(i -> i
            .group(Codec.FLOAT.fieldOf("speed")
                    .forGetter(p -> p.speed))
            .apply(i, com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData::new));

    public static final ParticleOptions.Deserializer<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {
                public com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData fromCommand(ParticleType<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> particleTypeIn,
                                                                                                         StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float speed = reader.readFloat();
                    return new com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData(speed);
                }

                public com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData fromNetwork(ParticleType<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> particleTypeIn,
                                                                                                         FriendlyByteBuf buffer) {
                    return new com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData(buffer.readFloat());
                }
            };

    float speed;

    public HauntedJetParticleData(float speed) {
        this.speed = speed;
    }

    public HauntedJetParticleData() {
        this(0);
    }

    @Override
    public ParticleType<?> getType() {
        return AllParticleTypes.HAUNTED_JET.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(speed);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %f", AllParticleTypes.HAUNTED_JET.parameter(), speed);
    }

    @Override
    public Deserializer<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> getCodec(ParticleType<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> type) {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public SpriteParticleRegistration<com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData> getMetaFactory() {
        return HauntedJetParticle.Factory::new;
    }

}
