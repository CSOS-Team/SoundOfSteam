package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import com.finchy.pipeorgans.PipeOrgans;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.server.level.ServerLevel;

/**
 * Behavior that allows a block entity to apply mutations when being placed via clipboard-assisted placement.
 * The actual mutation logic is provided by the block entity implementing {@link ClipboardAssistedPlacement} (or with the callback constructor).
 * Clipboard-assisted placement works by storing a source block's position in the clipboard item tag, and when placing a new block with the clipboard in the off-hand,
 * it looks up the source block entity, copies its data to the target block entity (excluding metadata), and then applies the placement mutation to adjust properties as needed.
 */
public class ClipboardAssistedPlacementBehaviour extends BlockEntityBehaviour {

    protected ClipboardAssistedPlacement cAP;

    public ClipboardAssistedPlacementBehaviour(SmartBlockEntity be, ClipboardAssistedPlacement cAP) {
        super(be);
        this.cAP = cAP;
    }

    public ClipboardAssistedPlacementBehaviour(SmartBlockEntity be) {
        super(be);
        if (be instanceof ClipboardAssistedPlacement cap) {
            this.cAP = cap;
        } else {
            this.cAP = ($) -> ClipboardAssistedPlacement.MutationResult.FAILURE_KEEP;
        }
    }

    public static final BehaviourType<ClipboardAssistedPlacementBehaviour> TYPE = new BehaviourType<>();

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public ClipboardAssistedPlacement.MutationResult applyPlacementMutation(SmartBlockEntity previous) {
        if (previous == null)
            return ClipboardAssistedPlacement.MutationResult.FAILURE_KEEP;

        ClipboardAssistedPlacement.MutationResult result = cAP.applyPlacementMutation(previous);
        PipeOrgans.LOGGER.debug("Clipboard-assisted placement mutation result: success={}, srcAction={}", result.success(), result.srcAction());
        blockEntity.setChanged();
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            level.getChunkAt(blockEntity.getBlockPos()).setUnsaved(true);
        }

        blockEntity.sendData();
        blockEntity.notifyUpdate();

        if (blockEntity.getLevel() instanceof ServerLevel level) {
            level.scheduleTick(blockEntity.getBlockPos(), blockEntity.getBlockState().getBlock(), 1);
        }

        PipeOrgans.LOGGER.debug("Notified block entity update after clipboard-assisted placement mutation");

        return result;
    }
}
