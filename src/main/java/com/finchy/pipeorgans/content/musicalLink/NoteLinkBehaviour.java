package com.finchy.pipeorgans.content.musicalLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class NoteLinkBehaviour extends BlockEntityBehaviour implements IRedstoneLinkable, ClipboardCloneable {

    public static final BehaviourType<NoteLinkBehaviour> TYPE = new BehaviourType<>();

    public NoteLinkBehaviour(SmartBlockEntity be, IntSupplier transmitter, IntConsumer receiver) {
        super(be);
        this.transmitter = transmitter;
        this.receiver = receiver;
    }

    public enum Mode {
        TRANSMIT,
        RECEIVE
    }

    Mode mode = Mode.TRANSMIT;
    IntSupplier transmitter;
    IntConsumer receiver;
    RedstoneLinkNetworkHandler.Frequency keyFrequency = RedstoneLinkNetworkHandler.Frequency.EMPTY;
    PipePitch pitch = PipePitch.INVALID;
    boolean newPos;
    boolean inNetwork;

    @Override
    public String getClipboardKey() {
        return "MusicalFrequency";
    }

    @Override
    public boolean writeToClipboard(CompoundTag tag, Direction side) {
        tag.put("Key", keyFrequency.getStack().serializeNBT());
        tag.putString("Pitch", pitch.getNormalizedName());
        return true;
    }

    @Override
    public boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!tag.contains("Key") || !tag.contains("Pitch"))
            return false;
        if (simulate) return true;

        keyFrequency = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(tag.getCompound("Key")));
        pitch = PipePitch.fromNormalizedName(tag.getString("Pitch"));
        return true;
    }

    public void notifySignalChange() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(getWorld(), this);
    }

    protected void disconnectFromNetwork() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(getWorld(), this);
        inNetwork = false;
    }

    protected void connectToNetwork() {
        blockEntity.sendData();
        Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(getWorld(), this);
        inNetwork = true;
    }

    public void changeKeyFrequency(ItemStack stack) {
        ItemStack is = stack.copy();
        is.setCount(1);
        if (inNetwork) disconnectFromNetwork();
        this.keyFrequency = RedstoneLinkNetworkHandler.Frequency.of(is);
        PipeOrgans.LOGGER.debug("NoteLinkBehaviour changed key frequency to {}", keyFrequency.getStack());
        if (!inNetwork) {
            connectToNetwork();
            PipeOrgans.LOGGER.debug("NoteLinkBehaviour updated network connection after key frequency change");
        }
    }

    public void changePitch(PipePitch pitch) {
        disconnectFromNetwork();
        this.pitch = pitch;
        PipeOrgans.LOGGER.debug("NoteLinkBehaviour changed pitch to {}", pitch.getNormalizedName());
        if (!inNetwork) {
            connectToNetwork();
            PipeOrgans.LOGGER.debug("NoteLinkBehaviour updated network connection after pitch change");
        }
    }

    public void changeMode(Mode mode) {
        if (inNetwork) disconnectFromNetwork();
        this.mode = mode;
        if (!inNetwork) connectToNetwork();
    }

    public void toggleMode() {
        if (this.mode == Mode.RECEIVE) {
            changeMode(Mode.TRANSMIT);
        } else {
            changeMode(Mode.RECEIVE);
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        if (getWorld().isClientSide)
            return;
        connectToNetwork();
    }

    @Override
    public void unload() {
        super.unload();
        if (getWorld().isClientSide)
            return;
        disconnectFromNetwork();
    }

    @Override
    public int getTransmittedStrength() {
        return mode == Mode.TRANSMIT ? transmitter.getAsInt() : 0;
    }

    @Override
    public void setReceivedStrength(int power) {
        receiver.accept(power);
    }

    @Override
    public boolean isListening() {
        return mode == Mode.RECEIVE;
    }

    @Override
    public boolean isAlive() {
        Level level = getWorld();
        BlockPos pos = getPos();
        if (blockEntity.isChunkUnloaded())
            return false;
        if (blockEntity.isRemoved())
            return false;
        if (!level.isLoaded(pos))
            return false;
        return level.getBlockEntity(pos) == blockEntity;
    }

    @Override
    public BlockPos getLocation() {
        return getPos();
    }

    @Override
    public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
        return Couple.create(keyFrequency, pitch.getMappedFrequency());
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public boolean hasNewPos() {
        return newPos;
    }

    public void ackNewPos() {
        this.newPos = false;
    }

    @Override
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.put("Key", keyFrequency.getStack().serializeNBT());
        nbt.putString("Pitch", pitch.getNormalizedName());
        nbt.putLong("LastKnownPosition", blockEntity.getBlockPos()
                .asLong());
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        long positionInTag = blockEntity.getBlockPos()
                .asLong();
        long positionKey = nbt.getLong("LastKnownPosition");
        newPos = positionInTag != positionKey;

        super.read(nbt,  clientPacket);
        keyFrequency = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("Key")));
        pitch = PipePitch.fromNormalizedName(nbt.getString("Pitch"));
        PipeOrgans.LOGGER.debug("NoteLinkBehaviour read from NBT: keyFrequency={}, pitch={}, newPos={}", keyFrequency.getStack(), pitch.getNormalizedName(), newPos);
    }
}
