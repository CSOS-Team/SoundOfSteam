package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.ClientConfig;
import com.mojang.blaze3d.audio.Library;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

@Mixin(Library.class)
public class SoundLibraryMixin {

    @Unique
    private static int pipeorgans$allocatedMaxSources = 255;

    private static int pipeorgans$maxSources() {
        try {
            return ClientConfig.MAX_SOUND_SOURCES.get();
        } catch (IllegalStateException notLoadedYet) {
            return 512;
        }
    }

    @Redirect(method = "init(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/openal/ALC10;alcCreateContext(JLjava/nio/IntBuffer;)J"))
    private long pipeorgans$createContextWithMaxSupportedSources(long device, IntBuffer ignoredNull) {
        int requestedMono = pipeorgans$maxSources();
        long context = 0;

        // Steps down from config maximum in aligned blocks until OS accepts the stream weight
        while (requestedMono >= 64) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                // Ensure exact 4-byte boundaries, stopping pitch & pan distortion
                int[] attribArray = new int[] {
                    ALC11.ALC_MONO_SOURCES, requestedMono,
                    ALC11.ALC_STEREO_SOURCES, 16,
                    0
                };

                IntBuffer attrs = stack.ints(attribArray);
                context = ALC10.alcCreateContext(device, attrs);

                if (context != 0) {
                    pipeorgans$allocatedMaxSources = requestedMono;
                    break;
                }
            }
            requestedMono -= 32;
        }

        // Fallback
        if (context == 0) {
            context = ALC10.alcCreateContext(device, (IntBuffer) null);
            pipeorgans$allocatedMaxSources = 255;
        }

        return context;
    }

    @ModifyConstant(method = "init(Ljava/lang/String;Z)V", constant = @Constant(intValue = 255))
    private int pipeorgans$raiseStaticCapToTrueAllocated(int original) {
        return pipeorgans$allocatedMaxSources;
    }
}
