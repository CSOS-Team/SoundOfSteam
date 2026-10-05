package com.finchy.pipeorgans.content.noteLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.content.equipment.clipboard.ClipboardContent;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class NoteLinkBehaviour extends BlockEntityBehaviour implements IRedstoneLinkable, ClipboardCloneable {

    public static final BehaviourType<NoteLinkBehaviour> TYPE = new BehaviourType<>();

    public enum Mode {
        TRANSMIT,
        RECEIVE
    }

    private RedstoneLinkNetworkHandler.Frequency keyFrequency;
    private PipePitch pitch;

    public boolean newPosition;
    private Mode mode;
    private IntSupplier transmission;
    private IntConsumer updateSignalCallback;

    private Runnable onLoadedCallback = null;

    protected NoteLinkBehaviour(SmartBlockEntity be) {
        super(be);
        keyFrequency = RedstoneLinkNetworkHandler.Frequency.EMPTY;
        pitch = PipePitch.DEFAULT;
        newPosition = true;
    }

    public static NoteLinkBehaviour receiver(SmartBlockEntity be, IntConsumer updateSignalCallback) {
        NoteLinkBehaviour noteLinkBehaviour = new NoteLinkBehaviour(be);
        noteLinkBehaviour.updateSignalCallback = updateSignalCallback;
        noteLinkBehaviour.mode = Mode.RECEIVE;
        return noteLinkBehaviour;
    }

    public static NoteLinkBehaviour transmitter(SmartBlockEntity be, IntSupplier transmission) {
        NoteLinkBehaviour noteLinkBehaviour = new NoteLinkBehaviour(be);
        noteLinkBehaviour.transmission = transmission;
        noteLinkBehaviour.mode = Mode.TRANSMIT;
        return noteLinkBehaviour;
    }

    // FIXME: The network setup seems to not work well on world load. Possible solutions:
    // - Defer the link behaviour addition like the redstone link does it
    // - Mixin to the network handler to apply the "one-to-one loading problem" fix implemented there for LinkBehavior
    //   (see https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/content/redstone/link/RedstoneLinkNetworkHandler.java#L122)
    // Both done, still seems to have issues. Needs further investigation.

    public NoteLinkBehaviour withOnLoadedCallback(Runnable onLoadedCallback) {
        this.onLoadedCallback = onLoadedCallback;
        return this;
    }

    public void copyDataFrom(NoteLinkBehaviour behaviour) {
        if (behaviour == null)
            return;
        keyFrequency = behaviour.keyFrequency;
        pitch = behaviour.pitch;
    }

    @Override
    public boolean isListening() {
        return mode == Mode.RECEIVE;
    }

    @Override
    public int getTransmittedStrength() {
        return mode == Mode.TRANSMIT ? transmission.getAsInt() : 0;
    }

    @Override
    public void setReceivedStrength(int power) {
        if (!newPosition)
            return;
        updateSignalCallback.accept(power);
    }

    public void notifySignalChange() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(getWorld(), this);
    }

    @Override
    public void initialize() {
        //PipeOrgans.LOGGER.debug("NoteLinkBehaviour initializing at {}", blockEntity.getBlockPos());
        if (onLoadedCallback == null)
            if (blockEntity instanceof NoteLinkBehaviourSubscriber nlbs)
                onLoadedCallback = nlbs::onNoteLinkBehaviorLoaded;
            else
                onLoadedCallback = () -> PipeOrgans.LOGGER.warn("Empty NoteLinkBehaviour onLoadedCallback. Block Entities should implement NoteLinkBehaviourSubscriber");
        super.initialize();
        if (getWorld().isClientSide)
            return;
        connectToNetwork();
        newPosition = true;
    }

    @Override
    public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
        return Couple.create(keyFrequency, pitch.getMappedFrequency());
    }

    @Override
    public void unload() {
        super.unload();
        if (getWorld().isClientSide)
            return;
        disconnectFromNetwork();
    }

    @Override
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(nbt, registries, clientPacket);
        nbt.put("Key", keyFrequency.getStack().saveOptional(registries));
        nbt.putString("Pitch", pitch.getNormalizedName());
        nbt.putLong("LastKnownPosition", blockEntity.getBlockPos()
                .asLong());
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        long positionInTag = blockEntity.getBlockPos()
                .asLong();
        long positionKey = nbt.getLong("LastKnownPosition");
        newPosition = positionInTag != positionKey;

        super.read(nbt, registries, clientPacket);

        keyFrequency = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.parseOptional(registries, nbt.getCompound("Key")));

        if (!nbt.contains("Pitch"))
            pitch = PipePitch.DEFAULT;
        else
            pitch = PipePitch.fromNormalizedName(nbt.getString("Pitch"));

        if (onLoadedCallback == null)
            PipeOrgans.LOGGER.warn("NoteLinkBehaviour read from NBT with null onLoadedCallback");
        else
            onLoadedCallback.run();
        //PipeOrgans.LOGGER.debug("NoteLinkBehaviour read from NBT: keyFrequency={}, pitch={}, newPos={}", keyFrequency.getStack(), pitch.getNormalizedName(), newPosition);
    }



    public void updateHeldClipboard(Player player) {
        ItemStack mainhand = player.getMainHandItem(); // get item in mainhand
        boolean mainhandIsClipboard = mainhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem());
        ItemStack offhand = player.getOffhandItem(); // get item in offhand
        boolean offhandIsClipboard = offhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem());
        if (!mainhandIsClipboard && !offhandIsClipboard) return; // if the player isn't holding any clipboards, return

        ItemStack clipboardStack;
        if (mainhandIsClipboard) // if there's a clipboard in the mainhand, prioritise that
            clipboardStack = mainhand;
        else // otherwise use the clipboard in the offhand
            clipboardStack = offhand;

        ClipboardContent clipboardContent = clipboardStack.getOrDefault(AllDataComponents.CLIPBOARD_CONTENT, ClipboardContent.EMPTY);
        
        CompoundTag copiedTag = new CompoundTag(); // make a new tag to put in "CopiedValues"
        CompoundTag musicalFreqTag = new CompoundTag(); // make a new tag to put in "MusicalFrequency"
        musicalFreqTag.putString("Pitch", pitch.getNormalizedName()); // default to F#-1
        musicalFreqTag.put("Key", getKey().saveOptional(getWorld().registryAccess())); // default to no key item
        musicalFreqTag.putBoolean("Receiver", blockEntity.getBlockState().getValue(NoteLinkBlock.RECEIVER));

        copiedTag.put("MusicalFrequency", musicalFreqTag);
        clipboardStack.set(AllDataComponents.CLIPBOARD_CONTENT, clipboardContent.setCopiedValues(copiedTag)); // apply the tags to the clipboard
    }



    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    private RedstoneLinkNetworkHandler getHandler() {
        return Create.REDSTONE_LINK_NETWORK_HANDLER;
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
    public String getClipboardKey() {
        return "MusicalFrequency";
    }
    
    //todo: move clipboard behaviour from behaviour into block entity

    @Override
    public boolean writeToClipboard(HolderLookup.@NotNull Provider registries, CompoundTag tag, Direction side) {
        tag.put("Key", keyFrequency.getStack().saveOptional(registries));
        tag.putString("Pitch", pitch.getNormalizedName());
        return true;
    }

    @Override
    public boolean readFromClipboard(HolderLookup.@NotNull Provider registries, CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!tag.contains("Key") || !tag.contains("Pitch"))
            return false;
        if (simulate) return true;

        setKeyFrequency(ItemStack.parseOptional(registries, tag.getCompound("Key")));
        PipePitch readPitch = PipePitch.fromNormalizedName(tag.getString("Pitch"));
        setPitch(readPitch);
        
        if (blockEntity instanceof NoteLinkBlockEntity nlbe) {
            nlbe.setPitchSlot(readPitch);
        }
        
        return true;
    }

    protected void disconnectFromNetwork() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(getWorld(), this);
    }

    protected void connectToNetwork() {
        blockEntity.sendData();
        Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(getWorld(), this);
    }

    public void setKeyFrequency(ItemStack stack) {
        stack = stack.copy();
        stack.setCount(1);
        ItemStack toCompare = getKey();
        boolean changed = !ItemStack.isSameItemSameComponents(stack, toCompare);

        if (changed)
            disconnectFromNetwork();

        this.keyFrequency = RedstoneLinkNetworkHandler.Frequency.of(stack);
        //PipeOrgans.LOGGER.debug("NoteLinkBehaviour changed key frequency to {}", keyFrequency.getStack());

        if (!changed)
            return;

        connectToNetwork();
        //PipeOrgans.LOGGER.debug("NoteLinkBehaviour updated network connection after key frequency change");
    }

    public void setPitch(PipePitch pitch) {
        disconnectFromNetwork();

        this.pitch = pitch;
        //PipeOrgans.LOGGER.debug("NoteLinkBehaviour changed pitch to {}", pitch.getNormalizedName());

        connectToNetwork();
        //PipeOrgans.LOGGER.debug("NoteLinkBehaviour updated network connection after pitch change");
    }

    public boolean hasNewPos() {
        return newPosition;
    }

    public void ackNewPos() {
        this.newPosition = false;
    }
    public void forceNewPos() {
        this.newPosition = true;
    }

    public RedstoneLinkNetworkHandler.Frequency getKeyFrequency() {
        return keyFrequency;
    }

    public ItemStack getKey() {
        ItemStack is = keyFrequency.getStack().copy();
        is.setCount(1);
        return is;
    }

    public PipePitch getPitch() {
        return pitch;
    }
}
