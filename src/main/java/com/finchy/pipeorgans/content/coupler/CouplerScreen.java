package com.finchy.pipeorgans.content.coupler;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.CouplerActionPacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CouplerScreen extends AbstractSimiContainerScreen<CouplerMenu> {

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;
    private static final int COLOR_BTN = 0xFF5A5A5A;
    private static final int COLOR_BTN_HOVER = 0xFF6E6E6E;
    private static final int COLOR_BTN_PRESSED = 0xFF50C8FF;
    private static final int COLOR_BTN_BORDER = 0xFF101010;
    private static final int COLOR_ADD = 0xFF3C7A3C;
    private static final int COLOR_ADD_HOVER = 0xFF4E9E4E;

    private final BlockPos pos;
    private final CouplerBlockEntity be;

    public CouplerScreen(CouplerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.be = container.getCouplerBE();
        this.pos = be.getBlockPos();
    }

    @Override
    protected void init() {
        setWindowSize(CouplerMenu.guiWidth(), CouplerMenu.guiHeight());
        setWindowOffset(0, 0);
        super.init();
    }

    private int couplerCount() {
        return be.getCouplers().size();
    }

    private int cellCount() {
        int n = couplerCount();
        return n < CouplerBlockEntity.MAX_COUPLERS ? n + 1 : n;
    }

    private int cellAt(int guiX, int guiY) {
        for (int i = 0; i < cellCount(); i++) {
            int x = CouplerMenu.cellX(i);
            int y = CouplerMenu.cellY(i);
            if (guiX >= x && guiX < x + CouplerMenu.BUTTON_SIZE && guiY >= y && guiY < y + CouplerMenu.BUTTON_SIZE)
                return i;
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cell = cellAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (cell != -1) {
            int n = couplerCount();
            if (cell < n) {
                if (button == 0)
                    send(CouplerActionPacket.simple(pos, CouplerActionPacket.TOGGLE, cell));
                else if (button == 1)
                    send(CouplerActionPacket.simple(pos, CouplerActionPacket.OPEN_EDIT, cell));
                return true;
            } else if (button == 0 && n < CouplerBlockEntity.MAX_COUPLERS) {
                send(CouplerActionPacket.simple(pos, CouplerActionPacket.ADD, -1));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void send(CouplerActionPacket packet) {
        AllPackets.getChannel().sendToServer(packet);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        int n = couplerCount();
        int hoveredCell = cellAt(mouseX - leftPos, mouseY - topPos);
        for (int i = 0; i < cellCount(); i++) {
            int x = leftPos + CouplerMenu.cellX(i);
            int y = topPos + CouplerMenu.cellY(i);
            boolean hovered = i == hoveredCell;
            if (i < n) {
                Coupler coupler = be.getCouplers().get(i);
                int color = coupler.pressed ? COLOR_BTN_PRESSED : (hovered ? COLOR_BTN_HOVER : COLOR_BTN);
                drawButton(graphics, x, y, color);
                drawLabel(graphics, x, y, coupler.name, coupler.pressed ? 0xFF202020 : 0xFFFFFFFF);
            } else {
                drawButton(graphics, x, y, hovered ? COLOR_ADD_HOVER : COLOR_ADD);
                drawGlyph(graphics, x, y, "+");
            }
        }
    }

    private void drawButton(GuiGraphics graphics, int x, int y, int color) {
        int s = CouplerMenu.BUTTON_SIZE;
        graphics.fill(x - 1, y - 1, x + s + 1, y + s + 1, COLOR_BTN_BORDER);
        graphics.fill(x, y, x + s, y + s, color);
    }

    private void drawLabel(GuiGraphics graphics, int x, int y, String name, int color) {
        if (name == null || name.isEmpty())
            return;
        float scale = 0.5f;
        int maxFontWidth = (int) ((CouplerMenu.BUTTON_SIZE - 3) / scale);
        String shown = trimToWidth(name, maxFontWidth);
        int cx = x + CouplerMenu.BUTTON_SIZE / 2;
        graphics.pose().pushPose();
        graphics.pose().translate(cx, y + (CouplerMenu.BUTTON_SIZE - (int) (font.lineHeight * scale)) / 2, 0);
        graphics.pose().scale(scale, scale, 1f);
        int w = font.width(shown);
        graphics.drawString(font, shown, -w / 2, 0, color, false);
        graphics.pose().popPose();
    }

    private void drawGlyph(GuiGraphics graphics, int x, int y, String text) {
        int cx = x + CouplerMenu.BUTTON_SIZE / 2;
        int w = font.width(text);
        graphics.drawString(font, text, cx - w / 2, y + (CouplerMenu.BUTTON_SIZE - font.lineHeight) / 2, 0xFFFFFFFF, false);
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

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, CouplerMenu.GRID_X, CouplerMenu.TITLE_Y, 0xFFFFFF, false);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        int cell = cellAt(x - leftPos, y - topPos);
        if (cell >= 0 && cell < couplerCount()) {
            Coupler coupler = be.getCouplers().get(cell);
            List<Component> lines = new ArrayList<>();
            lines.add(Component.literal(coupler.name.isEmpty() ? "Coupler " + (cell + 1) : coupler.name));
            lines.add(Component.translatable("gui.pipeorgans.coupler.routes").withStyle(s -> s.withColor(0xA0A0A0)));
            graphics.renderComponentTooltip(font, lines, x, y);
            return;
        }
        super.renderTooltip(graphics, x, y);
    }
}
