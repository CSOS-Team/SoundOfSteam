package com.finchy.pipeorgans.content.console;

import com.finchy.pipeorgans.content.midi.RedstoneMidiTransmitter;
import com.finchy.pipeorgans.midi.PitchMapping;
import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public class OrganConsoleBlockEntity extends SmartBlockEntity implements MenuProvider {

    public static final int MAX_MANUALS = 4;
    public static final int PEDAL_SECTION = 4;

    public static final int MANUAL_KEY_COUNT = 61;
    public static final int PEDAL_KEY_COUNT = 32;

    // Lowest MIDI pitch of a manual & pedalboard (C2)
    public static final int MANUAL_LOW_PITCH = 36;
    public static final int PEDAL_LOW_PITCH = 36;

    // Inclusive MIDI pitch range covered by the receive listeners
    public static final int RECEIVE_LOW_PITCH = 36;
    public static final int RECEIVE_HIGH_PITCH = 96;

    // Ghost inventory layout
    public static final int FILTER_SLOTS = 16; // sized 16 so RedstoneMidiTransmitter#setFrequencyKeysOnLoad is happy
    public static final int INPUT_SLOT_BASE = 8;

    public static int outputSlot(int section) {
        return section;
    }

    public static int inputSlot(int section) {
        return INPUT_SLOT_BASE + section;
    }

    // Persistent configuration
    private int manualCount = 1;
    private boolean hasPedalboard = false;

    // Frequency (key) filters. Slots 0-3 = manuals, 4 = pedalboard, 5 = receive filter
    public final ItemStackHandler filterInventory;

    // Output transmitter (channels 0-4)
    private final RedstoneMidiTransmitter link;

    // Runtime (server-side) state
    private final int[][] receivedPower = new int[PEDAL_SECTION + 1][RECEIVE_HIGH_PITCH - RECEIVE_LOW_PITCH + 1];
    private final List<ConsoleNoteReceiver> receivers = new ArrayList<>();

    private final Map<Integer, Set<Integer>> receivedEmitted = new HashMap<>();

    private final Map<UUID, Set<Long>> guiPlayerNotes = new HashMap<>();

    private final long[] receivedBits = new long[PEDAL_SECTION + 1];

    public boolean menuPedalboardMode = false;
    public int menuManualCount = 1;

    public OrganConsoleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        filterInventory = new ItemStackHandler(FILTER_SLOTS) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return true;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
                if (level == null || level.isClientSide)
                    return;
                if (slot <= PEDAL_SECTION) {                                  // output (emit) filter
                    link.changeFrequencyKey(slot, getStackInSlot(slot));
                } else if (slot >= INPUT_SLOT_BASE && slot <= INPUT_SLOT_BASE + PEDAL_SECTION) { // input (receive) filter
                    rebuildReceivers();
                }
            }
        };

        link = new RedstoneMidiTransmitter(this);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new ConsoleNetworkBehaviour(this));
    }

    // Configuration accessors and mutators
    public int getManualCount() {
        return manualCount;
    }

    public boolean hasPedalboard() {
        return hasPedalboard;
    }

    public ItemStackHandler getFilterInventory() {
        return filterInventory;
    }

    // Client helper
    public boolean isReceivedPitch(int section, int pitch) {
        if (section < 0 || section > PEDAL_SECTION)
            return false;
        if (pitch < RECEIVE_LOW_PITCH || pitch > RECEIVE_HIGH_PITCH)
            return false;
        return (receivedBits[section] & (1L << (pitch - RECEIVE_LOW_PITCH))) != 0L;
    }

    public void cycleManualCount() {
        manualCount = (manualCount % MAX_MANUALS) + 1; // 1 -> 2 -> 3 -> 4 -> 1
        if (level != null && !level.isClientSide) {
            releaseSectionsAboveManualCount();
            rebuildReceivers();
        }
        notifyUpdate();
    }

    public void togglePedalboard() {
        hasPedalboard = !hasPedalboard;
        if (level != null && !level.isClientSide) {
            if (!hasPedalboard)
                releaseSection(PEDAL_SECTION);
            rebuildReceivers();
        }
        notifyUpdate();
    }

    // True if a given section index is currently active in this console's configuration
    public boolean isSectionConfigured(int section) {
        if (section == PEDAL_SECTION)
            return hasPedalboard;
        return section >= 0 && section < manualCount;
    }

    public static int keyCount(int section) {
        return section == PEDAL_SECTION ? PEDAL_KEY_COUNT : MANUAL_KEY_COUNT;
    }

    public static int sectionLowPitch(int section) {
        return section == PEDAL_SECTION ? PEDAL_LOW_PITCH : MANUAL_LOW_PITCH;
    }

    // Maps a (section, key) pair to a MIDI pitch
    public static int pitchFor(int section, int key) {
        return sectionLowPitch(section) + key;
    }

    // GUI opening
    public void openMenu(ServerPlayer player, boolean pedalboardMode) {
        menuPedalboardMode = pedalboardMode;
        menuManualCount = manualCount;
        NetworkHooks.openScreen(player, this, buffer -> {
            sendToMenu(buffer);
            buffer.writeBoolean(pedalboardMode);
            buffer.writeVarInt(manualCount);
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(menuPedalboardMode
                ? "gui.pipeorgans.organ_console.pedalboard"
                : "gui.pipeorgans.organ_console.manuals");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return OrganConsoleMenu.create(containerId, playerInventory, this);
    }

    // Note handling from the GUI (server-side)
    private static long encode(int section, int key) {
        return (((long) section) << 32) | (key & 0xFFFFFFFFL);
    }

    // When a player presses or releases a key in the GUI
    public boolean handleGuiNote(Player player, int section, int key, boolean on, int velocity) {
        if (level == null || level.isClientSide)
            return false;
        if (!isSectionConfigured(section))
            return false;
        if (key < 0 || key >= keyCount(section))
            return false;

        int pitch = pitchFor(section, key);
        Set<Long> playerNotes = guiPlayerNotes.computeIfAbsent(player.getUUID(), u -> new HashSet<>());
        long code = encode(section, key);

        if (on) {
            if (playerNotes.add(code))
                link.activateNote(section, pitch, velocity);
        } else {
            if (playerNotes.remove(code))
                link.deactivateNote(section, pitch);
        }
        return true;
    }

    private void releaseSection(int section) {
        // Drop any GUI notes that target this section
        for (Iterator<Map.Entry<UUID, Set<Long>>> it = guiPlayerNotes.entrySet().iterator(); it.hasNext(); ) {
            Set<Long> notes = it.next().getValue();
            notes.removeIf(code -> {
                int s = (int) (code >> 32);
                if (s != section)
                    return false;
                int key = (int) (code & 0xFFFFFFFFL);
                link.deactivateNote(section, pitchFor(section, key));
                return true;
            });
            if (notes.isEmpty())
                it.remove();
        }
        // Drop any received notes re-emitted on this section
        Set<Integer> emitted = receivedEmitted.remove(section);
        if (emitted != null)
            for (int pitch : emitted)
                link.deactivateNote(section, pitch);
    }

    private void releaseSectionsAboveManualCount() {
        for (int section = manualCount; section < MAX_MANUALS; section++)
            releaseSection(section);
    }

    private void releasePlayerNotes(UUID uuid) {
        Set<Long> notes = guiPlayerNotes.remove(uuid);
        if (notes == null)
            return;
        for (long code : notes) {
            int section = (int) (code >> 32);
            int key = (int) (code & 0xFFFFFFFFL);
            link.deactivateNote(section, pitchFor(section, key));
        }
    }

    // Receive feature
    private void rebuildReceivers() {
        if (level == null || level.isClientSide)
            return;
        removeReceivers();

        // Clear any notes that were re-emitted from received signals
        for (Map.Entry<Integer, Set<Integer>> entry : receivedEmitted.entrySet())
            for (int pitch : entry.getValue())
                link.deactivateNote(entry.getKey(), pitch);
        receivedEmitted.clear();
        for (int[] row : receivedPower)
            java.util.Arrays.fill(row, 0);

        for (int section = 0; section <= PEDAL_SECTION; section++) {
            ItemStack inputStack = filterInventory.getStackInSlot(inputSlot(section));
            if (inputStack.isEmpty())
                continue;
            Frequency inputFreq = Frequency.of(inputStack);
            int low = sectionLowPitch(section);
            int high = low + keyCount(section) - 1;
            for (int pitch = low; pitch <= high; pitch++) {
                Frequency pitchFreq = Frequency.of(PitchMapping.getStack(pitch));
                ConsoleNoteReceiver receiver = new ConsoleNoteReceiver(section, pitch, Couple.create(inputFreq, pitchFreq));
                receivers.add(receiver);
                Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, receiver);
            }
        }
    }

    private void removeReceivers() {
        if (level == null)
            return;
        for (ConsoleNoteReceiver receiver : receivers)
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, receiver);
        receivers.clear();
    }

    // Re-emit each section's received notes onto that section's own output filter
    private void reconcileReceivedNotes() {
        for (int section = 0; section <= PEDAL_SECTION; section++) {
            Set<Integer> emitted = receivedEmitted.computeIfAbsent(section, s -> new HashSet<>());

            ItemStack outStack = filterInventory.getStackInSlot(outputSlot(section));
            ItemStack inStack = filterInventory.getStackInSlot(inputSlot(section));
            // Don't echo onto an empty output filter, and never echo onto the same frequency listening on
            boolean canEmit = isSectionConfigured(section)
                    && !outStack.isEmpty()
                    && !ItemStack.isSameItemSameTags(outStack, inStack);

            int low = sectionLowPitch(section);
            int high = low + keyCount(section) - 1;
            int[] power = receivedPower[section];

            for (int pitch = low; pitch <= high; pitch++) {
                boolean shouldBeOn = canEmit && power[pitch - RECEIVE_LOW_PITCH] > 0;
                boolean isOn = emitted.contains(pitch);
                if (shouldBeOn && !isOn) {
                    link.activateNote(section, pitch, 127);
                    emitted.add(pitch);
                } else if (!shouldBeOn && isOn) {
                    link.deactivateNote(section, pitch);
                    emitted.remove(pitch);
                }
            }
        }
    }

    // Listen for a single (section, pitch) on that section input frequency
    private class ConsoleNoteReceiver implements IRedstoneLinkable {
        private final int section;
        private final int pitch;
        private final Couple<Frequency> networkKey;

        ConsoleNoteReceiver(int section, int pitch, Couple<Frequency> networkKey) {
            this.section = section;
            this.pitch = pitch;
            this.networkKey = networkKey;
        }

        @Override
        public int getTransmittedStrength() {
            return 0;
        }

        @Override
        public void setReceivedStrength(int power) {
            receivedPower[section][pitch - RECEIVE_LOW_PITCH] = power;
        }

        @Override
        public boolean isListening() {
            return true;
        }

        @Override
        public boolean isAlive() {
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == OrganConsoleBlockEntity.this;
        }

        @Override
        public BlockPos getLocation() {
            return worldPosition;
        }

        @Override
        public Couple<Frequency> getNetworkKey() {
            return networkKey;
        }
    }

    // Ticking
    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide)
            return;

        cleanupStaleGuiNotes();
        reconcileReceivedNotes();
        syncReceivedBits();
    }

    // Recompute per-section received bitmask and pushes a client update when it changes
    private void syncReceivedBits() {
        boolean changed = false;
        for (int section = 0; section <= PEDAL_SECTION; section++) {
            long bits = 0L;
            int[] power = receivedPower[section];
            for (int i = 0; i < power.length; i++)
                if (power[i] > 0)
                    bits |= 1L << i;
            if (bits != receivedBits[section]) {
                receivedBits[section] = bits;
                changed = true;
            }
        }
        if (changed)
            notifyUpdate();
    }

    // Release notes held by players that are no longer using console GUI
    private void cleanupStaleGuiNotes() {
        if (guiPlayerNotes.isEmpty() || !(level instanceof ServerLevel serverLevel))
            return;

        double reachSq = 0;
        List<UUID> toRelease = new ArrayList<>();
        for (UUID uuid : guiPlayerNotes.keySet()) {
            Player player = serverLevel.getPlayerByUUID(uuid);
            boolean usingThis = false;
            if (player != null && player.containerMenu instanceof OrganConsoleMenu menu
                    && menu.getConsole() == this) {
                if (reachSq == 0)
                    reachSq = Math.pow(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()), 2) + 16;
                usingThis = player.distanceToSqr(Vec3.atCenterOf(worldPosition)) < reachSq;
            }
            if (!usingThis)
                toRelease.add(uuid);
        }
        for (UUID uuid : toRelease)
            releasePlayerNotes(uuid);
    }

    // Lifecycle and persistence

    public static final BehaviourType<ConsoleNetworkBehaviour> NETWORK_BEHAVIOUR = new BehaviourType<>();

    // Redstone-link network lifecycle
    private class ConsoleNetworkBehaviour extends BlockEntityBehaviour {
        ConsoleNetworkBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public BehaviourType<?> getType() {
            return NETWORK_BEHAVIOUR;
        }

        @Override
        public void initialize() {
            super.initialize();
            if (level == null || level.isClientSide)
                return;
            link.setFrequencyKeysOnLoad(filterInventory);
            rebuildReceivers();
        }

        @Override
        public void unload() {
            super.unload();
            if (level == null || level.isClientSide)
                return;
            removeReceivers();
            link.stopAllNotes();
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("ManualCount", manualCount);
        tag.putBoolean("HasPedalboard", hasPedalboard);
        tag.put("Filters", filterInventory.serializeNBT());
        if (clientPacket)
            tag.putLongArray("ReceivedBits", receivedBits.clone());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        manualCount = Math.max(1, Math.min(MAX_MANUALS, tag.getInt("ManualCount")));
        hasPedalboard = tag.getBoolean("HasPedalboard");
        if (tag.contains("Filters"))
            filterInventory.deserializeNBT(tag.getCompound("Filters"));
        if (clientPacket) {
            long[] bits = tag.getLongArray("ReceivedBits");
            for (int i = 0; i < receivedBits.length; i++)
                receivedBits[i] = i < bits.length ? bits[i] : 0L;
        }

        if (level != null && !level.isClientSide) {
            link.setFrequencyKeysOnLoad(filterInventory);
        }
    }
}
