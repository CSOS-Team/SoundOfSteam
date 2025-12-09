package com.finchy.pipeorgans.content.particles.hauntedJet;

import com.finchy.pipeorgans.init.AllParticleTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class HauntedJetParticleData implements ParticleOptions, ICustomParticleDataWithSprite<HauntedJetParticleData> {

    public static final MapCodec<HauntedJetParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.FLOAT.fieldOf("speed")
                    .forGetter(p -> p.speed))
            .apply(i, HauntedJetParticleData::new));

    public static final StreamCodec<ByteBuf, HauntedJetParticleData> STREAM_CODEC = ByteBufCodecs.FLOAT.map(
            HauntedJetParticleData::new, p -> p.speed
    );

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
    public MapCodec<HauntedJetParticleData> getCodec(ParticleType<HauntedJetParticleData> type) {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public SpriteParticleRegistration<HauntedJetParticleData> getMetaFactory() {
        return HauntedJetParticle.Factory::new;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, HauntedJetParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }
}
