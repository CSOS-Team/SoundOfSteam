package com.finchy.pipeorgans.block.gedeckt;

import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlock;
import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlockEntity;
import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleSoundInstance;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GedecktBlockEntity extends GenericWhistleBlockEntity {

    @Override
    public void setWhistleProperties() {
        this.baseBlock = AllBlocks.GEDECKT;
        this.extensionBlock = AllBlocks.GEDECKT_EXTENSION;
        this.blockEntity = AllBlockEntities.GEDECKT_BLOCK_ENTITY;
    }

    public GedecktBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @OnlyIn(Dist.CLIENT)
    protected GedecktSoundInstance soundInstance;

    @OnlyIn(Dist.CLIENT)
    protected void tickAudio(GenericWhistleBlock.WhistleSize size, boolean powered) {
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
                    .play(soundInstance = new GedecktSoundInstance(size, worldPosition));
            /*
            AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .175f,
                    size == GedecktBlock.WhistleSize.SMALL ? f + .75f : f, false);
             */
            particle = true;
        }

        soundInstance.keepAlive();
        soundInstance.setPitch(f);

        if (!particle)
            return;

        Direction facing = getBlockState().getOptionalValue(WhistleBlock.FACING)
                .orElse(Direction.SOUTH);
        float angle = 180 + AngleHelper.horizontalAngle(facing);
        Vec3 sizeOffset = VecHelper.rotate(new Vec3(0, -0.4f, 1 / 16f * size.ordinal()), angle, Direction.Axis.Y);
        Vec3 offset = VecHelper.rotate(new Vec3(0, 1, 0.75f), angle, Direction.Axis.Y);
        Vec3 v = offset.scale(.45f)
                .add(sizeOffset)
                .add(Vec3.atCenterOf(worldPosition));
        Vec3 m = offset.subtract(Vec3.atLowerCornerOf(facing.getNormal())
                .scale(.75f));
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }
}
