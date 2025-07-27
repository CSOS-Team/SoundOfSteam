package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import com.finchy.pipeorgans.midi.PitchMapping;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.finchy.pipeorgans.util.MathUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.redstone.link.RedstoneLinkFrequencySlot;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DataFlowIssue")
public class StopMasterBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, MenuProvider {

    private BlockPos linkedCoord = null;

    private StopMasterLinkBehaviour link;
    private int transmittedSignal;
    private FilteringBehaviour filtering;

    private int channels = 0;

    private final List<Integer> activeNotes;

    public StopMasterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        setChannel(0, true);
        activeNotes = new ArrayList<>();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        filtering = new FilteringBehaviour(this, new StopMasterSlotPositioning());
        filtering.withCallback(this::setKeyFrequency);
        behaviours.add(filtering);
        createLink();
        behaviours.add(link);
    }

    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        createLink();
        behaviours.add(link);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        if (linkedCoord != null) { // if stopmaster has been linked
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", linkedCoord.getX()); // put x/y/z coords in tag
            posTag.putInt("y", linkedCoord.getY());
            posTag.putInt("z", linkedCoord.getZ());
            tag.put("source_coord", posTag); // add tag to NBT
        }
        tag.putInt("channels", channels);

        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        CompoundTag posTag = (CompoundTag) tag.get("source_coord"); // get coords from NBT
        if (posTag != null) {
            int x = posTag.getInt("x"); // get x/y/z
            int y = posTag.getInt("y");
            int z = posTag.getInt("z");
            linkedCoord = new BlockPos(x, y, z); // set pos as this blockentity's linked coord
        }
        channels = tag.getInt("channels");
        super.read(tag, clientPacket);
    }

    public InteractionResult use(Player player) {
        if (player == null || player instanceof FakePlayer)
            return InteractionResult.PASS;

        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        NetworkHooks.openScreen((ServerPlayer) player, this, worldPosition);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        String link;
        if (linkedCoord == null) {
            link = "None";
        } else {
            link = "(%d, %d, %d)".formatted(linkedCoord.getX(), linkedCoord.getY(), linkedCoord.getZ());
        }
        CreateLang.translate("goggles.stop_master_source", link).forGoggles(tooltip);
        return true;
    }



    // GUI

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.stop_master");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return StopMasterMenu.create(pContainerId, pPlayerInventory, this);
    }



    // REDSTONE LINK INTERFACE

    protected void createLink() {
        Pair<ValueBoxTransform, ValueBoxTransform> slots =
                ValueBoxTransform.Dual.makeSlots(RedstoneLinkFrequencySlot::new);
        link = StopMasterLinkBehaviour.transmitter(this, slots, this::getSignal);
    }

    public int getSignal() {
        return transmittedSignal;
    }

    public void setKeyFrequency(ItemStack a) {
        link.setKeyFrequency(a);
    }

    public void setNoteFrequency(int pitch, int velocity) {

        // determine frequency to be used for note
        ItemStack freq = PitchMapping.getStack(pitch);

        link.setNoteFrequency(freq, velocity>0);

        // convert 0-127 velocity to 0-15 redstone strength
        int mappedStrength = Math.round(MathUtils.map(velocity, 0, 127, 0, 15));

        transmit(mappedStrength);
    }

    public void transmit(int strength) {
        transmittedSignal = strength;
        if (link != null)
            link.notifySignalChange();
    }

    // MIDI

    public boolean getChannel(int channel) {
        int mask = 1 << channel;
        return (channels & mask) != 0;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannel(int channel, boolean state) {
        if (channel < 0) return;
        channels = state?
                ( channels | (1 << channel)) // all 0s, plus the bit that you want to set on
                : ( channels & (((int)(Math.pow(2, 16))-1) - (int)Math.pow(2, channel)) ); // all 1s, minus the bit that you want to set off
        notifyUpdate();
    }

    public void setChannels(int channels) {
        this.channels = channels;
        PipeOrgans.LOGGER.info("SMBE @ {}: SET CHANNELS TO {}", worldPosition.toShortString(), channels);
        notifyUpdate();
    }

    public void toggleChannel(int channel) {
        if (channel < 0) return;
        int mask = 1 << channel;
        channels = channels ^ mask;
        notifyUpdate();
    }

    public void linkToSource(MidiSourceBlockEntity source) {
        // initiated in stopmaster
        if (linkedCoord == null) {
            linkedCoord = source.getBlockPos();
            source.linkStopMaster(this);
            notifyUpdate();
        }
    }

    public void linkToSource(Level level, BlockPos pos) {
        // initiated in stopmaster
        if (level.getBlockEntity(pos) instanceof MidiSourceBlockEntity be) { // if pos actually corresponds to a midi source
            linkToSource(be);
        }
    }

    public void removeSource() {
        // initiated in midi source
        linkedCoord = null;
        notifyUpdate();
    }

    public void onBlockRemoved() {
        if (linkedCoord != null) {
            if (level.getBlockEntity(linkedCoord) instanceof MidiSourceBlockEntity source) { // get source at linked coord
                source.removeStopMaster(this); // remove this stopmaster from linked source
            }
        }
        for (int pitch : activeNotes) {
            setNoteFrequency(pitch, 0);
        }
        activeNotes.clear();
    }

    public void receiveMidiSignal(MidiMessageServerObject mm) {
        if (!getChannel(mm.channel)) {
            return;
        }

        PipeOrgans.LOGGER.info("NOTE: {}, VELOCITY: {}, CHANNEL: {}", mm.note, mm.velocity, mm.channel);

        if (mm.velocity > 0) { // if note on
            handleNoteOn(mm.note, mm.velocity);
        } else { // if note off
            handleNoteOff(mm.note, mm.velocity);
        }
    }

    public void stopAllNotes() {
        for (int pitch : activeNotes) {
            setNoteFrequency(pitch, 0);
        }
        activeNotes.clear();
        level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, false), 3); //  turn power off
    }

    private void handleNoteOn(int pitch, int velocity) {
        setNoteFrequency(pitch, velocity);
        if (activeNotes.isEmpty()) { // if a note has just been pressed
            level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, true), 3); //  turn power on
        }
        activeNotes.add(pitch);
    }

    private void handleNoteOff(int pitch, int velocity) {
        setNoteFrequency(pitch, velocity);
        if (!activeNotes.isEmpty()) { // if there are notes being held
            activeNotes.remove((Integer) pitch);
            if (activeNotes.isEmpty()) { // if the last note has just been taken off
                level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, false), 3); //  turn power off
            }
        }
    }
}
