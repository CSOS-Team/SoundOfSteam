package com.finchy.pipeorgans.content.stop;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.StopActionPacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class StopScreen extends AbstractSimiContainerScreen<StopMenu> {

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;
    private static final int COLOR_SLOT_BORDER = 0xFF373737;
    private static final int COLOR_SLOT_INNER = 0xFF8B8B8B;

    private static final int COLOR_BTN = 0xFF5A5A5A;
    private static final int COLOR_BTN_HOVER = 0xFF6E6E6E;
    private static final int COLOR_BTN_PRESSED = 0xFFFFC83C;
    private static final int COLOR_BTN_BORDER = 0xFF101010;
    private static final int COLOR_ADD = 0xFF3C7A3C;
    private static final int COLOR_ADD_HOVER = 0xFF4E9E4E;

    private final BlockPos pos;
    private final StopBlockEntity be;

    public StopScreen(StopMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.be = container.getStopBE();
        this.pos = be.getBlockPos();
    }

    @Override
    protected void init() {
        setWindowSize(StopMenu.guiWidth(), StopMenu.guiHeight());
        setWindowOffset(0, 0);
        super.init();
    }

    private int stopCount() {
        return be.getStops().size();
    }

    private int cellCount() {
        int stops = stopCount();
        return stops < StopBlockEntity.MAX_STOPS ? stops + 1 : stops;
    }

    private int cellAt(int guiX, int guiY) {
        for (int i = 0; i < cellCount(); i++) {
            int x = StopMenu.cellX(i);
            int y = StopMenu.cellY(i);
            if (guiX >= x && guiX < x + StopMenu.BUTTON_SIZE && guiY >= y && guiY < y + StopMenu.BUTTON_SIZE)
                return i;
        }
        return -1;
    }

    // Input

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cell = cellAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (cell != -1) {
            int stops = stopCount();
            if (cell < stops) {
                if (button == 0)
                    send(StopActionPacket.simple(pos, StopActionPacket.TOGGLE, cell));
                else if (button == 1)
                    send(StopActionPacket.simple(pos, StopActionPacket.OPEN_EDIT, cell));
                return true;
            } else if (button == 0 && stops < StopBlockEntity.MAX_STOPS) {
                send(StopActionPacket.simple(pos, StopActionPacket.ADD, -1));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void send(StopActionPacket packet) {
        AllPackets.getChannel().sendToServer(packet);
    }

    // Rendering

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        // Division filter slot
        drawSlotBackground(graphics, StopMenu.DIVISION_BG_X + 1, StopMenu.DIVISION_BG_Y + 1);

        // Stop buttons + add button
        int stops = stopCount();
        int hoveredCell = cellAt(mouseX - leftPos, mouseY - topPos);
        for (int i = 0; i < cellCount(); i++) {
            int x = leftPos + StopMenu.cellX(i);
            int y = topPos + StopMenu.cellY(i);
            boolean hovered = i == hoveredCell;
            if (i < stops) {
                Stop stop = be.getStops().get(i);
                int color = stop.pressed ? COLOR_BTN_PRESSED : (hovered ? COLOR_BTN_HOVER : COLOR_BTN);
                drawButton(graphics, x, y, color);
                drawStopLabel(graphics, x, y, stop.name, stop.descriptor, stop.pressed ? 0xFF202020 : 0xFFFFFFFF,
                        stop.pressed ? 0xFF3A3A20 : 0xFFB8B8B8);
            } else {
                drawButton(graphics, x, y, hovered ? COLOR_ADD_HOVER : COLOR_ADD);
                drawAddGlyph(graphics, x, y);
            }
        }

        // Player inventory
        int invX = StopMenu.playerInvX();
        int invY = StopMenu.playerInvY();
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                drawSlotBackground(graphics, invX + col * 18, invY + row * 18);
        for (int col = 0; col < 9; col++)
            drawSlotBackground(graphics, invX + col * 18, invY + 58);
    }

    private void drawButton(GuiGraphics graphics, int x, int y, int color) {
        int s = StopMenu.BUTTON_SIZE;
        graphics.fill(x - 1, y - 1, x + s + 1, y + s + 1, COLOR_BTN_BORDER);
        graphics.fill(x, y, x + s, y + s, color);
    }

    private void drawStopLabel(GuiGraphics graphics, int x, int y, String name, String descriptor, int nameColor, int descColor) {
        float scale = 0.5f;
        int cx = x + StopMenu.BUTTON_SIZE / 2;
        int maxFontWidth = (int) ((StopMenu.BUTTON_SIZE - 3) / scale);
        boolean hasName = name != null && !name.isEmpty();
        boolean hasDesc = descriptor != null && !descriptor.isEmpty();

        if (hasName && hasDesc) {
            drawScaledCentered(graphics, cx, y + 8, trimToWidth(name, maxFontWidth), scale, nameColor);
            drawScaledCentered(graphics, cx, y + 15, trimToWidth(descriptor, maxFontWidth), scale, descColor);
        } else if (hasName) {
            drawScaledCentered(graphics, cx, y + (StopMenu.BUTTON_SIZE - (int) (font.lineHeight * scale)) / 2,
                    trimToWidth(name, maxFontWidth), scale, nameColor);
        }
    }

    private void drawScaledCentered(GuiGraphics graphics, int cx, int topY, String text, float scale, int color) {
        if (text.isEmpty())
            return;
        graphics.pose().pushPose();
        graphics.pose().translate(cx, topY, 0);
        graphics.pose().scale(scale, scale, 1f);
        int w = font.width(text);
        graphics.drawString(font, text, -w / 2, 0, color, false);
        graphics.pose().popPose();
    }

    private void drawAddGlyph(GuiGraphics graphics, int x, int y) {
        int cx = x + StopMenu.BUTTON_SIZE / 2;
        int w = font.width("+");
        graphics.drawString(font, "+", cx - w / 2, y + (StopMenu.BUTTON_SIZE - font.lineHeight) / 2, 0xFFFFFFFF, false);
    }

    private String trimToWidth(String text, int maxWidth) {
        if (font.width(text) <= maxWidth)
            return text;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (font.width(sb.toString() + text.charAt(i)) > maxWidth)
                break;
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }

    private void drawSlotBackground(GuiGraphics graphics, int guiX, int guiY) {
        int x = leftPos + guiX;
        int y = topPos + guiY;
        graphics.fill(x - 1, y - 1, x + 17, y + 17, COLOR_SLOT_BORDER);
        graphics.fill(x, y, x + 16, y + 16, COLOR_SLOT_INNER);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, StopMenu.DIVISION_BG_X, StopMenu.TITLE_Y, 0xFFFFFF, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.stop.division"),
                StopMenu.DIVISION_BG_X + 22, StopMenu.DIVISION_BG_Y + 5, 0xFFB0B0B0, false);
    }

    // Tooltips

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        // Division filter slot role tooltip
        if (hoveredSlot instanceof SlotItemHandler) {
            graphics.renderTooltip(font, Component.translatable("gui.pipeorgans.stop.division"), x, y);
            return;
        }

        // Stop button tooltip (name + descriptor)
        int cell = cellAt(x - leftPos, y - topPos);
        if (cell >= 0 && cell < stopCount()) {
            Stop stop = be.getStops().get(cell);
            List<Component> lines = new ArrayList<>();
            lines.add(Component.literal(stop.name.isEmpty() ? "Stop " + (cell + 1) : stop.name));
            if (!stop.descriptor.isEmpty())
                lines.add(Component.literal(stop.descriptor).withStyle(s -> s.withColor(0xA0A0A0)));
            graphics.renderComponentTooltip(font, lines, x, y);
            return;
        }

        super.renderTooltip(graphics, x, y);
    }
}
