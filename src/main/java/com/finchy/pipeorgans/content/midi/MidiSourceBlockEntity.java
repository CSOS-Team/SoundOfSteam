package com.finchy.pipeorgans.content.midi;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("DataFlowIssue")
public abstract class MidiSourceBlockEntity extends SmartBlockEntity {

    private final List<BlockPos> linkedCoords = new ArrayList<>();
    private int activeNotes;

    public MidiSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        ListTag coordsList = new ListTag();
        for (BlockPos pos : linkedCoords) { // for every linked position
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX()); // put x/y/z coords in tag
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            coordsList.add(posTag); // add coords to list
        }
        tag.put("linked_coords", coordsList); // add list to NBT

        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        ListTag coordsList = tag.getList("linked_coords", Tag.TAG_COMPOUND); // get coords from NBT
        linkedCoords.clear(); // clear this blockentity's current list

        for (int i=0; i<coordsList.size(); i++) { // for every coord in list
            CompoundTag posTag = coordsList.getCompound(i);
            int x = posTag.getInt("x"); // get x/y/z
            int y = posTag.getInt("y");
            int z = posTag.getInt("z");
            linkedCoords.add(new BlockPos(x, y, z)); // add pos to this blockentity's list
        }
        super.read(tag, clientPacket);
    }

    public abstract void handleMidiMessage(MidiMessage mm);

    public void handleNote(ShortMessage sm) {
        /*
        if (mm.velocity > 0) { // if note on
            if (activeNotes == 0) { // if a note has just been pressed
                reactToNote(true);
            }
            activeNotes++;

        } else { // if note off
            if (activeNotes>0) { // if there are notes being held
                activeNotes--;
                if (activeNotes == 0) { // if the last note has just been taken off
                    reactToNote(false); //  turn power off
                }
            }
        }
         */
    }

    public void reactToNote(boolean on) {
        level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, on), 3); //  turn power on/off
    }

    public void onBlockRemoved() {
        //removeFromAllStopMasters();
    }
}
