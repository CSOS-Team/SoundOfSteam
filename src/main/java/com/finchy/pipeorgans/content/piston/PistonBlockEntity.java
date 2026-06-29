package com.finchy.pipeorgans.content.piston;

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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public class PistonBlockEntity extends SmartBlockEntity implements MenuProvider {

    public static final int PISTON_COUNT = 16;
    public static final int SCAN_RADIUS = 6;
    public static final int CLEAR_PULSE_TICKS = 4;

    private final PistonPreset[] presets = new PistonPreset[PISTON_COUNT];

    private ClearTransmitter clearTransmitter;
    private int clearPulse = 0;
    private boolean tuttiActive = false;

    public PistonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        for (int i = 0; i < PISTON_COUNT; i++)
            presets[i] = new PistonPreset();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new PistonNetworkBehaviour(this));
    }

    public boolean isPresetEmpty(int index) {
        return index < 0 || index >= PISTON_COUNT || presets[index].isEmpty();
    }

    // Scanning nearby combination targets

    private Map<Long, CombinationTarget> scanTargets() {
        Map<Long, CombinationTarget> found = new LinkedHashMap<>();
        if (level == null)
            return found;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -SCAN_RADIUS; dx <= SCAN_RADIUS; dx++)
            for (int dy = -SCAN_RADIUS; dy <= SCAN_RADIUS; dy++)
                for (int dz = -SCAN_RADIUS; dz <= SCAN_RADIUS; dz++) {
                    cursor.setWithOffset(worldPosition, dx, dy, dz);
                    if (!level.isLoaded(cursor))
                        continue;
                    BlockEntity be = level.getBlockEntity(cursor);
                    if (be instanceof CombinationTarget target)
                        found.put(new BlockPos(dx, dy, dz).asLong(), target);
                }
        return found;
    }

    // Actions (server-side)

    public void setPiston(int index) {
        if (level == null || level.isClientSide || index < 0 || index >= PISTON_COUNT)
            return;
        PistonPreset preset = presets[index];
        preset.clear();
        for (Map.Entry<Long, CombinationTarget> entry : scanTargets().entrySet()) {
            CombinationTarget target = entry.getValue();
            long mask = 0L;
            int size = Math.min(target.combinationSize(), 64);
            for (int i = 0; i < size; i++)
                if (target.isEntryPressed(i))
                    mask |= 1L << i;
            preset.states.put(entry.getKey(), mask);
        }
        setChanged();
        notifyUpdate();
    }

    public void recallPiston(int index) {
        if (level == null || level.isClientSide || index < 0 || index >= PISTON_COUNT)
            return;
        PistonPreset preset = presets[index];
        if (preset.isEmpty())
            return; // an unset piston does nothing
        for (Map.Entry<Long, CombinationTarget> entry : scanTargets().entrySet()) {
            CombinationTarget target = entry.getValue();
            long mask = preset.states.getOrDefault(entry.getKey(), 0L);
            int size = target.combinationSize();
            for (int i = 0; i < size; i++)
                target.setEntryPressed(i, (mask & (1L << i)) != 0L);
        }
    }

    public void clearPiston(int index) {
        if (level == null || level.isClientSide || index < 0 || index >= PISTON_COUNT)
            return;
        presets[index].clear();
        setChanged();
        notifyUpdate();
    }

    public boolean isTuttiActive() {
        return tuttiActive;
    }

    // Non-destructive override on nearby console blocks
    public void tutti() {
        if (level == null || level.isClientSide)
            return;
        tuttiActive = !tuttiActive;
        applyTutti();
        setChanged();
        notifyUpdate();
    }

    private void applyTutti() {
        for (CombinationTarget target : scanTargets().values())
            target.setTuttiOverride(tuttiActive);
    }

    public void clear() {
        if (level == null || level.isClientSide)
            return;
        if (tuttiActive) {
            tuttiActive = false;
            applyTutti();
            setChanged();
            notifyUpdate();
        }
        if (clearTransmitter == null) {
            clearTransmitter = new ClearTransmitter();
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(level, clearTransmitter);
        }
        clearPulse = CLEAR_PULSE_TICKS;
        Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(level, clearTransmitter);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide)
            return;
        if (clearPulse > 0) {
            clearPulse--;
            if (clearPulse == 0)
                removeClearTransmitter();
        }
    }

    private void removeClearTransmitter() {
        if (clearTransmitter != null && level != null) {
            Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(level, clearTransmitter);
            clearTransmitter = null;
        }
    }

    private class ClearTransmitter implements IRedstoneLinkable {
        private final Couple<Frequency> key = ClearSignal.networkKey();

        @Override
        public int getTransmittedStrength() {
            return clearPulse > 0 ? 15 : 0;
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
            return !isRemoved() && level != null && level.getBlockEntity(worldPosition) == PistonBlockEntity.this;
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

    // GUI

    public void openMenu(ServerPlayer player) {
        NetworkHooks.openScreen(player, this, this::sendToMenu);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.piston.title");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return PistonMenu.create(containerId, playerInventory, this);
    }

    // Lifecycle and persistence

    public static final BehaviourType<PistonNetworkBehaviour> NETWORK_BEHAVIOUR = new BehaviourType<>();

    private class PistonNetworkBehaviour extends BlockEntityBehaviour {
        PistonNetworkBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public BehaviourType<?> getType() {
            return NETWORK_BEHAVIOUR;
        }

        @Override
        public void initialize() {
            super.initialize();
            if (tuttiActive)
                applyTutti();
        }

        @Override
        public void unload() {
            super.unload();
            removeClearTransmitter();
            if (tuttiActive)
                for (CombinationTarget target : scanTargets().values())
                    target.setTuttiOverride(false);
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        ListTag list = new ListTag();
        for (PistonPreset preset : presets)
            list.add(preset.toNbt());
        tag.put("Presets", list);
        tag.putBoolean("Tutti", tuttiActive);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        ListTag list = tag.getList("Presets", Tag.TAG_COMPOUND);
        for (int i = 0; i < PISTON_COUNT && i < list.size(); i++)
            presets[i] = PistonPreset.fromNbt(list.getCompound(i));
        tuttiActive = tag.getBoolean("Tutti");
    }
}
