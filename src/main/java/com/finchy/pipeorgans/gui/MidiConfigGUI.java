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

    private static final ResourceLocation guiTexture = PipeOrgans.asResource("textures/gui/midi_config.png");
    private static final int GUI_WIDTH = 208;
    private static final int GUI_HEIGHT = 134;

    private int cornerX;
    private int cornerY;

    private final MidiInputDeviceManager midiInputDeviceManager;
    private List<MidiDevice> availableMidiDevices;
    private int selectedDeviceIndex = 0; // from 0 to however many devices there are
    private String selectedDeviceName;

    protected MidiConfigGUI(String translatableTitle) {
        super(Component.translatable(translatableTitle));

        // get device manager from client proxy
        midiInputDeviceManager = ((ClientProxy)PipeOrgans.getProxy()).getMidiData().inputDeviceManager;
        // refresh devices list
        reloadDevices();

        setSelectedDeviceName();

        reevaluateCorners();
    }

    private void previousDevice() {
        selectedDeviceIndex = selectedDeviceIndex > 0 ? selectedDeviceIndex - 1 : availableMidiDevices.size()-1;
        setSelectedDeviceName();
    }

    private void nextDevice() {
        selectedDeviceIndex = selectedDeviceIndex < availableMidiDevices.size()-1 ? selectedDeviceIndex + 1 : 0;
        setSelectedDeviceName();
    }

    private void setSelectedDeviceName() {
        selectedDeviceName = availableMidiDevices!=null ?
                availableMidiDevices.get(selectedDeviceIndex).getDeviceInfo().getName() // if devices available
                :"No MIDI input devices available."; // if no midi input devices available (wow, really?)
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void reloadDevices() {
        availableMidiDevices = midiInputDeviceManager.getAvailableDevices();
        if (availableMidiDevices != null) {
            selectedDeviceIndex = 0;
            return;
        }
        selectedDeviceIndex = -1;
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        reevaluateCorners();
        super.resize(pMinecraft, pWidth, pHeight);
    }

    @Override
    protected void init() {
        // left and right buttons
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                Component.literal("<"),
                b -> previousDevice())
            .pos(cornerX+7,  cornerY+47).size(16, 16).build());
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                Component.literal(">"),
                b -> nextDevice())
            .pos(cornerX+149,  cornerY+47).size(16, 16).build());

        // refresh button
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                        Component.literal(""),
                        b -> reloadDevices())
                .pos(cornerX+167,  cornerY+47).size(16, 16).build());

    }

    private void reevaluateCorners() {
        cornerX = (Minecraft.getInstance().getWindow().getGuiScaledWidth()-GUI_WIDTH)/2;
        cornerY = (Minecraft.getInstance().getWindow().getGuiScaledHeight()-GUI_HEIGHT)/2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        renderGraphics(graphics);
        renderText(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderGraphics(GuiGraphics graphics) {
        graphics.blit(guiTexture, cornerX, cornerY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
    }

    private void renderText(GuiGraphics graphics) {
        // selected device name label
        graphics.drawString(minecraft.font, selectedDeviceName, cornerX+28, cornerY+51, 16777215, true);
    }
}
