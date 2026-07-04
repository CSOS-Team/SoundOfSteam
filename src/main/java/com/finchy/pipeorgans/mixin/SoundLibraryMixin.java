package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.ClientConfig;
import com.mojang.blaze3d.audio.Library;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.nio.IntBuffer;

@Mixin(Library.class)
public class SoundLibraryMixin {

    @Unique
    private int pipeorgans$allocatedMaxSources = 255;

    @Unique
    private int pipeorgans$maxSources() {
        try {
            return ClientConfig.MAX_SOUND_SOURCES.get();
        } catch (IllegalStateException notLoadedYet) {
            return 512;
        }
    }

    @ModifyArg(
        method = "init(Ljava/lang/String;Z)V",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/openal/ALC10;alcCreateContext(JLjava/nio/IntBuffer;)J", remap = false),
        index = 1
    )
    private IntBuffer pipeorgans$injectHardwareChannels(long device, IntBuffer localAttributes) {
        int requestedSources = pipeorgans$maxSources();

        MemoryStack stack = MemoryStack.stackGet();

        int[] attribArray = new int[] {
            ALC11.ALC_MONO_SOURCES, requestedSources,
            ALC11.ALC_STEREO_SOURCES, 8,
            0
        };

        IntBuffer safeBuffer = stack.ints(attribArray);
        this.pipeorgans$allocatedMaxSources = requestedSources;

        return safeBuffer;
    }

    @ModifyConstant(method = "init(Ljava/lang/String;Z)V", constant = @Constant(intValue = 255))
    private int pipeorgans$raiseStaticCapToTrueAllocated(int original) {
        return this.pipeorgans$allocatedMaxSources;
    }
}
