package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity {

    private boolean playing = false;
    private int tickPosition = 0;
    private List<Queue<MidiEvent>> currentSequence = null;
    private String currentMidi = "";
    private String currentMidiOwner = "";
    private int ppq;
    private int tickStep = 1;

    public TrackerBarBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.TRACKER_BAR_BLOCK_ENTITY.get(), pos, state);
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
                PipeOrgans.LOGGER.error("Tried to load invalid sequence into tracker bar!");
                return;
            }
            currentSequence = MidiUtils.MidiFileParser.parseMidiEvents(sequence);
            ppq = MidiUtils.MidiFileParser.getResolution(sequence);
            currentMidi = midi;
            currentMidiOwner = owner;

        } catch (IOException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void setTempo(int microsPerQ) {
        float midiTPS = (1000000f / microsPerQ) * ppq;
        tickStep = Math.round(midiTPS/20);
    }

    private void tickSequencer() {
        for (Queue<MidiEvent> track : currentSequence) {
            while (track.peek() != null && track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();

                if (MidiUtils.isTempoChange(msg)) { // if it's a tempo change MetaMessage
                    MetaMessage meta = (MetaMessage) msg;
                    byte[] data = meta.getData(); // get data bytes
                    int newTempo = ((data[0] & 0xFF) << 16) | // combine into a single int
                            ((data[1] & 0xFF) << 8) |
                            (data[2] & 0xFF);
                    setTempo(newTempo);

                } else if (MidiUtils.isFileEnd(msg)) { // if it's a file end MetaMessage
                    resetSequencer();
                    haltAllStopMasters(); // ngl... i have no idea why there are notes remaining. but this fixes it. soo....
                    break;

                } else if (MidiUtils.isShortMessage(msg)) { // if it's a ShortMessage
                    ShortMessage sm = (ShortMessage) msg;
                    int channel = sm.getChannel();
                    int note = sm.getData1();
                    if (MidiUtils.isNoteOn(sm)) { // if it's a note on ShortMessage
                        int velocity = sm.getData2();
                        handleMidiObject(new MidiMessageServerObject(channel, note, velocity)); // distribute to linked stopmasters
                    } else if (MidiUtils.isNoteOff(sm)) { // if it's a note off ShortMessage
                        handleMidiObject(new MidiMessageServerObject(channel, note, 0)); // distribute to linked stopmasters with velocity 0
                    }
                }

            }
        }
        tickPosition += tickStep;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getTickPosition() {
        return tickPosition;
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
