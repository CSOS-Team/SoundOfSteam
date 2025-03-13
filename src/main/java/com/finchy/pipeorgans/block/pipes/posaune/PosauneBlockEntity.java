package com.finchy.pipeorgans.block.pipes.posaune;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.PedalBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PosauneBlockEntity extends PedalBlockEntity {
    public PosauneBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, AllBlockEntities.POSAUNE_BLOCK_ENTITY.get());
    }

    @OnlyIn(Dist.CLIENT)
    protected PosauneSoundInstance soundInstance;

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void tickAudio(Generic.WhistleSize size, boolean powered) {
        if (!powered) {
            if (soundInstance != null) {
                soundInstance.fadeOut();
                soundInstance = null;
            }
            return;
        }

        float f = (float) Math.pow(2, -pitch / 12.0);
        boolean particle = level.getGameTime() % 8 == 0;
        Vec3 eyePosition = Minecraft.getInstance().cameraEntity.getEyePosition();
        float maxVolume = (float) Mth.clamp((64 - eyePosition.distanceTo(Vec3.atCenterOf(worldPosition))) / 64, 0, 1);

        if (soundInstance == null || soundInstance.isStopped() || soundInstance.getOctave() != size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new PosauneSoundInstance(size, worldPosition));

            AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .1f, f, false);

            particle = true;
        }

        soundInstance.keepAlive();
        soundInstance.setPitch(f);

        if (!particle)
            return;

        createSteamJet(size);
    }

    @Override
    public void createSteamJet(Generic.WhistleSize size) { // derived from ReedBlockEntity
        float yOffset = 0.25f;
        double yPos = (double) pitch + 1 + yOffset;
        Vec3 v = new Vec3(0, yPos, 0).add(Vec3.atBottomCenterOf(worldPosition));
        Vec3 m = new Vec3(0, 1, 0);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }
}
