package com.finchy.pipeorgans.content.piston;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.PistonActionPacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PistonScreen extends AbstractSimiContainerScreen<PistonMenu> {

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;
    private static final int COLOR_EMPTY = 0xFF3A3A3A;    // unset = dark grey
    private static final int COLOR_FILLED = 0xFFB0B0B0;   // set, idle = light grey
    private static final int COLOR_ACTIVE = 0xFF5CC85C;   // set + recalled = green
    private static final int COLOR_HOVER = 0xFF4A4A4A;
    private static final int COLOR_BORDER = 0xFF101010;
    private static final int COLOR_SELECTED = 0xFFFFC83C; // gold box = selected (to Set)

    private static final int COLOR_TUTTI_OFF = 0xFF5A5A5A;
    private static final int COLOR_TUTTI_ON = 0xFFFFC83C;
    private static final int COLOR_TUTTI_HOVER = 0xFF6E6E6E;

    private static final float LABEL_SCALE = 0.65f;

    private final BlockPos pos;
    private final PistonBlockEntity be;

    private int selected = -1;
    private int pressedPiston = -1;

    private Button setButton;
    private Button clearPistonButton;

    private int tuttiX, tuttiY, tuttiW, tuttiH;

    public PistonScreen(PistonMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.be = container.getPistonBE();
        this.pos = be.getBlockPos();
    }

    @Override
    protected void init() {
        setWindowSize(PistonMenu.guiWidth(), PistonMenu.guiHeight());
        setWindowOffset(0, 0);
        super.init();

        int bw = (PistonMenu.gridWidth() - 2 * 4) / 3; // three buttons with 4px gaps, filling grid width
        int by = topPos + PistonMenu.ACTIONS_Y;
        int bx = leftPos + PistonMenu.GRID_X;

        setButton = Button.builder(Component.translatable("gui.pipeorgans.piston.set"), b -> {
            if (selected >= 0)
                send(PistonActionPacket.SET, selected);
            selected = -1;
            updateContextButtons();
        }).bounds(bx, by, bw, PistonMenu.ACTION_H).build();
        addRenderableWidget(setButton);

        clearPistonButton = Button.builder(Component.translatable("gui.pipeorgans.piston.clear_piston"), b -> {
            if (selected >= 0)
                send(PistonActionPacket.CLEAR_PISTON, selected);
            selected = -1;
            updateContextButtons();
        }).bounds(bx, by, bw, PistonMenu.ACTION_H).build();
        addRenderableWidget(clearPistonButton);

        tuttiX = bx + (bw + 4);
        tuttiY = by;
        tuttiW = bw;
        tuttiH = PistonMenu.ACTION_H;

        addRenderableWidget(Button.builder(Component.translatable("gui.pipeorgans.piston.clear"), b -> send(PistonActionPacket.CLEAR, -1))
                .bounds(bx + 2 * (bw + 4), by, bw, PistonMenu.ACTION_H).build());

        updateContextButtons();
    }

    private void updateContextButtons() {
        boolean hasSel = selected >= 0 && selected < PistonBlockEntity.PISTON_COUNT;
        boolean set = hasSel && !be.isPresetEmpty(selected);
        setButton.visible = hasSel && !set;
        clearPistonButton.visible = hasSel && set;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateContextButtons();
    }

    private void send(int action, int index) {
        AllPackets.getChannel().sendToServer(new PistonActionPacket(pos, action, index));
    }

    private int cellAt(int guiX, int guiY) {
        for (int i = 0; i < PistonBlockEntity.PISTON_COUNT; i++) {
            int x = PistonMenu.cellX(i);
            int y = PistonMenu.cellY(i);
            if (guiX >= x && guiX < x + PistonMenu.BUTTON_SIZE && guiY >= y && guiY < y + PistonMenu.BUTTON_SIZE)
                return i;
        }
        return -1;
    }

    private boolean overTutti(double mouseX, double mouseY) {
        return mouseX >= tuttiX && mouseX < tuttiX + tuttiW && mouseY >= tuttiY && mouseY < tuttiY + tuttiH;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && overTutti(mouseX, mouseY)) {
            send(PistonActionPacket.TUTTI, -1);
            selected = -1;
            updateContextButtons();
            return true;
        }

        int cell = cellAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (cell != -1) {
            if (button == 0) {
                if (be.isPresetEmpty(cell)) {
                    selected = cell; // Select an unset piston: the Set button appears
                } else {
                    send(PistonActionPacket.RECALL, cell);
                    pressedPiston = cell;
                    selected = -1;
                }
            } else if (button == 1) {
                if (!be.isPresetEmpty(cell))
                    selected = cell; // Select a set piston: the Clear Piston button appears
                // right-clicking an unset piston does nothing
            }
            updateContextButtons();
            return true;
        }

        boolean handled = super.mouseClicked(mouseX, mouseY, button);
        selected = -1;
        updateContextButtons();
        return handled;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0)
            pressedPiston = -1; // green lasts only while held down
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        int hovered = cellAt(mouseX - leftPos, mouseY - topPos);
        for (int i = 0; i < PistonBlockEntity.PISTON_COUNT; i++) {
            int x = leftPos + PistonMenu.cellX(i);
            int y = topPos + PistonMenu.cellY(i);
            int s = PistonMenu.BUTTON_SIZE;
            boolean filled = !be.isPresetEmpty(i);

            int fill;
            if (i == pressedPiston && filled)
                fill = COLOR_ACTIVE;
            else if (i == hovered)
                fill = COLOR_HOVER;
            else
                fill = filled ? COLOR_FILLED : COLOR_EMPTY;

            int border = (i == selected) ? COLOR_SELECTED : COLOR_BORDER;
            graphics.fill(x - 1, y - 1, x + s + 1, y + s + 1, border);
            graphics.fill(x, y, x + s, y + s, fill);

            int textColor = (filled || i == pressedPiston) ? 0xFF202020 : 0xFFFFFFFF;
            drawScaledCentered(graphics, x + s / 2, y + (s - (int) (font.lineHeight * LABEL_SCALE)) / 2,
                    String.valueOf(i + 1), textColor);
        }

        boolean tuttiOn = be.isTuttiActive();
        boolean tuttiHover = overTutti(mouseX, mouseY);
        int tuttiColor = tuttiOn ? COLOR_TUTTI_ON : (tuttiHover ? COLOR_TUTTI_HOVER : COLOR_TUTTI_OFF);
        graphics.fill(tuttiX - 1, tuttiY - 1, tuttiX + tuttiW + 1, tuttiY + tuttiH + 1, COLOR_BORDER);
        graphics.fill(tuttiX, tuttiY, tuttiX + tuttiW, tuttiY + tuttiH, tuttiColor);
        graphics.drawCenteredString(font, Component.translatable("gui.pipeorgans.piston.tutti"),
                tuttiX + tuttiW / 2, tuttiY + (tuttiH - font.lineHeight) / 2, tuttiOn ? 0xFF202020 : 0xFFFFFFFF);
    }

    private void drawScaledCentered(GuiGraphics graphics, int cx, int topY, String text, int color) {
        graphics.pose().pushPose();
        graphics.pose().translate(cx, topY, 0);
        graphics.pose().scale(LABEL_SCALE, LABEL_SCALE, 1f);
        int w = font.width(text);
        graphics.drawString(font, text, -w / 2, 0, color, false);
        graphics.pose().popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, PistonMenu.GRID_X, PistonMenu.TITLE_Y, 0xFFFFFF, false);
    }
}
