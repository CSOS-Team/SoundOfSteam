package com.finchy.pipeorgans.mixin;

import com.finchy.pipeorgans.ClientConfig;
import com.mojang.blaze3d.audio.Library;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@Mixin(Library.class)
public class SoundLibraryMixin {
    private static int pipeorgans$maxSources() {
        try {
            return ClientConfig.MAX_SOUND_SOURCES.get();
        } catch (IllegalStateException notLoadedYet) {
            return 512;
        }
    }

    // Ask OpenAL for more mono sources instead of accepting the ~256 default.
    @Redirect(method = "init(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/openal/ALC10;alcCreateContext(JLjava/nio/IntBuffer;)J"))
    private long pipeorgans$createContextWithMoreSources(long device, IntBuffer ignoredNull) {
        int mono = pipeorgans$maxSources();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer attrs = stack.mallocInt(5)
                    .put(ALC11.ALC_MONO_SOURCES).put(mono)
                    .put(ALC11.ALC_STEREO_SOURCES).put(16)
                    .put(0);
            attrs.flip();
            return ALC10.alcCreateContext(device, attrs);
        }
    }

    // Lift the hard 255 cap on the static channel pool to the configured maximum.
    @ModifyConstant(method = "init(Ljava/lang/String;Z)V", constant = @Constant(intValue = 255))
    private int pipeorgans$raiseStaticCap(int original) {
        return pipeorgans$maxSources();
    }
}
