package com.finchy.pipeorgans.gui;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StopMasterScreen extends AbstractContainerScreen<StopMasterMenu> {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/midi_config.png");
    private static final int GUI_WIDTH = 208;
    private static final int GUI_HEIGHT = 134;

    private int cornerX;
    private int cornerY;

    public static final int COLUMN_1_X = 0;
    public static final int COLUMN_1_Y = 0;

    public StopMasterScreen(StopMasterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(Button.builder(
                Component.literal("1"),
                b -> menu.blockEntity.toggleChannel(1)
        ).pos(cornerX+COLUMN_1_X, cornerY+COLUMN_1_Y).size(16, 16).build());

    }

    private void reevaluateCorners() {
        cornerX = (Minecraft.getInstance().getWindow().getGuiScaledWidth()-GUI_WIDTH)/2;
        cornerY = (Minecraft.getInstance().getWindow().getGuiScaledHeight()-GUI_HEIGHT)/2;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }
}
