package com.finchy.pipeorgans.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybinding {
    public static final String KEY_CATEGORY_PIPEORGANS = "key.category.pipeorgans";

    public static final String MIDI_CONFIG_TRANSLATE = "key.pipeorgans.midi_config";

    public static final KeyMapping MIDI_CONFIG_KEY = new KeyMapping(MIDI_CONFIG_TRANSLATE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SEMICOLON, KEY_CATEGORY_PIPEORGANS);
}
