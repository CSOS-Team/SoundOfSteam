package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class StopMasterBlockEntity extends SmartBlockEntity {

    private BlockPos linkedCoord = null;

    private int[] enabledChannels;

    public StopMasterBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.STOP_MASTER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        if (linkedCoord != null) { // if stopmaster has been linked
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", linkedCoord.getX()); // put x/y/z coords in tag
            posTag.putInt("y", linkedCoord.getY());
            posTag.putInt("z", linkedCoord.getZ());
            tag.put("source_coord", posTag); // add tag to NBT
        }
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        CompoundTag posTag = (CompoundTag) tag.get("source_coord"); // get coords from NBT
        int x = posTag.getInt("x"); // get x/y/z
        int y = posTag.getInt("y");
        int z = posTag.getInt("z");
        linkedCoord = new BlockPos(x, y, z); // set pos as this blockentity's linked coord

        super.read(tag, clientPacket);
    }

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

    public void receiveMidiSignal(MidiMessageServerObject mm) {
        if (mm.velocity > 0) { // if note on
            level.setBlock(worldPosition.above(), Blocks.STONE.defaultBlockState(), 3);
        } else { // if note off
            level.setBlock(worldPosition.above(), Blocks.AIR.defaultBlockState(), 3);
        }
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("SM"));
    }

    public CompoundTag createTag() {
        CompoundTag tag = new CompoundTag();
        BlockPos blockPos = getBlockPos();
        int[] writePos = new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
        tag.putIntArray("pos", writePos);
        return tag;
    }
}
