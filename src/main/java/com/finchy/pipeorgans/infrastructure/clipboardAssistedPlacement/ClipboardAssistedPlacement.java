package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface ClipboardAssistedPlacement {
    enum SourceAction {
        KEEP,
        REPLACE,
        REMOVE
    }

    record MutationResult(boolean success, SourceAction srcAction) {
        public static final MutationResult SUCCESS_KEEP = new MutationResult(true, SourceAction.KEEP);
        public static final MutationResult SUCCESS_REPLACE = new MutationResult(true, SourceAction.REPLACE);
        public static final MutationResult SUCCESS_REMOVE = new MutationResult(true, SourceAction.REMOVE);

        public static final MutationResult FAILURE_KEEP = new MutationResult(false, SourceAction.KEEP);
        public static final MutationResult FAILURE_REPLACE = new MutationResult(false, SourceAction.REPLACE);
        public static final MutationResult FAILURE_REMOVE = new MutationResult(false, SourceAction.REMOVE);
    }

    MutationResult applyPlacementMutation(SmartBlockEntity previous);
}
