package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.WindchestBlock;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.animation.LerpedFloat;

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

import static com.finchy.pipeorgans.block.pipes.generic.GenericPipeBlock.FACING;
import static com.finchy.pipeorgans.block.pipes.generic.GenericPipeBlock.getAttachedDirection;
import static com.finchy.pipeorgans.init.AllSoundEvents.TROMPETTE_DEEP;

public class GenericPipeBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public WeakReference<FluidTankBlockEntity> source;
    public LerpedFloat animation;
    protected int pitch;

    public GenericPipeBlockEntity(BlockPos pos, BlockState blockState, RegistryObject<BlockEntityType> blockEntity) {
        super(blockEntity.get(), pos, blockState);
        source = new WeakReference<>(null);
        animation = LerpedFloat.angular();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("Pitch", pitch);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        pitch = tag.getInt("Pitch");
        super.read(tag, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        String[] pitches = CreateLang.translateDirect("generic.notes")
                .getString()
                .split(";");
        CreateLang.translate("generic.pitch", pitches[pitch % pitches.length]).forGoggles(tooltip);
        return true;
    }

    protected boolean isPowered() {
        return getBlockState().getOptionalValue(GenericPipeBlock.POWERED)
                .orElse(false);
    }

    protected Generic.WhistleSize getOctave() {
        return getBlockState().getOptionalValue(GenericPipeBlock.SIZE)
                .orElse(Generic.WhistleSize.MEDIUM);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            if (isPowered())
                award(AllAdvancements.STEAM_WHISTLE);
            return;
        }

        FluidTankBlockEntity tank = getTank();

        BlockState state = getBlockState();
        BlockPos attachedPos = getBlockPos().relative(getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);
        boolean isActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            isActive = windchest.isMasterActive(level, attachedState.getValue(FACING), attachedPos);
        }

        boolean powered;
        if (isPowered()) {
            powered = ((tank != null && tank.boiler.isActive() && (tank.boiler.passiveHeat || tank.boiler.activeHeat > 0)
                    || isVirtual()) || isActive );
        } else {
            powered = false;
        }

        animation.chase(powered ? 1 : 0, powered ? .5f : .4f, powered ? LerpedFloat.Chaser.EXP : LerpedFloat.Chaser.LINEAR);
        animation.tickChaser();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.tickAudio(getOctave(), powered));
    }

    @OnlyIn(Dist.CLIENT)
    protected GenericSoundInstance soundInstance;

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
                    .play(soundInstance = new GenericSoundInstance(size, worldPosition, TROMPETTE_DEEP.get()));

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

    public void createSteamJet(Generic.WhistleSize size) {
        Direction facing = getBlockState().getOptionalValue(GenericPipeBlock.FACING)
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

    public void updatePitch() {
        BlockPos currentPos = worldPosition.above();
        int newPitch;
        for (newPitch = 0; newPitch <= 12; newPitch += 2) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!(blockState.getBlock() instanceof GenericExtensionBlock))
                break;
            if (blockState.getValue(GenericExtensionBlock.SHAPE) == Generic.QuadrupleExtensionShape.DOUBLE) {
                newPitch++;
                break;
            }
            currentPos = currentPos.above();
        }
        if (pitch == newPitch)
            return;
        pitch = newPitch;

        notifyUpdate();

        FluidTankBlockEntity tank = getTank();
        if (tank != null && tank.boiler != null)
            tank.boiler.checkPipeOrganAdvancement(tank);
    }

    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = getAttachedDirection(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }
}
