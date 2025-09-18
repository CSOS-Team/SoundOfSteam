package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;

//@SuppressWarnings("DataFlowIssue")
public abstract class MidiSourceBlockEntity extends SmartBlockEntity implements MenuProvider {

    public ItemStackHandler storedGhostInv;
    protected RedstoneMidiLink link;

    public MidiSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        storedGhostInv = new ItemStackHandler(16) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return true; // change this later
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
                if (!level.isClientSide()) {
                    link.changeFrequencyKey(slot, getStackInSlot(slot));
                }
            }
        };
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        link = new RedstoneMidiLink(pLevel, worldPosition);
        link.setFrequencyKeysOnLoad(storedGhostInv);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.put("frequencyItems", storedGhostInv.serializeNBT());
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        storedGhostInv.deserializeNBT(tag.getCompound("frequencyItems"));
        super.read(tag, clientPacket);
    }

    public abstract void handleMidiMessage(MidiMessage mm);

    public void handleNote(ShortMessage sm) {
        if (MidiUtils.isNoteOn(sm)) { // if note on
            if (!link.areNotesActive()) { // if no notes are currently pressed
                reactToNote(true);
            }
            link.activateNote(sm.getChannel(), sm.getData1(), sm.getData2());

        } else { // if note off
            link.deactivateNote(sm.getChannel(), sm.getData1());
            if (!link.areNotesActive()) { // if the last note has just been taken off
                reactToNote(false);
            }
        }
    }

    public void reactToNote(boolean on) {
        level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, on), 3); //  turn power on/off
    }

    public void onBlockRemoved() {
        link.stopAllNotes();
    }

}
