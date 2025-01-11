package com.finchy.pipeorgans.mixin;

import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoilerData.class)
public abstract class BoilerDataEvaluateMixin {

    /**
     * @author FinchyMcFinch
     * @reason let other blocks convert Create's fluid tanks to boilers
     */
    @Inject(method = "evaluate", at = @At())
    public void evaluate(FluidTankBlockEntity controller, CallbackInfoReturnable<Boolean> cir) {

    }
}
