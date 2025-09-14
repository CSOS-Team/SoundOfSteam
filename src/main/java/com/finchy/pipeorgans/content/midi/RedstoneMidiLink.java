package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.midi.PitchMapping;
import com.finchy.pipeorgans.util.MathUtils;
import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.IntAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class RedstoneMidiLink {

    public final List<Frequency> FrequencyKeys;
    private final ArrayList<Map<Integer, ManualNoteFrequency>> activeNotes;
    static final int TIMEOUT = 2;
    private final Level world;
    private final BlockPos pos;

    public RedstoneMidiLink(Level world, BlockPos pos) {
        FrequencyKeys = new ArrayList<>(Collections.nCopies(16, Frequency.of(ItemStack.EMPTY)));
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
            return new ManualNoteFrequency(pos, Couple.create(key, pitch), Math.round(MathUtils.map(velocity, 0, 127, 0, 15)));
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

    public ManualNoteFrequency noteFrequency(int channel, int pitch, int velocity) {
        Frequency keyFreq = FrequencyKeys.get(channel);
        Frequency pitchFreq = Frequency.of(PitchMapping.getStack(pitch));
        return ManualNoteFrequency.create(pos, keyFreq, pitchFreq, velocity);
    }

    public void activateNote(int channel, int pitch, int velocity) {
        ManualNoteFrequency noteFrequency = noteFrequency(channel, pitch, velocity);
        Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, noteFrequency);
        activeNotes.get(channel).put(pitch, noteFrequency);
        PipeOrgans.LOGGER.info("ADDED TO NETWORK");
    }

    public void deactivateNote(int channel, int pitch) {
        Map<Integer, ManualNoteFrequency> channelNotes = activeNotes.get(channel);
        if (channelNotes.containsKey(pitch)) {
            ManualNoteFrequency noteFrequency = channelNotes.get(pitch);
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, noteFrequency);
        }
    }

    public boolean areNotesActive() {
        return !activeNotes.isEmpty();
    }

}
