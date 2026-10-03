package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.content.noteLink.NoteLinkBehaviour;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RedstoneLinkNetworkHandler.class, remap = false)
public class RedstoneLinkNetworkHandlerMixin {
    @Inject(method = "updateNetworkOf", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 1))
    private void onUpdateNetworkOf(LevelAccessor level, IRedstoneLinkable actor, CallbackInfo ci, @Local(name = "power") int power) {
        if (actor instanceof NoteLinkBehaviour noteLink) {
            if (noteLink.isListening()) {
                noteLink.forceNewPos();
                noteLink.setReceivedStrength(power);
            }
        }
    }
}
