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

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import java.util.List;
import java.util.Queue;

public class TrackerBarBlockEntity extends MidiSourceBlockEntity {

    private boolean playing = false;
    private int tickPosition = 0;
    private List<Queue<MidiEvent>> currentSequence = null;
    private int microsPerQ;
    private int PPQ;

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
        currentSequence = MidiUtils.MidiParser.parseMidiFile(midi, owner);
    }

    private void tickSequencer() {
        tickPosition += 1; // temporary value until i figure out tempo properly
        for (Queue<MidiEvent> track : currentSequence) {
            //PipeOrgans.LOGGER.info("CURRENTLY: {}, MESSAGE: {}", tickPosition, track.peek().getTick());
            if (track.peek().getTick() <= tickPosition) {
                MidiMessage msg = track.poll().getMessage();
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.valueOf(msg.getMessage()[1])));
            }
        }
        tickPosition += 1; // temporary value until i figure out tempo properly
    }

    public void startSequencer() {
        playing = true;
        PipeOrgans.LOGGER.info("STARTED PLAYING");
    }

    public void stopSequencer() {
        playing = false;
        PipeOrgans.LOGGER.info("STOPPED PLAYING");
    }
}
