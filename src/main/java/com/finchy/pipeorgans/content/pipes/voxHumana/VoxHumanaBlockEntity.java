package com.finchy.pipeorgans.content.pipes.voxHumana;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlockEntity;
import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VoxHumanaBlockEntity extends QuadruplePipeBlockEntity {
    public VoxHumanaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        pipeBlock = AllBlocks.VOX_HUMANA;
    }

    @OnlyIn(Dist.CLIENT)
    protected VoxHumanaSoundInstance soundInstance;

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void tickAudio(PipeSize size, boolean powered) {
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
                    .play(soundInstance = new VoxHumanaSoundInstance(size, worldPosition));

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
    public void createSteamJet(PipeSize size) {
        Direction facing = getBlockState().getOptionalValue(GenericPipeBlock.FACING)
                .orElse(Direction.SOUTH);
        float angle = 180+ AngleHelper.horizontalAngle(facing);

        float yOffset = pitch==0?0.125f:0;
        double yPos = ((double) pitch/4)+1 + yOffset;

        if (size == PipeSize.TINY) { size = PipeSize.SMALL; }
        double zOffset = (2 / 16f*size.ordinal()) + (pitch==0?0:0.0625);

        Vec3 v = VecHelper.rotate(
                new Vec3(0, yPos, zOffset), angle, Direction.Axis.Y).add(Vec3.atBottomCenterOf(worldPosition));

        Vec3 m = VecHelper.rotate(new Vec3(0, 1, 1), angle, Direction.Axis.Y);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }
}
