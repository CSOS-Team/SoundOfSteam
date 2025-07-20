package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity implements MenuProvider {

    public boolean playing = false;
    public int tickPosition = 0;
    private List<Queue<MidiEvent>> currentSequence = null;
    private String currentMidi = "";
    private String currentMidiOwner = "";
    private int ppq;
    private int tickStep = 1;
    public double bpm;
    public final List<String> channelInstruments;

    public TrackerBarInventory inventory;

    public class TrackerBarInventory extends ItemStackHandler {
        public TrackerBarInventory() {
            super(2);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    }

    public TrackerBarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        channelInstruments = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            channelInstruments.add(MidiUtils.GeneralMidiInstrument.EMPTY.name);
        }
        inventory = new TrackerBarInventory();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Tracker Bar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return TrackerBarMenu.create(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void tick() {
        super.tick();
        if (playing) {
            tickSequencer();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void loadSequence(String midi, String owner) {
        try {
            Sequence sequence = MidiUtils.MidiFileParser.getSequenceFromFile(midi, owner);
            if (sequence == null) {
                PipeOrgans.LOGGER.error("Tried to load invalid sequence into tracker bar! {}, {}", midi, owner);
                return;
            }
            currentSequence = MidiUtils.MidiFileParser.parseMidiEvents(sequence);
            ppq = MidiUtils.MidiFileParser.getResolution(sequence);
            MidiUtils.MidiFileParser.initialParse(sequence, this::setChannelInstrument, this::setTempo);
            currentMidi = midi;
            currentMidiOwner = owner;

        } catch (IOException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void setChannelInstrument(int channel, int program) {
        channelInstruments.set(channel, MidiUtils.GeneralMidiInstrument.fromProgram(program).name);
    }

    private void setTempo(byte[] data) {
        int microsPerQ = ((data[0] & 0xFF) << 16) | // combine into a single int
                ((data[1] & 0xFF) << 8) |
                (data[2] & 0xFF);
        float midiTPS = (1000000f / microsPerQ) * ppq;
        tickStep = Math.round(midiTPS/20);
        bpm = 60000000.0 / microsPerQ;
    }

    private void tickSequencer() {
        for (Queue<MidiEvent> track : currentSequence) {
            while (track.peek() != null && track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();

                if (msg instanceof MetaMessage mm) { // if it's a MetaMessage

                    if (MidiUtils.isTempoChange(mm)) { // if it's a tempo change MetaMessage
                        setTempo(mm.getData());

                    } else if (MidiUtils.isFileEnd(mm)) { // if it's a file end MetaMessage
                        resetSequencer();
                        haltAllStopMasters(); // ngl... i have no idea why there are notes remaining. but this fixes it. soo....
                        break;
                    }

                } else if (msg instanceof ShortMessage sm) { // if it's a ShortMessage

                    int channel = sm.getChannel();
                    int data1 = sm.getData1();
                    if (MidiUtils.isNoteOn(sm)) { // if it's a note on ShortMessage
                        int velocity = sm.getData2();
                        handleMidiObject(new MidiMessageServerObject(channel, data1, velocity)); // distribute to linked stopmasters
                    } else if (MidiUtils.isNoteOff(sm)) { // if it's a note off ShortMessage
                        handleMidiObject(new MidiMessageServerObject(channel, data1, 0)); // distribute to linked stopmasters with velocity 0

                    } else if (MidiUtils.isProgramChange(sm)) { // setting instrument on a particular channel
                        channelInstruments.set(channel, MidiUtils.GeneralMidiInstrument.fromProgram(data1).name);
                    }
                }

            }
        }
        tickPosition += tickStep;
    }

    public String getCurrentMidi() {
        return currentMidi;
    }

    public String getCurrentMidiOwner() {
        return currentMidiOwner;
    }

    public void resetSequencer() {
        playing = false;
        tickPosition = 0;
        tickStep = 1;
    }

    public void resumeSequencer() {
        playing = true;
        PipeOrgans.LOGGER.info("RESUMED PLAYING");
    }

    public void pauseSequencer() {
        playing = false;
        PipeOrgans.LOGGER.info("PAUSED PLAYING");
    }

    public void startSequencer() {
        resetSequencer();
        playing = true;
        PipeOrgans.LOGGER.info("STARTED PLAYING");
    }

    public void stopSequencer() {
        resetSequencer();
        PipeOrgans.LOGGER.info("STOPPED PLAYING");
    }

    public void toggleSequencer() {
        playing = !playing;
        PipeOrgans.LOGGER.info("TOGGLED PLAYING");
    }

}
