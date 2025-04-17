package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.gui.StopMasterMenu;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.pitchMappings.AllPitchMappings;
import com.finchy.pipeorgans.midi.pitchMappings.PitchMapping;
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
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("DataFlowIssue")
public class StopMasterBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, MenuProvider {

    private BlockPos linkedCoord = null;

    private PitchMapping mapping;

    private StopMasterLinkBehaviour link;
    private int transmittedSignal;
    private FilteringBehaviour filtering;

    private ArrayList<Integer> enabledChannels = new ArrayList<>(){};

    private int activeNotes;

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), pos, state);
        addChannel(0);
        addChannel(1);
        addChannel(2);
        addChannel(3);// DEVELOPMENT ONLY
        mapping = AllPitchMappings.getMapping("pipe_centric"); // DEVELOPMENT ONLY
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
        tag.putIntArray("channels", enabledChannels);
        if (mapping != null) {
            tag.putString("mapping", mapping.id());
        } else {
            tag.putString("mapping", "pipe_centric");
        }
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

        String mappingID = tag.getString("mapping");
        mapping = AllPitchMappings.getMapping(mappingID);

        super.read(tag, clientPacket);
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
        return new StopMasterMenu(pContainerId, pPlayerInventory, this);
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
        ItemStack freq = mapping.getStack(pitch);

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

    // REMEMBER CHANNELS START AT ZERO!
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
    }

    private void handleNoteOn(int pitch, int velocity) {
        setNoteFrequency(pitch, velocity);
        if (activeNotes == 0) { // if a note has just been pressed
            level.setBlock(worldPosition, getBlockState().setValue(StopMasterBlock.POWERED, true), 3); //  turn power on
        }
        activeNotes += 1;
    }

    private void handleNoteOff(int pitch, int velocity) {
        setNoteFrequency(pitch, velocity);
        if (activeNotes>0) { // if there are notes being held
            activeNotes -= 1;
            if (activeNotes == 0) { // if the last note has just been taken off
                level.setBlock(worldPosition, getBlockState().setValue(StopMasterBlock.POWERED, false), 3); //  turn power off
            }
        }
    }
}
