package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.content.windchest.WindchestMasterBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EncasedFanBlockEntity.class, remap = false)
public class FanUpdateWindchestMixin {

    @Inject(method = "onSpeedChanged", at = @At(value = "TAIL"))
    private void updateSpeedChanged(float prevSpeed, CallbackInfo ci) {

        EncasedFanBlockEntity instance = (EncasedFanBlockEntity)(Object)this;

        Level level = instance.getLevel();
        BlockState fanState = instance.getBlockState();
        BlockPos adjacentPos = instance.getBlockPos().relative(fanState.getValue(EncasedFanBlock.FACING));
        if (level.getBlockState(adjacentPos).getBlock() instanceof WindchestMasterBlock) {
            WindchestMasterBlock.updateMasterWindy(level, adjacentPos);
        }
    }

//    @WrapOperation(method = "remove", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/fan/EncasedFanBlockEntity;updateChute()V"))
//    private void updateRemove(EncasedFanBlockEntity instance, Operation<Void> original) {
//
//        original.call(instance);
//
//        Level level = instance.getLevel();
//        BlockState fanState = instance.getBlockState();
//        BlockPos adjacentPos = instance.getBlockPos().relative(fanState.getValue(EncasedFanBlock.FACING));
//        if (level.getBlockState(adjacentPos).getBlock() instanceof WindchestMasterBlock) {
//            WindchestMasterBlock.updateMasterWindy(level, adjacentPos);
//        }
//        PipeOrgans.LOGGER.info("remove");
//    }
//
    @Inject(method = "blockInFrontChanged", at = @At(value = "TAIL"))
    private void updateBIFC(CallbackInfo ci) {
        EncasedFanBlockEntity instance = (EncasedFanBlockEntity)(Object)this;
        instance.updateChute();

        Level level = instance.getLevel();
        BlockState fanState = instance.getBlockState();
        BlockPos adjacentPos = instance.getBlockPos().relative(fanState.getValue(EncasedFanBlock.FACING));
        if (level.getBlockState(adjacentPos).getBlock() instanceof WindchestMasterBlock) {
            WindchestMasterBlock.updateMasterWindy(level, adjacentPos);
        }
    }
}
