package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class StopMasterBlockEntity extends SmartBlockEntity {

    public KeyboardRelayBlockEntity linkedSource = null;
    private int[] enabledChannels;

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

    public KeyboardRelayBlockEntity getSource() {
        return linkedSource;
    }

    public void linkToSource(KeyboardRelayBlockEntity source) {
        // initiated in stopmaster
        if (linkedSource == null) {
            linkedSource = source;
            source.linkStopMaster(this);
            sendData();
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
        linkedSource = null;
        sendData();
    }

    public void onBlockRemoved() {
        linkedSource.removeStopMaster(this); // remove this stopmaster from linked source
        // no need to remove linked source from this stopmaster because this stopmaster is about to get removed
    }

    public void receiveMidiSignal(MidiMessageServerObject mm) {
        if (mm.velocity > 0) { // if note on
            level.setBlock(worldPosition.above(), Blocks.STONE.defaultBlockState(), 3);
        } else { // if note off
            level.setBlock(worldPosition.above(), Blocks.AIR.defaultBlockState(), 3);
        }
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("SM"));
    }

}
