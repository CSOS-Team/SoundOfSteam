package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.TrackerBarGUIPacket;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TrackerBarScreen extends AbstractSimiContainerScreen<TrackerBarMenu> {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/tracker_bar.png");
    private static final int GUI_WIDTH = 330;
    private static final int GUI_HEIGHT = 159;

    protected IconButton playButton;
    protected IconButton stopButton;
    protected IconButton confirmButton;

    private static final int BUTTONS_Y = 135;
    private static final int PLAY_X = 153;
    private static final int STOP_X = 175;
    private static final int CONFIRM_X = 297;

    private static final int MAX_INSTRUMENT_LABEL_WIDTH = 63;
    private static final int INSTRUMENT_LABELS_TEXT_X = 38;
    private static final int INSTRUMENT_LABELS_TEXT_Y = 25;

    private static final int INSTRUMENT_LABELS_HOVER_X = 35;
    private static final int INSTRUMENT_LABELS_HOVER_Y = 23;
    private static final int INSTRUMENT_LABEL_WIDTH = 69;
    private static final int INSTRUMENT_LABEL_HEIGHT = 12;

    private final ItemStack renderedItem = AllBlocks.TRACKER_BAR.asStack();

    public TrackerBarScreen(TrackerBarMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(GUI_WIDTH, GUI_HEIGHT + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(-11, 8);
        super.init();

        int x = leftPos;
        int y = topPos;

        boolean buttonsActive = menu.contentHolder.getButtonsEnabled();

        // add buttons 'n such
        playButton = new IconButton(x+PLAY_X, y+BUTTONS_Y, AllIcons.I_PLAY);
        playButton.active = buttonsActive;
        playButton.withCallback(() -> sendUpdatePacket("play"));

        stopButton = new IconButton(x+STOP_X, y+BUTTONS_Y, AllIcons.I_STOP);
        stopButton.active = buttonsActive;
        stopButton.withCallback(() -> sendUpdatePacket("stop"));

        confirmButton = new IconButton(x+CONFIRM_X, y+BUTTONS_Y, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> menu.player.closeContainer());

        addRenderableWidgets(playButton, stopButton, confirmButton);

    }

    private void sendUpdatePacket(String button) {
        AllPackets.getChannel().sendToServer(new TrackerBarGUIPacket(button, menu.contentHolder.getBlockPos()));
    }

    private static Component shortenText(Component componentIn, int maxWidth) {
        Font font = Minecraft.getInstance().font;
        if (font.width(componentIn) <= maxWidth)
            return componentIn;

        String trim = "...";
        int trimWidth = font.width(trim);
        String raw = componentIn.getString();
        int rawLength = raw.length();

        for (int i = rawLength; i>0; i--) {
            String sub = raw.substring(0, i);
            if (font.width(sub) + trimWidth <= maxWidth)
                return Component.literal(sub + trim).setStyle(componentIn.getStyle());
        }
        // if nothing fits, somehow
        return Component.literal(trim).setStyle(componentIn.getStyle());
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        boolean buttonsActive = menu.getButtonsEnabled();

        playButton.active = buttonsActive;
        stopButton.active = buttonsActive;

        playButton.setIcon(menu.isPlaying() ? AllIcons.I_PAUSE : AllIcons.I_PLAY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, (314-font.width(title))/2, 4, 0x505050, false);
        int channel = 0;
        for (int row=0; row<4; row++) {
            for (int column=0; column<4; column++) {
                Component instrumentName = menu.getChannelInstrumentName(channel++);
                graphics.drawString(
                        font, shortenText(
                                Component.literal(channel + ": ").append(instrumentName),
                                MAX_INSTRUMENT_LABEL_WIDTH
                        ),
                        column*INSTRUMENT_LABEL_WIDTH+INSTRUMENT_LABELS_TEXT_X,
                        row*INSTRUMENT_LABEL_HEIGHT+INSTRUMENT_LABELS_TEXT_Y,
                        16777215, true
                );
            }
        }
    }

    @Override
    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
        int labelsHoverX = INSTRUMENT_LABELS_HOVER_X + leftPos;
        int labelsHoverY = INSTRUMENT_LABELS_HOVER_Y + topPos;

        if (labelsHoverX+1 <= mouseX && mouseX <= labelsHoverX+INSTRUMENT_LABEL_WIDTH*4-1
                && labelsHoverY+1 <= mouseY && mouseY <= labelsHoverY+INSTRUMENT_LABEL_HEIGHT*4-1) {
            int hoveredIndexX = (mouseX-labelsHoverX)/INSTRUMENT_LABEL_WIDTH;
            int hoveredIndexY = (mouseY-labelsHoverY)/INSTRUMENT_LABEL_HEIGHT;

            Component hoveredName = menu.getChannelInstrumentName(hoveredIndexX + 4*hoveredIndexY);
            if (hoveredName.equals(Component.empty())) return;
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(hoveredName);
            graphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int invX = getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.getWidth())-15;
        int invY = topPos + GUI_HEIGHT + 4;
        renderPlayerInventory(pGuiGraphics, invX, invY);

        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 512, 512);

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(leftPos + GUI_WIDTH, topPos + GUI_HEIGHT - 40, -200)
                .scale(3)
                .render(pGuiGraphics);
    }
}
