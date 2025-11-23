package com.finchy.pipeorgans.content.midi.rollpuncher;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.PipeOrgansClient;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.midi.PipeOrgansPaths;
import com.finchy.pipeorgans.midi.client.ClientMidiLoader;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RollPuncherScreen extends AbstractSimiContainerScreen<RollPuncherMenu> {

    private ScrollInput midisArea;
    private IconButton confirmButton;
    private IconButton folderButton;
    private IconButton refreshButton;
    private Label midisLabel;

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/roll_puncher.png");
    private static final int GUI_WIDTH = 214;
    private static final int GUI_HEIGHT = 85;

    private float progress;
    private float chasingProgress;
    private float lastChasingProgress;

    private final ItemStack renderedItem = AllBlocks.ROLL_PUNCHER.asStack();

    private List<Rect2i> extraAreas = Collections.emptyList();

    public RollPuncherScreen(RollPuncherMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(GUI_WIDTH, GUI_HEIGHT + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(-11, 8);
        super.init();

        PipeOrgansClient.MIDI_SENDER.refresh();
        List<Component> availableMidis = PipeOrgansClient.MIDI_SENDER.getAvailableMidis();

        int x = leftPos;
        int y = topPos+2;

        midisLabel = new Label(x+51, y+26, CommonComponents.EMPTY).withShadow();
        midisLabel.text = CommonComponents.EMPTY;
        if (!availableMidis.isEmpty()) {
            midisArea = new SelectionScrollInput(x+45, y+21, 139, 18).forOptions(availableMidis)
                    .titled(Component.translatable("gui.pipeorgans.roll_puncher.availableMidis"))
                    .writingTo(midisLabel);
            addRenderableWidgets(midisArea, midisLabel);
        }

        confirmButton = new IconButton(x+44, y+56, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            if (menu.canWrite() && midisArea != null) {
                ClientMidiLoader midiSender = PipeOrgansClient.MIDI_SENDER;
                lastChasingProgress = chasingProgress = progress = 0;
                List<Component> availableMidis1 = midiSender.getAvailableMidis();
                Component midi = availableMidis1.get(midisArea.getState());
                midiSender.startNewUpload(midi.getString());
            }
        });

        folderButton = new IconButton(x+20, y+21, AllIcons.I_OPEN_FOLDER);
        folderButton.withCallback(() -> Util.getPlatform().openFile(PipeOrgansPaths.MIDIS_DIR.toFile()));
        folderButton.setToolTip(Component.translatable("gui.pipeorgans.roll_puncher.open_folder"));

        refreshButton = new IconButton(x+206, y+21, AllIcons.I_REFRESH);
        refreshButton.withCallback(() -> {
            ClientMidiLoader midiSender = PipeOrgansClient.MIDI_SENDER;
            midiSender.refresh();
            List<Component> availableMidis1 = midiSender.getAvailableMidis();
            removeWidget(midisArea);

            if (!availableMidis1.isEmpty()) {
                midisArea = new SelectionScrollInput(leftPos+45, topPos+21, 139, 18)
                        .forOptions(availableMidis1)
                        .titled(Component.translatable("gui.pipeorgans.roll_puncher.availableMidis"))
                        .writingTo(midisLabel);
                midisArea.onChanged();
                addRenderableWidget(midisArea);
            } else {
                midisArea = null;
                midisLabel.text = CommonComponents.EMPTY;
            }
        });
        refreshButton.setToolTip(Component.translatable("gui.pipeorgans.roll_puncher.refresh"));

        addRenderableWidgets(confirmButton, folderButton, refreshButton);

        extraAreas = ImmutableList.of(
                new Rect2i(x+GUI_WIDTH, y+GUI_HEIGHT-40, 48, 48),
                new Rect2i(refreshButton.getX(), refreshButton.getY(), refreshButton.getWidth(), refreshButton.getHeight())
        );
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int invX = getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.getWidth());
        int invY = topPos+GUI_HEIGHT+4;
        renderPlayerInventory(pGuiGraphics, invX, invY);

        int x = leftPos;
        int y = topPos;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        Component titleText;
        if (menu.contentHolder.isUploading)
            titleText = Component.translatable("gui.pipeorgans.roll_puncher.uploading");
        else if (menu.getSlot(1).hasItem())
            titleText = Component.translatable("gui.pipeorgans.roll_puncher.finished");
        else
            titleText = title;

        pGuiGraphics.drawString(font, titleText, x + (GUI_WIDTH - 8 - font.width(titleText)) / 2, y+4, 0x505050, false);

        if (midisArea == null)
            pGuiGraphics.drawString(font, Component.translatable("gui.pipeorgans.roll_puncher.noMidis"), x+54, y+26, 0xD3D3D3);

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(x+GUI_WIDTH, y+GUI_HEIGHT-40, -200)
                .scale(3)
                .render(pGuiGraphics);

        int width = (int) (AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.getWidth() * Mth.lerp(pPartialTick, lastChasingProgress, chasingProgress));
        int height = AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.getHeight();
        pGuiGraphics.blit(AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.location, x+70, y+59,
                AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.getStartX(), AllGuiTextures.SCHEMATIC_TABLE_PROGRESS.getStartY(), width, height);
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        boolean finished = menu.getSlot(1).hasItem();

        if (menu.contentHolder.isUploading || finished) {
            if (finished)
                chasingProgress = lastChasingProgress = progress = 1;
            else {
                lastChasingProgress = chasingProgress;
                progress = menu.contentHolder.uploadingProgress;
                chasingProgress += (progress - chasingProgress) * 0.5f;
            }
            confirmButton.active = false;

            if (midisLabel != null) {
                midisLabel.colored(0xCCDDFF);
                String uploadingMidi = menu.contentHolder.uploadingMidi;
                if (uploadingMidi == null)
                    midisLabel.text = null;
                else
                    midisLabel.text = Component.literal(uploadingMidi);
            }
            if (midisArea != null)
                midisArea.visible = false;

        } else {
            progress = 0;
            chasingProgress = lastChasingProgress = 0;
            confirmButton.active = true;

            if (midisLabel != null)
                midisLabel.colored(0xFFFFFF);
            if (midisArea != null) {
                midisArea.writingTo(midisLabel);
                midisArea.visible = true;
            }
        }
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }
}
