package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MusicRollItem;
import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TrackerBarScreen extends AbstractSimiContainerScreen<TrackerBarMenu> {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/tracker_bar.png");
    private static final int GUI_WIDTH = 330;
    private static final int GUI_HEIGHT = 153;

    private ItemStack prevItem;

    protected IconButton playButton;
    protected IconButton stopButton;

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

        playButton = new IconButton(x+153, y+129, AllIcons.I_PLAY);
        playButton.active = false;
        playButton.withCallback(() -> {
           menu.contentHolder.pressTogglePlayButton();
           playButton.setIcon(menu.contentHolder.playing ? AllIcons.I_PAUSE : AllIcons.I_PLAY);
        });
        // todo: need to sort out proper logic for initialising these buttons correctly depending on contents of block entity

        stopButton = new IconButton(x+175, y+129, AllIcons.I_STOP);
        stopButton.active = false;
        stopButton.withCallback(() -> {
           menu.contentHolder.pressStopButton();
           playButton.setIcon(AllIcons.I_PLAY);
        });

        prevItem = menu.getSlot(0).getItem();

        addRenderableWidgets(playButton, stopButton);

    }

    @Override
    protected void containerTick() {
        super.containerTick();
        ItemStack stack = menu.getSlot(0).getItem();
        if (!stack.equals(prevItem, false)) { // if item in slot has changed
            prevItem = stack;

            if (!stack.equals(ItemStack.EMPTY)) { // item has been LOADED
                if (stack.hasTag()) { // item has tag
                    playButton.active = true;
                    playButton.setIcon(AllIcons.I_PLAY);
                    stopButton.active = true;
                    CompoundTag tag = stack.getTag();
                    String midi = tag.getString("File");
                    String owner = tag.getString("Owner");
                    menu.contentHolder.loadSequence(midi, owner);
                    return;

                } // else, item has been loaded, but has invalid tag

            } // else, item has been REMOVED
            playButton.active = false;
            playButton.setIcon(AllIcons.I_PLAY);
            stopButton.active = false;
            menu.contentHolder.unloadSequence();

        } // item hasn't changed, so we don't care

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
