package com.finchy.pipeorgans.block.midiblocks;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.StopMasterDevice;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.sound.midi.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StopMasterBlockEntity extends SmartBlockEntity {

    private final StopMasterDevice stopMasterDevice;

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), pos, state);
        stopMasterDevice = new StopMasterDevice("id", this);

        try {
            System.out.println(new File(".").getAbsolutePath());
            if (level.isClientSide()) {return;}
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Transmitter t = sequencer.getTransmitter();
            Receiver r = stopMasterDevice.getReceiver();
            t.setReceiver(r);

            Path dir = Paths.get("schematics").toAbsolutePath();

            File midiFile = new File("ST-ANNE-SAMPLE-FILE.mid");
            Sequence sequence = MidiSystem.getSequence(midiFile);
            sequencer.setSequence(sequence);
            sequencer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

    public void sendPitchState(int pitch, boolean status) {
        PipeOrgans.LOGGER.info("SMBlock: Received {} {}", pitch, status);
    }
}
