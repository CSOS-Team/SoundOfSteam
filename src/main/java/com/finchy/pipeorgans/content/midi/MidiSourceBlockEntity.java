package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockEntity;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

public abstract class MidiSourceBlockEntity extends SmartBlockEntity {

    private final List<BlockPos> linkedCoords = new ArrayList<>();
    private int activeNotes;

    public MidiSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void handleMidiObject(MidiMessageServerObject mm) {
        for (BlockPos pos : linkedCoords) { // for every linked position
            if (level.getBlockEntity(pos) instanceof StopMasterBlockEntity sm) { // if stopmaster is at that location
                sm.receiveMidiSignal(mm); // send midi to stopmaster
            }
        }
        if (mm.velocity > 0) { // if note on
            if (activeNotes == 0) { // if a note has just been pressed
                level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, true), 3); //  turn power on
            }
            activeNotes++;

        } else { // if note off
            if (activeNotes>0) { // if there are notes being held
                activeNotes--;
                if (activeNotes == 0) { // if the last note has just been taken off
                    level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, false), 3); //  turn power off
                }
            }
        }
    }

    public void linkStopMaster(StopMasterBlockEntity be) {
        BlockPos pos = be.getBlockPos(); // get pos of stopmaster
        if (!linkedCoords.contains(pos)) { // if stopmaster has not already been linked
            linkedCoords.add(pos); // add pos to list
        }
        notifyUpdate();
    }

    public void removeStopMaster(StopMasterBlockEntity be) {
        linkedCoords.remove(be.getBlockPos()); // remove pos from list
        notifyUpdate();
    }

    public void removeFromAllStopMasters() {
        for (BlockPos pos : linkedCoords) { // for every linked position
            if (level.getBlockEntity(pos) instanceof StopMasterBlockEntity sm) { // if stopmaster is at that location
                sm.removeSource(); // remove source from stopmaster
            }
        }
    }

    public void onBlockRemoved() {
        removeFromAllStopMasters();
    }
}
