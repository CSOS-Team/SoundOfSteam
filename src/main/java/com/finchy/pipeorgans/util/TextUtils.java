package com.finchy.pipeorgans.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import com.finchy.pipeorgans.PipeOrgans;

import java.util.List;
import java.util.Map;

public class TextUtils
{
    private static final MutableComponent NO_EFFECTS = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    /**
     * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
     */

    public static MutableComponent getTranslation(String key, Object... args) {
    return Component.translatable(PipeOrgans.MOD_ID + "." + key, args);
    }
}
