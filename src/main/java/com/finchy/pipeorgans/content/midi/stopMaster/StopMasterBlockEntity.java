package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.finchy.pipeorgans.util.MathUtils;

import com.simibubi.create.content.redstone.link.RedstoneLinkFrequencySlot;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("DataFlowIssue")
public class StopMasterBlockEntity extends SmartBlockEntity {

    private BlockPos linkedCoord = null;

    private StopMasterLinkBehaviour link;
    private int transmittedSignal;

    private ArrayList<Integer> enabledChannels = new ArrayList<>(){};

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), pos, state);
        addChannel(1); // DEVELOPMENT ONLY
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
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
        tag.putIntArray("channels", enabledChannels);
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
        int[] channels = tag.getIntArray("channels");
        enabledChannels = Arrays.stream(channels).boxed().collect(Collectors.toCollection(ArrayList::new));

        super.read(tag, clientPacket);
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

    public void setNoteFrequency(int pitch, int velocity) {
        // determine frequency to be used for note
        ItemStack freq = pitch==60? new ItemStack(Items.COBBLESTONE):new ItemStack(Items.OAK_PLANKS);

        link.setFrequency(freq, new ItemStack(Items.AIR), velocity>0);

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

    public void linkToSource(KeyboardRelayBlockEntity source) {
        // initiated in stopmaster
        if (linkedCoord == null) {
            linkedCoord = source.getBlockPos();
            source.linkStopMaster(this);
            notifyUpdate();
        }
    }

    public void linkToSource(Level level, BlockPos pos) {
        // initiated in stopmaster
        if (level.getBlockEntity(pos) instanceof KeyboardRelayBlockEntity be) { // if pos actually corresponds to a midi source
            linkToSource(be);
        }
    }

    public void removeSource() {
        // initiated in KBR
        linkedCoord = null;
        notifyUpdate();
    }

    public void onBlockRemoved() {
        if (linkedCoord != null) {
            if (level.getBlockEntity(linkedCoord) instanceof KeyboardRelayBlockEntity kbr) { // get kbr at linked coord
                kbr.removeStopMaster(this); // remove this stopmaster from linked source
            }
        }
    }

    public void addChannel(int channel) {
        if (!enabledChannels.contains(channel)) {
            enabledChannels.add(channel);
            enabledChannels.sort(Integer::compareTo);
        }
    }

    public void removeChannel(int channel) {
        if (enabledChannels.contains(channel)) {
            enabledChannels.remove(channel);
        }
    }

    public void toggleChannel(int channel) {
        if (enabledChannels.contains(channel)) {
            enabledChannels.remove(channel);
        } else {
            enabledChannels.add(channel);
            enabledChannels.sort(Integer::compareTo);
        }
    }

    public void receiveMidiSignal(MidiMessageServerObject mm) {
        if (!enabledChannels.contains(mm.channel)) {
            return;
        }

        if (mm.velocity > 0) { // if note on
            level.setBlock(worldPosition.above(), Blocks.STONE.defaultBlockState(), 3); // TESTING ONLY
            handleNoteOn(mm.note, mm.velocity);
        } else { // if note off
            level.setBlock(worldPosition.above(), Blocks.AIR.defaultBlockState(), 3); // TESTING ONLY
            handleNoteOff(mm.note, mm.velocity);
        }
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("SM"));
    }

    private void handleNoteOn(int pitch, int velocity) {
        setNoteFrequency(pitch, velocity);
    }

    private void handleNoteOff(int pitch, int velocity) {
        setNoteFrequency(pitch, velocity);
    }
}
