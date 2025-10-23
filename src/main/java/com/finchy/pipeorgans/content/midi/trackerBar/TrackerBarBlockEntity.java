package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.content.midi.MidiSequencerBehaviour;
import com.finchy.pipeorgans.content.midi.MidiSourceBehaviour;
import com.finchy.pipeorgans.util.MidiLoadException;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.*;
import java.util.List;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public class TrackerBarBlockEntity extends KineticBlockEntity implements MenuProvider {

    protected LazyOptional<IItemHandler> itemCapability;

    private boolean buttonsEnabled = false;

    public TrackerBarInventory inventory;
    protected final ContainerData data;

    public float rollerAngle = 0f;
    public static final float MAX_ROLLER_VELOCITY = 21.666f;
    public static final float SCROLL_SPEED = 1/32f;

    MidiSourceBehaviour midiSourceBehaviour;
    MidiSequencerBehaviour midiSequencerBehaviour;

    public static class TrackerBarInventory extends ItemStackHandler {
        private final TrackerBarBlockEntity be;

        public TrackerBarInventory(TrackerBarBlockEntity be) {
            super(1);
            this.be = be;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            be.setChanged();
        }
    }

    public TrackerBarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        inventory = new TrackerBarInventory(this);
        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(0);
                    case 1 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(1);
                    case 2 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(2);
                    case 3 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(3);
                    case 4 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(4);
                    case 5 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(5);
                    case 6 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(6);
                    case 7 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(7);
                    case 8 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(8);
                    case 9 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(9);
                    case 10 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(10);
                    case 11 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(11);
                    case 12 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(12);
                    case 13 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(13);
                    case 14 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(14);
                    case 15 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.channelInstruments.get(15);

                    case 16 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.isPlaying() ? 1 : 0;
                    case 17 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.getTickPosition();
                    case 18 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.getEndTick();
                    case 19 -> TrackerBarBlockEntity.this.midiSequencerBehaviour.get10xBPM();
                    case 20 -> TrackerBarBlockEntity.this.buttonsEnabled ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                // nuh uh
            }

            @Override
            public int getCount() {
                return 21;
            }
        };
        itemCapability = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(midiSourceBehaviour = new MidiSourceBehaviour(this));
        behaviours.add(midiSequencerBehaviour = new MidiSequencerBehaviour(this));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (isItemHandlerCap(cap))
            return itemCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemCapability.invalidate();
    }

    public void onBlockRemoved() {
        midiSourceBehaviour.link.stopAllNotes();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.tracker_bar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return TrackerBarMenu.create(pContainerId, pPlayerInventory, this, data);
    }

    @Override
    public void tick() {
        super.tick();
        if (midiSequencerBehaviour.isPlaying() && speed != 0) {
            midiSequencerBehaviour.tickSequencer();
            rollerAngle += MAX_ROLLER_VELOCITY;
        }
    }

    public void setButtonsEnabled(boolean enabled) {
        buttonsEnabled = enabled;
    }

    public float getRollerAngle(float partialTicks) {
        return midiSequencerBehaviour.isPlaying() ? (rollerAngle + MAX_ROLLER_VELOCITY*partialTicks)/360 : rollerAngle;
    }

    public float getScrollSpeed() {
        return (midiSequencerBehaviour.isPlaying() && speed != 0) ? SCROLL_SPEED : 0;
    }

    public void onRollChanged() {
        ItemStack stack = inventory.getStackInSlot(0);
        midiSequencerBehaviour.unloadSequence();
        if (stack.isEmpty()) {
            buttonsEnabled = false;
        } else if (MidiUtils.isMusicRollValid(stack)) {
            try {
                CompoundTag tag = stack.getTag();
                midiSequencerBehaviour.loadSequence(tag.getString("File"), tag.getString("Owner"));
                buttonsEnabled = true;
            } catch (MidiLoadException e) {
                buttonsEnabled = false;
            }
        } else {
            buttonsEnabled = false;
        }
        setChanged();
    }

    public boolean getButtonsEnabled() {
        return buttonsEnabled;
    }

    public void pressTogglePlayButton() {
        if (midiSequencerBehaviour.isSequenceLoaded()) {
            midiSequencerBehaviour.toggleSequencer();
        }
    }

    public void pressStopButton() {
        midiSequencerBehaviour.restartPlayback();
    }

    public void handleNote(ShortMessage sm) {
        midiSourceBehaviour.handleNote(sm);
    }

    public void stopAllNotes() {
        midiSourceBehaviour.link.stopAllNotes();
    }

}
