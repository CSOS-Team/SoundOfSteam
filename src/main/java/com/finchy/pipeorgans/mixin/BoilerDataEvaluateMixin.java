package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.block.GedecktBlock;
import com.finchy.pipeorgans.PipeOrgans;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
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
                    target = "Lcom/simibubi/create/content/fluids/tank/FluidTankBlockEntity;getLevel()Lnet/minecraft/world/level/Level;",
                    shift = At.Shift.BY, by = 4
            )
    )
    private void checkOtherWhistleTypes(FluidTankBlockEntity controller, CallbackInfoReturnable<Boolean> cir) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        for (int yOffset = 0; yOffset < controller.getHeight(); yOffset++) {
            for (int xOffset = 0; xOffset < controller.getWidth(); xOffset++) {
                for (int zOffset = 0; zOffset < controller.getWidth(); zOffset++) {

                    BlockPos pos = controllerPos.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!FluidTankBlock.isTank(blockState))
                        continue;
                    for (Direction d : Iterate.directions) {
                        BlockPos attachedPos = pos.relative(d);
                        BlockState attachedState = level.getBlockState(attachedPos);
                        if (attachedState.getBlock() instanceof GedecktBlock
                                && WhistleBlock.getAttachedDirection(attachedState)
                                .getOpposite() == d) {
                            PipeOrgans.LOGGER.info("before:" + attachedWhistles);
                            attachedWhistles++;
                            PipeOrgans.LOGGER.info("after:" + attachedWhistles);
                        }
                    }
                }
            }
        }
    }

}