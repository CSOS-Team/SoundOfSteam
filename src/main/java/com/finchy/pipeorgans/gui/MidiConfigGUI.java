package com.finchy.pipeorgans.gui;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.midi.client.ClientProxy;
import com.finchy.pipeorgans.midi.client.MidiInputDeviceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.sound.midi.MidiDevice;
import java.util.List;

public class MidiConfigGUI extends Screen {

    private ResourceLocation guiTexture;
    private final int GUI_WIDTH = 176;
    private final int GUI_HEIGHT = 166;
    private final int TEXTURE_DIMENSIONS = 256;

    private MidiInputDeviceManager midiInputDeviceManager;
    private List<MidiDevice> availableMidiDevices;
    private int visibleDeviceId = 0;

    protected MidiConfigGUI(String translatableTitle) {
        super(Component.translatable(translatableTitle));
        // get device manager from client proxy
        midiInputDeviceManager = ((ClientProxy)PipeOrgans.getProxy()).getMidiData().inputDeviceManager;
        // get available input devices
        availableMidiDevices = midiInputDeviceManager.getAvailableDevices();

        guiTexture = PipeOrgans.asResource("textures/gui/inventory.png");
        // add left and right buttons
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("<"), b -> setCurrentPage(this.pages.get(Math.max(this.pages.indexOf(this.currentPage) - 1, 0)))).pos(leftPos,  topPos - 50).size(16, 16).build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        renderGraphics(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderGraphics(GuiGraphics graphics) {
        int cornerX = (Minecraft.getInstance().getWindow().getGuiScaledWidth()-GUI_WIDTH)/2;
        int cornerY = (Minecraft.getInstance().getWindow().getGuiScaledHeight()-GUI_HEIGHT)/2;
        graphics.blit(guiTexture, cornerX, cornerY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
    }
}
