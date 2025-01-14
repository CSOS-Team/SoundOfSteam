package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlock;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( value = BoilerData.class, remap = false)
public class BoilerDataEvaluateMixin {

    /*
    @Definition(id = "STEAM_WHISTLE", field = "Lcom/simibubi/create/AllBlocks;STEAM_WHISTLE:Lcom/tterrag/registrate/util/entry/BlockEntry;")
    @Definition(id = "has", method = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    @Definition(id = "attachedState", local = @Local(type = BlockState.class, name = "attachedState"))
    @Expression("STEAM_WHISTLE.has(attachedState)")
    @ModifyExpressionValue(method = "evaluate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean checkOtherWhistleBlocks(boolean original) {
        return original || true;
    }
     */

    @Shadow public int attachedWhistles;

    @Inject(method = "evaluate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    shift = At.Shift.BY, by = 4
            )
    )
    private void checkOtherWhistleTypes(FluidTankBlockEntity controller,
                                        CallbackInfoReturnable<Boolean> cir,
                                        @Local Level levelGotten,
                                        @Local(ordinal = 1) BlockPos posGotten) {
        for (Direction d : Iterate.directions) {
            BlockPos attachedPos = posGotten.relative(d);
            BlockState attachedState = levelGotten.getBlockState(attachedPos);
            if (attachedState.getBlock() instanceof GenericWhistleBlock) {
                this.attachedWhistles++;
            }
        }
    }

}