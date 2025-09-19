package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.midi.PitchMapping;
import com.finchy.pipeorgans.util.MathUtils;
import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.IntAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class RedstoneMidiLink {

    public final List<Frequency> FrequencyKeys;
    // list of channels, each channel being a map of active pitches and the corresponding ManualNoteFrequency
    private final ArrayList<Map<Integer, ManualNoteFrequency>> activeNotes;
    static final int TIMEOUT = 2;
    private final Level world;
    private final BlockPos pos;

    public RedstoneMidiLink(Level world, BlockPos pos) {
        FrequencyKeys = new ArrayList<>(Collections.nCopies(16, Frequency.of(ItemStack.EMPTY)));
        // set channel 10 to a different frequency by default, as 10 is usually the percussion channel
        FrequencyKeys.set(9, Frequency.of(new ItemStack(Items.STICK)));
        activeNotes = new ArrayList<>();
        for (int i=0; i<16; i++) {
            activeNotes.add(new HashMap<>());
        }
        this.world = world;
        this.pos = pos;
    }

    public static class ManualNoteFrequency extends IntAttached<Couple<Frequency>> implements IRedstoneLinkable {

        private final BlockPos pos;
        private final int strength;

        public ManualNoteFrequency(BlockPos pos, Couple<Frequency> second, int velocity) {
            super(TIMEOUT, second);
            this.pos = pos;
            this.strength = velocity;
        }

        public static ManualNoteFrequency create(BlockPos pos, Frequency key, Frequency pitch, int velocity) {
            return new ManualNoteFrequency(pos, Couple.create(key, pitch), velocity);
        }

        public static int midiVelocityToRedstone(int velocity) {
            return Math.round(MathUtils.map(velocity, 0, 127, 0, 15));
        }

        @Override
        public int getTransmittedStrength() {
            return isAlive() ? strength : 0;
        }

        @Override
        public boolean isAlive() {
            return getFirst() > 0;
        }

        @Override
        public BlockPos getLocation() {
            return pos;
        }

        @Override
        public void setReceivedStrength(int power) {
        }

        @Override
        public boolean isListening() {
            return false;
        }

        @Override
        public Couple<Frequency> getNetworkKey() {
            return getSecond();
        }

    }

    public void setFrequencyKeysOnLoad(ItemStackHandler ghostInv) {
        for (int i = 0; i < 16; i++) {
            FrequencyKeys.set(i, Frequency.of(ghostInv.getStackInSlot(i)));
        }
    }

    public void changeFrequencyKey(int channel, ItemStack newKey) {
        for (Map.Entry<Integer, ManualNoteFrequency> entry : activeNotes.get(channel).entrySet()) {
            ManualNoteFrequency oldNote = entry.getValue();

            if (oldNote.getSecond().getFirst().getStack().equals(newKey)) // if the new key is the same as the existing key
                return;
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, oldNote);

            Frequency pitchFreq = oldNote.getSecond().getSecond();
            ManualNoteFrequency newNoteFrequency = ManualNoteFrequency.create(oldNote.pos, Frequency.of(newKey), pitchFreq, oldNote.strength);
            entry.setValue(newNoteFrequency);
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, newNoteFrequency);
        }
        FrequencyKeys.set(channel, Frequency.of(newKey));

    }

    public void stopAllNotes() {
        for (Map<Integer, ManualNoteFrequency> entry : activeNotes) {
            for (Iterator<Map.Entry<Integer, ManualNoteFrequency>> noteIterator = entry.entrySet().iterator(); noteIterator.hasNext(); ) {
                Map.Entry<Integer, ManualNoteFrequency> noteEntry = noteIterator.next();
                noteIterator.remove();
                Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, noteEntry.getValue());
            }
        }
    }

    public ManualNoteFrequency noteFrequency(int channel, int pitch, int velocity) {
        Frequency keyFreq = FrequencyKeys.get(channel);
        Frequency pitchFreq = Frequency.of(PitchMapping.getStack(pitch));
        return ManualNoteFrequency.create(pos, keyFreq, pitchFreq, ManualNoteFrequency.midiVelocityToRedstone(velocity));
    }

    public void activateNote(int channel, int pitch, int velocity) {
        ManualNoteFrequency noteFrequency = noteFrequency(channel, pitch, velocity);
        Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, noteFrequency);
        activeNotes.get(channel).put(pitch, noteFrequency);
    }

    public void deactivateNote(int channel, int pitch) {
        Map<Integer, ManualNoteFrequency> channelNotes = activeNotes.get(channel);
        if (channelNotes.containsKey(pitch)) {
            ManualNoteFrequency noteFrequency = channelNotes.get(pitch);
            channelNotes.remove(pitch);
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, noteFrequency);
        }
    }

    public boolean areNotesActive() {
        return !activeNotes.isEmpty();
    }

}
