package com.finchy.pipeorgans.gui;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.midi.client.ClientProxy;
import com.finchy.pipeorgans.midi.client.MidiInputDeviceManager;
import com.finchy.pipeorgans.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

import javax.sound.midi.MidiDevice;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class MidiConfigGUI extends Screen {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/midi_config.png");
    private static final int GUI_WIDTH = 208;
    private static final int GUI_HEIGHT = 134;

    private int cornerX;
    private int cornerY;

    private static final int BUTTONS_Y = 47;
    private static final int PREV_BUTTON_X = 7;
    private static final int NEXT_BUTTON_X = 149;

    private static final int REFRESH_BUTTON_X = 167;
    private static final int REFRESH_BUTTON_U = 208;

    private static final int SAVE_BUTTON_X = 185;
    private static final int SAVE_BUTTON_U = 224;

    private static final int DEVICE_LABELS_X = 10;

    private final MidiInputDeviceManager midiInputDeviceManager;
    private List<MidiDevice> availableMidiDevices = new ArrayList<>();
    private int selectedDeviceIndex = 0; // from 0 to however many devices there are

    private final Component title = Component.translatable("gui.pipeorgans.midi_config");
    private final Font font = Minecraft.getInstance().font;

    private String selectedDeviceName;
    private String selectedDeviceVendor;
    private String selectedDeviceVersion;
    private String selectedDeviceDesc;

    private String activeDeviceName;

    protected MidiConfigGUI(String translatableTitle) {
        super(Component.translatable(translatableTitle));

        // get device manager from client proxy
        midiInputDeviceManager = ((ClientProxy)PipeOrgans.getProxy()).getMidiData().inputDeviceManager;
        // refresh devices list
        reloadDevices();

        reevaluateCorners();
    }

    private void previousDevice() {
        selectedDeviceIndex = selectedDeviceIndex > 0 ? selectedDeviceIndex - 1 : availableMidiDevices.size()-1;
        // if 0, loop around, otherwise subtract 1
        setSelectedDeviceInfo();
    }

    private void nextDevice() {
        selectedDeviceIndex = selectedDeviceIndex < availableMidiDevices.size()-1 ? selectedDeviceIndex + 1 : 0;
        // if maximum length, loop around to 0, otherwise add 1
        setSelectedDeviceInfo();
    }

    private void setSelectedDeviceInfo() {
        if (!availableMidiDevices.isEmpty()) {
            selectedDeviceName = availableMidiDevices.get(selectedDeviceIndex).getDeviceInfo().getName();
            selectedDeviceVendor = "Vendor: "+availableMidiDevices.get(selectedDeviceIndex).getDeviceInfo().getVendor();
            selectedDeviceVersion = "Version: "+availableMidiDevices.get(selectedDeviceIndex).getDeviceInfo().getVersion();
            selectedDeviceDesc = availableMidiDevices.get(selectedDeviceIndex).getDeviceInfo().getDescription();
        } else {
            selectedDeviceName = "No MIDI input devices available."; // if no midi input devices available (wow, really?)
            selectedDeviceVendor = "";
            selectedDeviceVersion = "";
            selectedDeviceDesc = "";
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void reloadDevices() {
        // for some reason, god only knows why, MidiSystem.getMidiDeviceInfo() doesn't seem to
        // update when devices are removed - at least not in my testing with loopMIDI.
        availableMidiDevices = midiInputDeviceManager.getAvailableDevices();
        selectedDeviceIndex = 0;
        setSelectedDeviceInfo();
    }

    public void saveSelection() {
        midiInputDeviceManager.saveDeviceSelection(availableMidiDevices.get(selectedDeviceIndex));
        // get active device name
        activeDeviceName = "Current: "+midiInputDeviceManager.getActiveDeviceName();
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        reevaluateCorners();
        super.resize(pMinecraft, pWidth, pHeight);
    }

    @Override
    protected void init() {
        // get active device name
        activeDeviceName = "Current: "+midiInputDeviceManager.getActiveDeviceName();
        // left and right buttons
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                Component.literal("<"),
                b -> previousDevice())
            .pos(cornerX+ PREV_BUTTON_X,  cornerY+ BUTTONS_Y).size(16, 16).build());
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                Component.literal(">"),
                b -> nextDevice())
            .pos(cornerX+ NEXT_BUTTON_X,  cornerY+ BUTTONS_Y).size(16, 16).build());

        // refresh button
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                        Component.literal(""),
                        b -> reloadDevices())
                .pos(cornerX+ REFRESH_BUTTON_X,  cornerY+ BUTTONS_Y).size(16, 16).build());

        // save button
        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                        Component.literal(""),
                        b -> saveSelection())
                .pos(cornerX+ SAVE_BUTTON_X,  cornerY+ BUTTONS_Y).size(16, 16).build());

    }

    private void reevaluateCorners() {
        cornerX = (Minecraft.getInstance().getWindow().getGuiScaledWidth()-GUI_WIDTH)/2;
        cornerY = (Minecraft.getInstance().getWindow().getGuiScaledHeight()-GUI_HEIGHT)/2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        // render base texture
        graphics.blit(GUI_TEXTURE, cornerX, cornerY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        super.render(graphics, mouseX, mouseY, partialTick);

        renderGraphics(graphics);
        renderText(graphics);
    }

    private void renderGraphics(GuiGraphics graphics) {
        graphics.blit(GUI_TEXTURE, cornerX+ REFRESH_BUTTON_X, cornerY+ BUTTONS_Y, REFRESH_BUTTON_U, 0, 16, 16, 256, 256); // refresh button icon
        graphics.blit(GUI_TEXTURE, cornerX+ SAVE_BUTTON_X, cornerY+ BUTTONS_Y, SAVE_BUTTON_U, 0, 16, 16, 256, 256); // save button icon
    }

    private void renderText(GuiGraphics graphics) {
        // title label
        graphics.drawString(font, title,
                cornerX+8, cornerY+6, 4210752, false);

        // current device label
        graphics.drawString(font, activeDeviceName,
                cornerX+ DEVICE_LABELS_X, cornerY+31, 16777215, true);
        // name label
        graphics.drawString(font, selectedDeviceName,
                cornerX+28, cornerY+51, 16777215, true);
        // vendor label
        graphics.drawString(font, selectedDeviceVendor,
                cornerX+ DEVICE_LABELS_X, cornerY+70, 16777215, true);
        // version label
        graphics.drawString(font, selectedDeviceVersion,
                cornerX+ DEVICE_LABELS_X, cornerY+81, 16777215, true);
        // description label
        GuiUtils.drawWordWrapDropShadow(graphics, font, FormattedText.of(selectedDeviceDesc),
                cornerX+10, cornerY+92, 188, 16777215, true);
    }
}
