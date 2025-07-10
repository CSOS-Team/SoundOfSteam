package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity {

    private boolean playing = false;
    private int tickPosition = 0;
    private List<Queue<MidiEvent>> currentSequence = null;
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
            if (sequence == null)
                return;
            currentSequence = MidiUtils.MidiFileParser.parseMidiEvents(sequence);
            ppq = MidiUtils.MidiFileParser.getResolution(sequence);

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
            while ((track.peek() != null ? track.peek().getTick() : 0) <= tickPosition) {
                MidiMessage msg = Objects.requireNonNull(track.poll()).getMessage();
                // CHECK FOR TEMPO CHANGES
                // IF TEMPO CHANGE, CALL setTempo() WITH NEW MPQ VALUE
                if (MidiUtils.isTempoChange(msg)) {
                    MetaMessage meta = (MetaMessage) msg;
                    byte[] data = meta.getData();
                    int newTempo = ((data[0] & 0xFF) << 16) |
                            ((data[1] & 0xFF) << 8) |
                            (data[2] & 0xFF);
                    setTempo(newTempo);
                }
                // OTHERWISE, CONSIDER IT A NOTE ON/OFF MSG
                // NEED TO LOOK INTO OTHER KINDS OF META MESSAGES TO HANDLE
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(msg.getMessage()[1])));

                if (track.peek() == null) { // at the end of the song
                    resetSequencer();
                    break;
                }

            }
        }
        tickPosition += tickStep; // temporary value until i figure out tempo properly
    }

    public void resetSequencer() {
        playing = false;
        tickPosition = 0;
        tickStep = 1;
    }

    public void startSequencer() {
        playing = true;
        PipeOrgans.LOGGER.info("STARTED PLAYING");
    }

    public void stopSequencer() {
        playing = false;
        PipeOrgans.LOGGER.info("STOPPED PLAYING");
    }

    public void toggleSequencer() {
        playing = !playing;
        PipeOrgans.LOGGER.info("TOGGLED PLAYING");
    }
}
