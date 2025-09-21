package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.TrackerBarGUIPacket;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TrackerBarScreen extends AbstractSimiContainerScreen<TrackerBarMenu> {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/tracker_bar.png");
    private static final int GUI_WIDTH = 330;
    private static final int GUI_HEIGHT = 203;

    protected IconButton playButton;
    protected IconButton stopButton;
    protected IconButton confirmButton;

    protected Label titleLabel;

    private static final int maxInstrumentLabelWidth = 63;

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
        playButton = new IconButton(x+153, y+179, AllIcons.I_PLAY);
        playButton.active = buttonsActive;
        playButton.withCallback(() -> sendUpdatePacket("play"));

        stopButton = new IconButton(x+175, y+179, AllIcons.I_STOP);
        stopButton.active = buttonsActive;
        stopButton.withCallback(() -> sendUpdatePacket("stop"));

        confirmButton = new IconButton(x + 297, y + 179, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> menu.player.closeContainer());

        addRenderableWidgets(playButton, stopButton, confirmButton);

        titleLabel = new Label(41, 26, Component.empty());

    }

    private void sendUpdatePacket(String button) {
        AllPackets.getChannel().sendToServer(new TrackerBarGUIPacket(button, menu.contentHolder.getBlockPos()));
    }

    private static Component shortenText(Component componentIn) {
        Font font = Minecraft.getInstance().font;
        if (font.width(componentIn) <= TrackerBarScreen.maxInstrumentLabelWidth)
            return componentIn;

        String trim = "...";
        int trimWidth = font.width(trim);
        String raw = componentIn.getString();
        int rawLength = raw.length();

        for (int i = rawLength; i>0; i--) {
            String sub = raw.substring(0, i);
            if (font.width(sub) + trimWidth <= TrackerBarScreen.maxInstrumentLabelWidth)
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
        for (int column=0; column<4; column++) {
            for (int row=0; row<4; row++) {
                graphics.drawString(
                        font, shortenText(menu.getChannelInstrument(channel++)),
                        column*69+38, row*18+48, 16777215, true
                );
            }
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
