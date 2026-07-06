package com.finchy.pipeorgans.midi.client;

import net.minecraft.client.Minecraft;

public class MidiDataManager {
    public final MidiInputDeviceManager inputDeviceManager;

    public MidiDataManager() {
        inputDeviceManager = new MidiInputDeviceManager();
    }

    public boolean connectedToServer() {
        return (Minecraft.getInstance().player != null && (Minecraft.getInstance().hasSingleplayerServer() || Minecraft.getInstance().getCurrentServer() != null));
    }
}
