package com.finchy.pipeorgans.content.musicalLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class NoteLinkScreen extends AbstractSimiContainerScreen<NoteLinkMenu> {
    public static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/note_link.png");
    public static final int GUI_WIDTH = 227;
    public static final int GUI_HEIGHT = 123;

    public static final int MAIN_WINDOW_WIDTH = GUI_WIDTH - 35;
    public static final int MAIN_WINDOW_HEIGHT = GUI_HEIGHT;

    public static final int TITLE_X = 80;
    public static final int TITLE_Y = 6;

    public static final int REDSTONE_LINK_DISPLAY_X = GUI_WIDTH + -6;
    public static final int REDSTONE_LINK_DISPLAY_Y = 30;

    public static final Font font = Minecraft.getInstance().font;

    private final Component octaveLabel = Component.translatable("gui.pipeorgans.note_link.octave");
    private final List<Component> octaveOptions = PipePitch.Octave.getAllComponents();
    private final Component pitchClassLabel = Component.translatable("gui.pipeorgans.note_link.pitch_class");
    private final List<Component> pitchClassOptions = PipePitch.PitchClass.getAllComponents();

    private IconButton confirmButton;
    private ScrollInput octaveScroll;
    private ScrollInput pitchClassScroll;

    private final NoteLinkBlockEntity blockEntity;

    private int x() {
        return getGuiLeft() + 12;
    }

    private int y() {
        return getGuiTop() + 2;
    }

    public NoteLinkScreen(NoteLinkMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.blockEntity = container.contentHolder;
    }

    @Override
    protected void init() {
        super.init();
        setWindowSize(GUI_WIDTH, GUI_HEIGHT + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(-6, 0);

        int x = x();
        int y = y();

        Label labelOctaveSelect = new Label(x + 50, y + 48, CommonComponents.EMPTY).withShadow();
        octaveScroll = new SelectionScrollInput(x + 45, y + 43, 118, 18).forOptions(octaveOptions)
                .titled(octaveLabel.plainCopy())
                .setState(blockEntity.getPitch().octave().ordinal())
                .calling(menu::setOctave)
                .writingTo(labelOctaveSelect);

        Label labelPitchClassSelect = new Label(x + 50, y + 70, CommonComponents.EMPTY).withShadow();
        pitchClassScroll = new SelectionScrollInput(x + 45, y + 65, 118, 18).forOptions(pitchClassOptions)
                .titled(pitchClassLabel.plainCopy())
                .setState(blockEntity.getPitch().pitchClass().ordinal())
                .calling(menu::setPitchClass)
                .writingTo(labelPitchClassSelect);
        addRenderableWidgets(labelOctaveSelect, octaveScroll, labelPitchClassSelect, pitchClassScroll);

        confirmButton =
                new IconButton(x + MAIN_WINDOW_WIDTH - 33, y + MAIN_WINDOW_HEIGHT - 26, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            menu.saveData(blockEntity);
        });
        addRenderableWidget(confirmButton);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 12 + (MAIN_WINDOW_WIDTH - 6) / 2 - font.width(title) / 2, 4, 0x505050, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

        int x = getGuiLeft() + 12;
        int y = getGuiTop();

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        int invX = getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.getWidth())-15;
        int invY = topPos + MAIN_WINDOW_HEIGHT + 4;
        renderPlayerInventory(pGuiGraphics, invX, invY);



        GuiGameElement.of(AllBlocks.NOTE_LINK.asStack())
                .<GuiGameElement.GuiRenderBuilder>at(x + MAIN_WINDOW_WIDTH + -6, y + MAIN_WINDOW_HEIGHT - 40, -200)
                .scale(3)
                .render(pGuiGraphics);

        GuiGameElement.of(com.simibubi.create.AllBlocks.REDSTONE_LINK.asStack())
                .<GuiGameElement.GuiRenderBuilder>at(x + REDSTONE_LINK_DISPLAY_X, y + REDSTONE_LINK_DISPLAY_Y, -200)
                .scale(3)
                .render(pGuiGraphics);
    }
}
