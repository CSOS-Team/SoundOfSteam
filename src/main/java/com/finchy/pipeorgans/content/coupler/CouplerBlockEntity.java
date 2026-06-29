package com.finchy.pipeorgans.content.coupler;

import com.finchy.pipeorgans.content.piston.ClearSignal;
import com.finchy.pipeorgans.content.piston.CombinationTarget;
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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public class CouplerBlockEntity extends SmartBlockEntity implements MenuProvider, CombinationTarget {

    public static final int MAX_COUPLERS = 12;
    public static final int LOW_PITCH = 36;
    public static final int HIGH_PITCH = 96;

    // Prevent circular couplers from looping
    private static final java.util.Set<Object> propagating =
            java.util.Collections.newSetFromMap(new IdentityHashMap<>());

    private final List<Coupler> couplers = new ArrayList<>();
    private final Map<Coupler, List<IRedstoneLinkable>> activeLinks = new IdentityHashMap<>();

    public boolean menuEditMode = false;
    public int menuEditIndex = -1;

    private ClearReceiver clearReceiver;
    private int clearSignalPower = 0;
    private boolean clearedThisPulse = false;

    // Non-destructive tutti, does not change stored pressed states
    private boolean tuttiOverride = false;

    public CouplerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new CouplerNetworkBehaviour(this));
    }

    // Accessors

    public List<Coupler> getCouplers() {
        return couplers;
    }

    @Nullable
    public Coupler getCoupler(int index) {
        return index >= 0 && index < couplers.size() ? couplers.get(index) : null;
    }

    // Mutations (server-side)

    public void addCoupler() {
        if (level == null || level.isClientSide || couplers.size() >= MAX_COUPLERS)
            return;
        couplers.add(new Coupler("Coupler " + (couplers.size() + 1),
                net.minecraft.world.item.ItemStack.EMPTY, net.minecraft.world.item.ItemStack.EMPTY));
        notifyUpdate();
    }

    public void deleteCoupler(int index) {
        if (level == null || level.isClientSide)
            return;
        Coupler coupler = getCoupler(index);
        if (coupler == null)
            return;
        removeCoupler(coupler);
        couplers.remove(index);
        notifyUpdate();
    }

    public void editCoupler(int index, String name, net.minecraft.world.item.ItemStack divisionA, net.minecraft.world.item.ItemStack divisionB) {
        if (level == null || level.isClientSide)
            return;
        Coupler coupler = getCoupler(index);
        if (coupler == null)
            return;
        coupler.name = name == null ? "" : name;
        coupler.divisionA = normalize(divisionA);
        coupler.divisionB = normalize(divisionB);
        registerCoupler(coupler);
        notifyUpdate();
    }

    private static net.minecraft.world.item.ItemStack normalize(net.minecraft.world.item.ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return net.minecraft.world.item.ItemStack.EMPTY;
        net.minecraft.world.item.ItemStack copy = stack.copy();
        copy.setCount(1);
        return copy;
    }

    public void togglePress(int index) {
        if (level == null || level.isClientSide)
            return;
        Coupler coupler = getCoupler(index);
        if (coupler == null)
            return;
        coupler.pressed = !coupler.pressed;
        registerCoupler(coupler);
        notifyUpdate();
    }

    public void clearAllPressed() {
        if (level == null || level.isClientSide)
            return;
        boolean any = false;
        for (Coupler coupler : couplers) {
            if (coupler.pressed) {
                coupler.pressed = false;
                registerCoupler(coupler);
                any = true;
            }
        }
        if (any)
            notifyUpdate();
    }

    @Override
    public int combinationSize() {
        return couplers.size();
    }

    @Override
    public boolean isEntryPressed(int index) {
        Coupler coupler = getCoupler(index);
        return coupler != null && coupler.pressed;
    }

    @Override
    public void setEntryPressed(int index, boolean pressed) {
        if (level == null || level.isClientSide)
            return;
        Coupler coupler = getCoupler(index);
        if (coupler == null || coupler.pressed == pressed)
            return;
        coupler.pressed = pressed;
        registerCoupler(coupler);
        notifyUpdate();
    }

    @Override
    public void setAllEntriesPressed(boolean pressed) {
        if (level == null || level.isClientSide)
            return;
        boolean any = false;
        for (Coupler coupler : couplers) {
            if (coupler.pressed != pressed) {
                coupler.pressed = pressed;
                registerCoupler(coupler);
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
        refreshAll(); // emits all while active, stored pressed states are untouched
    }

    // Coupling links (zero tick delay, receiver pushes the transmitter immediately)

    private void registerCoupler(Coupler coupler) {
        if (level == null || level.isClientSide)
            return;
        removeCoupler(coupler);
        if ((!coupler.pressed && !tuttiOverride) || coupler.divisionA.isEmpty() || coupler.divisionB.isEmpty())
            return;

        Frequency aFreq = Frequency.of(coupler.divisionA);
        Frequency bFreq = Frequency.of(coupler.divisionB);
        List<IRedstoneLinkable> links = new ArrayList<>();
        for (int pitch = LOW_PITCH; pitch <= HIGH_PITCH; pitch++) {
            Frequency pitchFreq = Frequency.of(PitchMapping.getStack(pitch));
            CoupleTransmitter transmitter = new CoupleTransmitter(Couple.create(bFreq, pitchFreq));
            CoupleReceiver receiver = new CoupleReceiver(Couple.create(aFreq, pitchFreq), transmitter);
            links.add(transmitter);
            links.add(receiver);
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, transmitter);
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, receiver);
        }
        activeLinks.put(coupler, links);
    }

    private void removeCoupler(Coupler coupler) {
        List<IRedstoneLinkable> links = activeLinks.remove(coupler);
        if (links != null && level != null)
            for (IRedstoneLinkable link : links)
                Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, link);
    }

    private void refreshAll() {
        if (level == null || level.isClientSide)
            return;
        for (Coupler coupler : couplers)
            registerCoupler(coupler);
    }

    private void removeAllLinks() {
        if (level == null)
            return;
        for (List<IRedstoneLinkable> links : activeLinks.values())
            for (IRedstoneLinkable link : links)
                Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, link);
        activeLinks.clear();
    }

    private class CoupleTransmitter implements IRedstoneLinkable {
        private final Couple<Frequency> key;
        int strength = 0;

        CoupleTransmitter(Couple<Frequency> key) {
            this.key = key;
        }

        @Override
        public int getTransmittedStrength() {
            return isAlive() ? strength : 0;
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
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == CouplerBlockEntity.this;
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

    private class CoupleReceiver implements IRedstoneLinkable {
        private final Couple<Frequency> key;
        private final CoupleTransmitter target;

        CoupleReceiver(Couple<Frequency> key, CoupleTransmitter target) {
            this.key = key;
            this.target = target;
        }

        @Override
        public int getTransmittedStrength() {
            return 0;
        }

        @Override
        public void setReceivedStrength(int power) {
            if (target.strength == power)
                return;
            target.strength = power;
            if (level == null)
                return;
            // Push the destination division immediately (no tick delay). Visited set to prevent loops
            if (!propagating.add(target))
                return;
            try {
                Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(level, target);
            } finally {
                propagating.remove(target);
            }
        }

        @Override
        public boolean isListening() {
            return true;
        }

        @Override
        public boolean isAlive() {
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == CouplerBlockEntity.this;
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

    // Clear receiver

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
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == CouplerBlockEntity.this;
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

    // GUI opening

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
        if (getCoupler(index) == null)
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
        return Component.translatable(menuEditMode ? "gui.pipeorgans.coupler.edit" : "gui.pipeorgans.coupler.main");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        if (menuEditMode)
            return CouplerEditMenu.create(containerId, playerInventory, this, menuEditIndex);
        return CouplerMenu.create(containerId, playerInventory, this);
    }

    // Lifecycle and persistence

    public static final BehaviourType<CouplerNetworkBehaviour> NETWORK_BEHAVIOUR = new BehaviourType<>();

    private class CouplerNetworkBehaviour extends BlockEntityBehaviour {
        CouplerNetworkBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public BehaviourType<?> getType() {
            return NETWORK_BEHAVIOUR;
        }

        @Override
        public void initialize() {
            super.initialize();
            refreshAll();
            clearReceiver = new ClearReceiver();
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, clearReceiver);
        }

        @Override
        public void unload() {
            super.unload();
            removeAllLinks();
            if (clearReceiver != null) {
                Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, clearReceiver);
                clearReceiver = null;
            }
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        ListTag list = new ListTag();
        for (Coupler coupler : couplers)
            list.add(coupler.toNbt());
        tag.put("Couplers", list);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        couplers.clear();
        ListTag list = tag.getList("Couplers", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++)
            couplers.add(Coupler.fromNbt(list.getCompound(i)));
        if (level != null && !level.isClientSide)
            removeAllLinks();
    }
}
