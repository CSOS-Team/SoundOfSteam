package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
/**
 * Mixin 1 of 2 that allows the base create Steam Whistle to function on CSOS windchests
 */

@Mixin(value = WhistleBlockEntity.class, remap = false)
public class WhistleBlockEntityMixin {

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/steamWhistle/WhistleBlockEntity;isVirtual()Z")
    )
    private boolean virtualOrWindchestActive(boolean original) {
        WhistleBlockEntity self = (WhistleBlockEntity)(Object)this;

        BlockPos attachedPos = self.getBlockPos().relative(
                WhistleBlock.getAttachedDirection(self.getBlockState())
        );
        BlockState attachedState = self.getLevel().getBlockState(attachedPos);
        boolean isActive = (attachedState.getBlock() instanceof WindchestBlock windchest)
                && windchest.isMasterActive(self.getLevel(), attachedState.getValue(WhistleBlock.FACING), attachedPos);

        return original || isActive;
    }

}
