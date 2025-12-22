package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.nbt.CompoundTag;

/**
 * Behavior that allows a block entity to apply mutations when being placed via clipboard-assisted placement.
 * The actual mutation logic is provided by the block entity implementing {@link ClipboardAssistedPlacement} (or with the callback constructor).
 * Clipboard-assisted placement works by storing a source block's position in the clipboard item tag, and when placing a new block with the clipboard in the off-hand,
 * it looks up the source block entity, copies its data to the target block entity (excluding metadata), and then applies the placement mutation to adjust properties as needed.
 */
public class ClipboardAssistedPlacementBehaviour extends BlockEntityBehaviour {

    protected Runnable applyPlacementMutation;

    public ClipboardAssistedPlacementBehaviour(SmartBlockEntity be, Runnable applyPlacementMutation) {
        super(be);
        this.applyPlacementMutation = applyPlacementMutation;
    }

    public ClipboardAssistedPlacementBehaviour(SmartBlockEntity be) {
        super(be);
        if (be instanceof ClipboardAssistedPlacement cap) {
            this.applyPlacementMutation = cap::applyPlacementMutation;
        } else {
            this.applyPlacementMutation = () -> {};
        }
    }

    public static final BehaviourType<ClipboardAssistedPlacementBehaviour> TYPE = new BehaviourType<>();

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public void applyPlacementMutation(ClipboardAssistedPlacementBehaviour previous) {
        if (previous == null)
            return;
        blockEntity.load(previous.blockEntity.saveWithoutMetadata()); // avoids identity conflicts
        applyPlacementMutation.run();
        blockEntity.setChanged();
        blockEntity.sendData();
        blockEntity.notifyUpdate();
    }
}
