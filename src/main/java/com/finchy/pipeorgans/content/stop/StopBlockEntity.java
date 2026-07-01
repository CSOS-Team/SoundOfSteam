package com.finchy.pipeorgans.content.stop;

import com.finchy.pipeorgans.content.piston.ClearSignal;
import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public class StopBlockEntity extends SmartBlockEntity implements MenuProvider, com.finchy.pipeorgans.content.piston.CombinationTarget {

    public static final int MAX_STOPS = 18;
    public static final int TRANSMIT_STRENGTH = 15;

    // Division filter is second frequency of every emitted signal
    public final ItemStackHandler divisionInv;

    private final List<Stop> stops = new ArrayList<>();

    // Server-side live transmitters for currently-pressed stops
    private final Map<Stop, StopTransmitter> transmitters = new IdentityHashMap<>();

    public boolean menuEditMode = false;
    public int menuEditIndex = -1;

    // Clear-signal receiver state released from redstone link broadcast
    private ClearReceiver clearReceiver;
    private int clearSignalPower = 0;
    private boolean clearedThisPulse = false;

    private boolean tuttiOverride = false;

    public StopBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        divisionInv = new ItemStackHandler(1) {
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
                if (level != null && !level.isClientSide)
                    refreshTransmitters(); // division frequency changed
            }
        };

        for (int i = 0; i < MAX_STOPS; i++)
            stops.add(new Stop());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new StopNetworkBehaviour(this));
    }

    // Accessors

    public List<Stop> getStops() {
        return stops;
    }

    public ItemStackHandler getDivisionInv() {
        return divisionInv;
    }

    @Nullable
    public Stop getStop(int index) {
        return index >= 0 && index < stops.size() ? stops.get(index) : null;
    }

    private Frequency divisionFrequency() {
        return Frequency.of(divisionInv.getStackInSlot(0));
    }

    // Mutations (server-side)

    public void editStop(int index, String name, String descriptor, ItemStack filter) {
        if (level == null || level.isClientSide)
            return;
        Stop stop = getStop(index);
        if (stop == null)
            return;
        stop.name = name == null ? "" : name;
        stop.descriptor = descriptor == null ? "" : descriptor;
        stop.filter = filter == null ? ItemStack.EMPTY : filter.copy();
        if (!stop.filter.isEmpty())
            stop.filter.setCount(1);
        if (stop.isUnused())
            stop.pressed = false; // clearing all fields makes it unused
        registerOrUpdate(stop);
        notifyUpdate();
    }

    /** Clears a stop's fields, making it "unused". */
    public void clearStop(int index) {
        editStop(index, "", "", ItemStack.EMPTY);
    }

    // Reorders a stop
    public void moveStop(int from, int to) {
        if (level == null || level.isClientSide)
            return;
        if (from < 0 || from >= stops.size() || to < 0 || to >= stops.size() || from == to)
            return;
        Stop moved = stops.remove(from);
        stops.add(to, moved); // transmitters are keyed by Stop identity, so they stay valid
        notifyUpdate();
    }

    public void togglePress(int index) {
        if (level == null || level.isClientSide)
            return;
        Stop stop = getStop(index);
        if (stop == null || stop.isUnused()) // unused stops don't latch
            return;
        stop.pressed = !stop.pressed;
        registerOrUpdate(stop);
        notifyUpdate();
    }

    public void clearAllPressed() {
        if (level == null || level.isClientSide)
            return;
        boolean any = false;
        for (Stop stop : stops) {
            if (stop.pressed) {
                stop.pressed = false;
                registerOrUpdate(stop);
                any = true;
            }
        }
        if (any)
            notifyUpdate();
    }

    // CombinationTarget (for Piston presets)

    @Override
    public int combinationSize() {
        return stops.size();
    }

    @Override
    public boolean isEntryPressed(int index) {
        Stop stop = getStop(index);
        return stop != null && stop.pressed;
    }

    @Override
    public void setEntryPressed(int index, boolean pressed) {
        if (level == null || level.isClientSide)
            return;
        Stop stop = getStop(index);
        if (stop == null || stop.isUnused()) // unused stops can't be pressed
            return;
        if (stop.pressed == pressed)
            return;
        stop.pressed = pressed;
        registerOrUpdate(stop);
        notifyUpdate();
    }

    @Override
    public void setAllEntriesPressed(boolean pressed) {
        if (level == null || level.isClientSide)
            return;
        boolean any = false;
        for (Stop stop : stops) {
            if (stop.pressed != pressed) {
                stop.pressed = pressed;
                registerOrUpdate(stop);
                any = true;
            }
        }
        if (any)
            notifyUpdate();
    }

    @Override
    public void setTuttiOverride(boolean active) {
        if (level == null || level.isClientSide || tuttiOverride == active)
            return;
        tuttiOverride = active;
        for (Stop stop : stops)
            registerOrUpdate(stop); // emits all while active; stored pressed states are untouched
    }

    // Redstone-link transmitters

    private void registerOrUpdate(Stop stop) {
        if (level == null || level.isClientSide)
            return;
        removeTransmitter(stop);
        if (stop.isUnused())
            return; // unused stops never emit
        if (stop.pressed || tuttiOverride) {
            Couple<Frequency> key = Couple.create(Frequency.of(stop.filter), divisionFrequency());
            StopTransmitter transmitter = new StopTransmitter(key);
            transmitters.put(stop, transmitter);
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, transmitter);
        }
    }

    private void removeTransmitter(Stop stop) {
        StopTransmitter transmitter = transmitters.remove(stop);
        if (transmitter != null && level != null)
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, transmitter);
    }

    private void refreshTransmitters() {
        if (level == null || level.isClientSide)
            return;
        for (Stop stop : stops)
            registerOrUpdate(stop);
    }

    private void removeAllTransmitters() {
        if (level == null)
            return;
        for (StopTransmitter transmitter : transmitters.values())
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, transmitter);
        transmitters.clear();
    }

    private class StopTransmitter implements IRedstoneLinkable {
        private final Couple<Frequency> networkKey;

        StopTransmitter(Couple<Frequency> networkKey) {
            this.networkKey = networkKey;
        }

        @Override
        public int getTransmittedStrength() {
            return isAlive() ? TRANSMIT_STRENGTH : 0;
        }

        @Override
        public void setReceivedStrength(int power) {
        }

        @Override
        public boolean isListening() {
            return false;
        }

        @Override
        public boolean isAlive() {
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == StopBlockEntity.this;
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

    // GUI

    public void openMainMenu(ServerPlayer player) {
        menuEditMode = false;
        menuEditIndex = -1;
        NetworkHooks.openScreen(player, this, buffer -> {
            sendToMenu(buffer);
            buffer.writeBoolean(false);
            buffer.writeVarInt(-1);
        });
    }

    public void openEditMenu(ServerPlayer player, int index) {
        if (getStop(index) == null)
            return;
        menuEditMode = true;
        menuEditIndex = index;
        NetworkHooks.openScreen(player, this, buffer -> {
            sendToMenu(buffer);
            buffer.writeBoolean(true);
            buffer.writeVarInt(index);
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(menuEditMode
                ? "gui.pipeorgans.stop.edit"
                : "gui.pipeorgans.stop.main");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        if (menuEditMode)
            return StopEditMenu.create(containerId, playerInventory, this, menuEditIndex);
        return StopMenu.create(containerId, playerInventory, this);
    }

    // Ticking

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide)
            return;
        if (clearSignalPower > 0) {
            if (!clearedThisPulse) {
                clearAllPressed();
                clearedThisPulse = true;
            }
        } else {
            clearedThisPulse = false;
        }
    }

    private class ClearReceiver implements IRedstoneLinkable {
        private final Couple<Frequency> key = ClearSignal.networkKey();

        @Override
        public int getTransmittedStrength() {
            return 0;
        }

        @Override
        public void setReceivedStrength(int power) {
            clearSignalPower = power;
        }

        @Override
        public boolean isListening() {
            return true;
        }

        @Override
        public boolean isAlive() {
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == StopBlockEntity.this;
        }

        @Override
        public BlockPos getLocation() {
            return worldPosition;
        }

        @Override
        public Couple<Frequency> getNetworkKey() {
            return key;
        }
    }

    // Lifecycle and persistence

    public static final BehaviourType<StopNetworkBehaviour> NETWORK_BEHAVIOUR = new BehaviourType<>();

    private class StopNetworkBehaviour extends BlockEntityBehaviour {
        StopNetworkBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public BehaviourType<?> getType() {
            return NETWORK_BEHAVIOUR;
        }

        @Override
        public void initialize() {
            super.initialize();
            refreshTransmitters();
            clearReceiver = new ClearReceiver();
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, clearReceiver);
        }

        @Override
        public void unload() {
            super.unload();
            removeAllTransmitters();
            if (clearReceiver != null) {
                Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, clearReceiver);
                clearReceiver = null;
            }
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Division", divisionInv.serializeNBT());
        ListTag list = new ListTag();
        for (Stop stop : stops)
            list.add(stop.toNbt());
        tag.put("Stops", list);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (tag.contains("Division"))
            divisionInv.deserializeNBT(tag.getCompound("Division"));
        stops.clear();
        ListTag list = tag.getList("Stops", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size() && i < MAX_STOPS; i++)
            stops.add(Stop.fromNbt(list.getCompound(i)));
        while (stops.size() < MAX_STOPS)
            stops.add(new Stop()); // always keep the full fixed set of slots

        if (level != null && !level.isClientSide) {
            removeAllTransmitters();
            // If the behaviour is already initialized (mid-session update, not initial load),
            // re-register transmitters immediately so pressed stops keep emitting.
            if (getBehaviour(NETWORK_BEHAVIOUR) != null)
                refreshTransmitters();
        }
    }
}
