package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlock;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.sound.midi.ShortMessage;
import java.util.List;

public class MidiSourceBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<MidiSourceBehaviour> TYPE = new BehaviourType<>();

    public ItemStackHandler storedGhostInv;
    public RedstoneMidiLink link;

    protected Level level;
    protected BlockPos pos;

    public MidiSourceBehaviour(SmartBlockEntity be) {
        super(be);
        level = be.getLevel();
        pos = be.getBlockPos();

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
                blockEntity.setChanged();
                if (!blockEntity.getLevel().isClientSide()) {
                    link.changeFrequencyKey(slot, getStackInSlot(slot));
                }
            }
        };

        link = new RedstoneMidiLink(be);
        link.setFrequencyKeysOnLoad(storedGhostInv);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        tag.put("frequencyItems", storedGhostInv.serializeNBT());
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        storedGhostInv.deserializeNBT(tag.getCompound("frequencyItems"));
        link.setFrequencyKeysOnLoad(storedGhostInv);
    }

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
        level.setBlock(pos, blockEntity.getBlockState().setValue(KeyboardRelayBlock.TRANSMITTING, on), 3); //  turn power on/off
    }
}
