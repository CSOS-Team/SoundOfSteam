package com.finchy.pipeorgans.mixin.createwhistle;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WhistleBlock.class, remap = false)
public class SteamWhistleMixin {

    @WrapOperation(
            method = "canSurvive",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/tank/FluidTankBlock;isTank(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean CanSurviveOnWindchest(BlockState state, Operation<Boolean> original) {
        return original.call(state) || (state.getBlock() instanceof WindchestBlock);
    }
}
