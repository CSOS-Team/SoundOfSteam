package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import net.minecraft.core.Direction;

import static com.finchy.pipeorgans.init.AllTags.AllBlockTags.VALID_WHISTLE;

public class AllBlockMovementChecks {
    public static void register() {
        BlockMovementChecks.registerAttachedCheck(
                (state, world, pos, direction) -> {
                    if (VALID_WHISTLE.matches(state))
                        return BlockMovementChecks.CheckResult.of(direction == (
                                state.getValue(GenericPipeBlock.WALL) ? state.getValue(GenericPipeBlock.FACING) : Direction.DOWN
                        ));
                    else if (state.getBlock() instanceof GenericExtensionBlock<?>)
                        return BlockMovementChecks.CheckResult.of(direction == ((GenericExtensionBlock<?>) state.getBlock()).getAttachedDirection(state));
                    
                    else
                        return BlockMovementChecks.CheckResult.PASS;
                }
        );
    }
}
