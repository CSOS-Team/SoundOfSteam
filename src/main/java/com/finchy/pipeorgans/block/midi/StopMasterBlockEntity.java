package com.finchy.pipeorgans.block.midi;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class StopMasterBlockEntity extends SmartBlockEntity {

    public KeyboardRelayBlockEntity linkedSource = null;

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(type, pos, state);
        // link source on placing, if set
        // method here
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

    public KeyboardRelayBlockEntity getSource() {
        return linkedSource;
    }

}
