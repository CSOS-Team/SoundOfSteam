package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class GenericPipeBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public WeakReference<FluidTankBlockEntity> source;
    public LerpedFloat animation;
    public int pitch;

    protected float steamJetOffset;

    protected BlockEntry<? extends GenericPipeBlock> baseBlock;

    public GenericPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        source = new WeakReference<>(null);
        animation = LerpedFloat.angular();
        steamJetOffset = 0.125f;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

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
        int stopSize = Integer.parseInt(((GenericPipeBlockItem) getBlockState().getBlock().asItem()).stopSize);
        double octave = 5-getOctave().ordinal() + (pitch<=6?1:0) - (Math.log(stopSize/8)/Math.log(2));
        CreateLang.translate("generic.pitch", pitches[pitch % pitches.length]).add(Component.literal(String.valueOf(octave)))
                .forGoggles(tooltip);
        return true;
    }

    protected boolean isPowered() {
        return getBlockState().getOptionalValue(GenericPipeBlock.POWERED)
                .orElse(false);
    }

    protected EPipeSizes.PipeSize getOctave() {
        return getBlockState().getOptionalValue(GenericPipeBlock.SIZE)
                .orElse(EPipeSizes.PipeSize.MEDIUM);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) return;
        FluidTankBlockEntity tank = getTank();

        BlockState state = getBlockState();
        BlockPos attachedPos = getBlockPos().relative(GenericPipeBlock.getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);
        boolean isActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            isActive = windchest.isMasterActive(level, attachedState.getValue(GenericPipeBlock.FACING), attachedPos);
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
    protected abstract void tickAudio(EPipeSizes.PipeSize size, boolean powered);

    public void createSteamJet(EPipeSizes.PipeSize size) {
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

    public void createHauntedJet(EPipeSizes.PipeSize size) {
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
        level.addParticle(new HauntedJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }

    public void createReedSteamJet() {
        double yPos = ((double) pitch/ baseBlock.get().EPB) +1 + steamJetOffset;
        Vec3 v = new Vec3(0, yPos, 0).add(Vec3.atBottomCenterOf(worldPosition));
        Vec3 m = new Vec3(0, 1, 0);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }

    public abstract void updatePitch();

    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = GenericPipeBlock.getAttachedDirection(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }

}
