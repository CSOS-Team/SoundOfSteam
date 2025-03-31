package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.sound.midi.ShortMessage;
import java.util.List;

public class StopMasterBlockEntity extends SmartBlockEntity {

    public KeyboardRelayBlockEntity linkedSource = null;

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), pos, state);
        // link source on placing, if set
        // method here
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

    public KeyboardRelayBlockEntity getSource() {
        return linkedSource;
    }

    public void linkToSource(KeyboardRelayBlockEntity source) {
        if (linkedSource == null) {
            linkedSource = source;
            source.linkStopMaster(this);
        }
    }

    public void removeSource(KeyboardRelayBlockEntity source) {
        if (linkedSource == source) {
            linkedSource = null;
            sendData();
        }
    }

    public void receiveMidiSignal(ShortMessage sm) {

    }

}
