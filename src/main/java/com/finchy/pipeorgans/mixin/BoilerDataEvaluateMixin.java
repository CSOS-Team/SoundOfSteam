package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.init.AllTags;
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
                || attachedState.is(AllTags.Blocks.VALID_WHISTLES);
    }

}