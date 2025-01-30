package com.finchy.pipeorgans.block.trompette;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.generic.ReedBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.RegistryObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class TrompetteBlockEntity extends ReedBlockEntity {

    @OnlyIn(Dist.CLIENT)
    protected TrompetteSoundInstance soundInstance;

    public TrompetteBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, AllBlockEntities.TROMPETTE_BLOCK_ENTITY);
    }

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
                    .play(soundInstance = new TrompetteSoundInstance(size, worldPosition));

            AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .1f,
                    size == Generic.WhistleSize.SMALL ? f + .75f : f, false);

            particle = true;
        }

        soundInstance.keepAlive();
        soundInstance.setPitch(f);

        if (!particle)
            return;

        createSteamJet(size);
    }
}
