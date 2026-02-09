package com.finchy.pipeorgans.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.finchy.pipeorgans.content.windchest.WindchestBlock;

import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Mixin 2 of 2 that allows the base create Steam Whistle to function on CSOS windchests
 */

@Mixin(value = WhistleBlock.class, remap = false)
public abstract class WhistleBlockMixin {

    @WrapOperation(
            method = "canSurvive",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/tank/FluidTankBlock;isTank(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean isTankOrWindchest(BlockState state, Operation<Boolean> original) {
        return original.call(state) || state.getBlock() instanceof WindchestBlock;
    }
}
