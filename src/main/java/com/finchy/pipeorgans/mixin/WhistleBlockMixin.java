package com.finchy.pipeorgans.mixin;

import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WhistleBlock.class)
public abstract class WhistleBlockMixin {

    @Inject(
            method = "canSurvive",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pipeorgans$allowWindchest(
            BlockState state,
            LevelReader level,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        BlockPos attachedPos = pos.relative(
                WhistleBlock.getAttachedDirection(state)
        );

        BlockState attachedState = level.getBlockState(attachedPos);

        if (attachedState.getBlock() instanceof WindchestBlock) {
            cir.setReturnValue(true);
        }
    }
}
