package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.ClientConfig;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.Mth;


import java.lang.ref.WeakReference;
import java.util.List;

public abstract class GenericPipeBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public WeakReference<FluidTankBlockEntity> source;
    public LerpedFloat animation;
    public int pitch;
    protected boolean goggles;

    public boolean hasGoggles() {
        return goggles;
    }

    public void setGoggles(boolean goggles) {
        this.goggles = goggles;
    }

    protected static final float STEAM_JET_OFFSET = 0.125f;

    protected final BlockEntry<? extends GenericPipeBlock> pipeBlock; // MUST register block entities after blocks
    protected final BlockEntry<? extends GenericExtensionBlock<?>> extensionBlock;

    public GenericPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                  BlockEntry<? extends GenericPipeBlock> pipeBlock,
                                  BlockEntry<? extends GenericExtensionBlock<?>> extensionBlock) {
        super(type, pos, state);
        source = new WeakReference<>(null);
        animation = LerpedFloat.angular();
        this.pipeBlock = pipeBlock;
        this.extensionBlock = extensionBlock;
        goggles = false;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("Pitch", pitch);
        tag.putBoolean("Goggles", goggles);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        pitch = tag.getInt("Pitch");

        //The Funny Goggles
        boolean hadGoggles = goggles;
        goggles = tag.getBoolean("Goggles");

        if (!clientPacket)
            return;

        if (hadGoggles != goggles) {
            DistExecutor.unsafeRunWhenOn(
                    Dist.CLIENT,
                    () -> () -> VisualizationHelper.queueUpdate(this)
            );
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ClientConfig.syncFromFile();
        String[] pitches = CreateLang.translateDirect("generic.notes")
                .getString()
                .split(";");

        StopSize stopSize = ((GenericPipeBlockItem) getBlockState()
                .getBlock().asItem()).stopSize;

        int mutatedPitch = stopSize.getMutatedPitch(pitch);
        int mutatedOctave = stopSize.getMutatedOctave(pitch, getOctave());

        boolean useBrackets = ClientConfig.showOctaveBrackets;

        String octaveText = (useBrackets || mutatedOctave == -1)
                ? "(" + mutatedOctave + ")"
                : String.valueOf(mutatedOctave);


        CreateLang.translate("generic.pitch", pitches[mutatedPitch % pitches.length])
                .add(Component.literal(octaveText))
                .forGoggles(tooltip);

        return true;
    }


    protected boolean isPowered() {
        return getBlockState().getOptionalValue(GenericPipeBlock.POWERED)
                .orElse(false);
    }

    protected PipeSize getOctave() {
        return getBlockState().getOptionalValue(GenericPipeBlock.SIZE)
                .orElse(PipeSize.MEDIUM);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) return;
        FluidTankBlockEntity tank = getTank();

        BlockState state = getBlockState();
        BlockPos attachedPos = getBlockPos().relative(pipeBlock.get().getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);
        boolean isActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            isActive = windchest.isMasterActive(level, attachedState.getValue(GenericPipeBlock.FACING), attachedPos);
        }

        boolean powered = ((tank != null && tank.boiler.isActive() && (tank.boiler.passiveHeat || tank.boiler.activeHeat > 0)) || isActive) && isPowered();

        animation.chase(powered ? 1 : 0, powered ? .5f : .4f, powered ? LerpedFloat.Chaser.EXP : LerpedFloat.Chaser.LINEAR);
        animation.tickChaser();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.tickAudio(getOctave(), powered));
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void tickAudio(PipeSize size, boolean powered);

    public void createSteamJet(PipeSize size) {
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

    public void createReedSteamJet() {
        double yPos = pipeBlock.get().exactLengthForPitch(pitch) + 1 + STEAM_JET_OFFSET;
        Vec3 v = new Vec3(0, yPos, 0).add(Vec3.atBottomCenterOf(worldPosition));
        Vec3 m = new Vec3(0, 1, 0);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }
    public void createHorizontalReedSteamJet() {
        Direction facing = getBlockState().getOptionalValue(GenericPipeBlock.FACING)
                .orElse(Direction.SOUTH);
        float angle = 180 + AngleHelper.horizontalAngle(facing);
        Vec3 m = VecHelper.rotate(new Vec3(0, 0, 1), angle, Direction.Axis.Y);
        double yPos = pipeBlock.get().exactLengthForPitch(pitch) + 0.625f;
        Vec3 v = m.scale(yPos)
                .add(Vec3.atCenterOf(worldPosition));

        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }

    public void updatePitch() {
        Direction pipeOutFacing = pipeBlock.get().getExtensionDirection(getBlockState());
        BlockPos currentPos = worldPosition.relative(pipeOutFacing);
        int newPitch;
        for (newPitch = 0; newPitch <= 12; newPitch += pipeBlock.get().extensionsPerBlock()) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!(blockState.getBlock().equals(extensionBlock.get()))) {
                break;
            }
            ExtensionShapes.IExtensionShape<?> shape = blockState.getValue(extensionBlock.get().SHAPE);
            if (!shape.isFullBlockLong()) {
                newPitch += shape.extensionNumber();
                break;
            }
            currentPos = currentPos.relative(pipeOutFacing);
        }
        if (pitch == newPitch)
            return;
        pitch = newPitch;

        notifyUpdate();

        FluidTankBlockEntity tank = getTank();
        if (tank != null && tank.boiler != null)
            tank.boiler.checkPipeOrganAdvancement(tank);
    }

    @OnlyIn(Dist.CLIENT)
    protected void playChiffSound(float baseVolume) {
        if (level == null)
            return;

        float pitchFactor = (float) Math.pow(2, -pitch / 12.0);

        Vec3 eyePosition = Minecraft.getInstance().cameraEntity.getEyePosition();
        float distanceVolume = (float) Mth.clamp(
                (64 - eyePosition.distanceTo(Vec3.atCenterOf(worldPosition))) / 64,
                0, 1
        );

        float configVolume = (float) ClientConfig.whistleChiffVolume;

        AllSoundEvents.WHISTLE_CHIFF.playAt(
                level,
                worldPosition,
                distanceVolume * baseVolume * configVolume,
                pitchFactor,
                false
        );
    }


    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = pipeBlock.get().getAttachedDirection(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }
}
