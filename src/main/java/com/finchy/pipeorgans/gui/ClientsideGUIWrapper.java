package com.finchy.pipeorgans.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class ClientsideGUIWrapper {

    public static void openMidiConfigGUI(Level level, Player player) {
        openGUI();
    }

    public static void openGUI(Level level, Screen screen) {
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(screen);
        }
    }

}
