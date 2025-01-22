package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.block.diapason.DiapasonBlock;
import com.finchy.pipeorgans.block.gamba.GambaBlock;
import com.finchy.pipeorgans.block.gedeckt.GedecktBlock;
import com.finchy.pipeorgans.block.piccolo.PiccoloBlock;
import com.finchy.pipeorgans.block.subbass.SubbassBlock;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.tank.BoilerData;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BoilerData.class, remap = false)
public class BoilerDataEvaluateMixin {

    @Definition(id = "STEAM_WHISTLE", field = "Lcom/simibubi/create/AllBlocks;STEAM_WHISTLE:Lcom/tterrag/registrate/util/entry/BlockEntry;")
    @Definition(id = "has", method = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    @Expression("STEAM_WHISTLE.has(?)")
    @ModifyExpressionValue(method = "evaluate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean checkOtherWhistleBlocks(boolean original, @Local(ordinal=1) BlockState attachedState) {
        return original
                || attachedState.getBlock() instanceof GedecktBlock
                || attachedState.getBlock() instanceof DiapasonBlock
                || attachedState.getBlock() instanceof GambaBlock
                || attachedState.getBlock() instanceof PiccoloBlock
                || attachedState.getBlock() instanceof SubbassBlock;
    }

    /*
    @WrapOperation(
            method = "evaluate",
            at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean checkOtherWhistleBlocks(BlockEntry instance, BlockState state, Operation<Boolean> original) {
        return AllBlocks.STEAM_WHISTLE.has(state)
                || state.getBlock() instanceof GedecktBlock
                || state.getBlock() instanceof DiapasonBlock;
    }
     */

}