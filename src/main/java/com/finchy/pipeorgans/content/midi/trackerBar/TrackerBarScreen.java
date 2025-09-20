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

    }

    private void sendUpdatePacket(String button) {
        AllPackets.getChannel().sendToServer(new TrackerBarGUIPacket(button, menu.contentHolder.getBlockPos()));
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
