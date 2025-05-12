package com.finchy.pipeorgans.gui;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.network.PacketHandler;
import com.finchy.pipeorgans.network.packet.UpdateStopMasterC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("NullableProblems")
public class StopMasterScreen extends AbstractContainerScreen<StopMasterMenu> {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/stop_master.png");
    private static final int GUI_WIDTH = 170;
    private static final int GUI_HEIGHT = 134;

    public static final int COLUMN_1_X = 8;
    public static final int COLUMN_2_X = 48;
    public static final int COLUMN_3_X = 88;
    public static final int COLUMN_4_X = 128;
    public static final int ROW_1_Y = 36;
    public static final int ROW_2_Y = 54;
    public static final int ROW_3_Y = 72;
    public static final int ROW_4_Y = 90;

    public static final int CHANNEL_LABEL_X = 8;
    public static final int CHANNEL_LABEL_Y = 25;

    public StopMasterScreen(StopMasterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageWidth = GUI_WIDTH;
        imageHeight = GUI_HEIGHT;

    }

    @Override
    protected void init() {
        super.init();

        addChannelButton(0, COLUMN_1_X, ROW_1_Y);
        addChannelButton(1, COLUMN_1_X, ROW_2_Y);
        addChannelButton(2, COLUMN_1_X, ROW_3_Y);
        addChannelButton(3, COLUMN_1_X, ROW_4_Y);

        addChannelButton(4, COLUMN_2_X, ROW_1_Y);
        addChannelButton(5, COLUMN_2_X, ROW_2_Y);
        addChannelButton(6, COLUMN_2_X, ROW_3_Y);
        addChannelButton(7, COLUMN_2_X, ROW_4_Y);

        addChannelButton(8, COLUMN_3_X, ROW_1_Y);
        addChannelButton(9, COLUMN_3_X, ROW_2_Y);
        addChannelButton(10, COLUMN_3_X, ROW_3_Y);
        addChannelButton(11, COLUMN_3_X, ROW_4_Y);

        addChannelButton(12, COLUMN_4_X, ROW_1_Y);
        addChannelButton(13, COLUMN_4_X, ROW_2_Y);
        addChannelButton(14, COLUMN_4_X, ROW_3_Y);
        addChannelButton(15, COLUMN_4_X, ROW_4_Y);

    }

    protected void addChannelButton(int channel, int x, int y) {
        addRenderableWidget(Button.builder(
                Component.literal(Integer.toString(channel+1)), b -> sendUpdatePacket(channel, "") // toggle channel, don't touch mapping
        ).pos(leftPos+x, topPos+y).size(16, 16).build());
    }

    public void sendUpdatePacket(int toggledChannel, String newMapping) {
        UpdateStopMasterC2SPacket packet = UpdateStopMasterC2SPacket.createPacket(toggledChannel, newMapping, menu.blockEntity.getBlockPos());
        PacketHandler.sendToServer(packet);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderChecks(pGuiGraphics);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, title, titleLabelX, titleLabelY, 4210752, false);
        pGuiGraphics.drawString(font, Component.translatable("gui.pipeorgans.stop_master.channel_label"), CHANNEL_LABEL_X, CHANNEL_LABEL_Y, 4210752, false);
    }

    protected void renderChecks(GuiGraphics pGuiGraphics) {
        for (int i=0; i<=15; i++) {
            if (menu.blockEntity.getChannel(i)) { // if channel is enabled
                int x = (26 + (int) Math.floor(((double) i /4))*40);
                int y = (36 + (i % 4)*18);
                pGuiGraphics.blit(GUI_TEXTURE, leftPos+x, topPos+y, 176, 0, 16, 16, 256, 256);
            }
        }
    }
}
