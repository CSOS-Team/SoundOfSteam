package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@SuppressWarnings("DataFlowIssue")
public abstract class MidiSourceBlockEntity extends SmartBlockEntity {

    private final List<BlockPos> linkedCoords = new ArrayList<>();
    private final HashMap<Integer, RedstoneMidiLink.ManualNoteFrequency> activeNotes;
    private RedstoneMidiLink link;

    public MidiSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        activeNotes = new HashMap<>();
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        link = new RedstoneMidiLink(pLevel, worldPosition);
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
        if (sm.getData2() > 0) { // if note on
            PipeOrgans.LOGGER.info("NOTE ON");
            if (!link.areNotesActive()) { // if no notes are currently pressed
                reactToNote(true);
                PipeOrgans.LOGGER.info("TURNED POWER ON");
            }
            link.activateNote(sm.getChannel(), sm.getData1(), sm.getData2());
            PipeOrgans.LOGGER.info("ACTIVATED NOTE");

        } else { // if note off
            PipeOrgans.LOGGER.info("NOTE OFF");
            link.deactivateNote(sm.getChannel(), sm.getData1());
            if (!link.areNotesActive()) { // if the last note has just been taken off
                reactToNote(false);
                PipeOrgans.LOGGER.info("TURNED POWER OFF");
            }
            PipeOrgans.LOGGER.info("DEACTIVATED NOTE");
        }
    }

    public void reactToNote(boolean on) {
        level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, on), 3); //  turn power on/off
    }

    public void onBlockRemoved() {
        //removeFromAllStopMasters();
    }
}
