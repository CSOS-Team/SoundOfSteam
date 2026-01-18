package com.finchy.pipeorgans.mixin;

import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import net.createmod.catnip.animation.LerpedFloat;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WhistleBlockEntity.class)
public abstract class WhistleBlockEntityMixin {

    @Shadow(remap = false) protected abstract boolean isPowered();
    @Shadow(remap = false) protected abstract WhistleBlock.WhistleSize getOctave();
    @Shadow(remap = false) protected abstract FluidTankBlockEntity getTank();
    @Shadow(remap = false) protected LerpedFloat animation;

    @Shadow(remap = false)
    protected abstract void tickAudio(WhistleBlock.WhistleSize size, boolean powered);

    @Inject(method = "tick",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void pipeorgans$allowWindchestPower(CallbackInfo ci) {
        WhistleBlockEntity self = (WhistleBlockEntity)(Object)this;

        if (!self.getLevel().isClientSide())
            return;

        FluidTankBlockEntity tank = getTank();

        BlockState state = self.getBlockState();
        BlockPos pos = self.getBlockPos();
        Direction attachedDir = WhistleBlock.getAttachedDirection(state);
        BlockPos attachedPos = pos.relative(attachedDir);
        BlockState attachedState = self.getLevel().getBlockState(attachedPos);

        boolean windchestActive = false;

        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            windchestActive = windchest.isMasterActive(
                    self.getLevel(),
                    attachedState.getValue(GenericPipeBlock.FACING),
                    attachedPos
            );
        }

        boolean powered =
                isPowered() &&
                        (
                                (tank != null
                                        && tank.boiler != null
                                        && tank.boiler.isActive()
                                        && (tank.boiler.passiveHeat || tank.boiler.activeHeat > 0))
                                        || windchestActive
                        );

        animation.chase(
                powered ? 1 : 0,
                powered ? .5f : .4f,
                powered ? LerpedFloat.Chaser.EXP : LerpedFloat.Chaser.LINEAR
        );
        animation.tickChaser();

        DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> tickAudio(getOctave(), powered)
        );

        ci.cancel(); // prevent Create client logic
    }
}
